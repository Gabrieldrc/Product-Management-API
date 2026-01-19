package com.hackerrank.sample;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.security.RateLimitingFilter;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HttpJsonDynamicUnitTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private jakarta.persistence.EntityManager entityManager; // Inyectado directamente

    private MockMvc mockMvc;
    private final Map<String, String> httpJsonAndTestname = new HashMap<>();
    private final Map<String, Long> executionTime = new HashMap<>();
    private final Map<String, FailureInfo> testFailures = new HashMap<>();
    private List<String> httpJsonFiles = new ArrayList<>();

    private record FailureInfo(String testCase, String reason, String expected, String found) {
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(rateLimitingFilter)
                .build();
    }

    @Test
    @Transactional
    public void dynamicTests() throws IOException {
        long startTime = System.currentTimeMillis();
        Path testcasesDir = Paths.get("src/test/resources/testcases");
        if (!Files.exists(testcasesDir)) return;

        httpJsonFiles = Files.list(testcasesDir)
                .filter(Files::isRegularFile)
                .map(f -> f.getFileName().toString())
                .filter(f -> f.endsWith(".json"))
                .sorted()
                .toList();

        loadDescriptions();
        AtomicInteger processedRequestCount = new AtomicInteger(1);

        for (String filename : httpJsonFiles) {
            resetDatabaseState(); // Lógica de limpieza encapsulada

            List<String> jsonLines = loadFileLines("testcases/" + filename);
            long fileStartTime = System.currentTimeMillis();

            for (String jsonLine : jsonLines) {
                String cleanLine = jsonLine.trim();
                if (cleanLine.isEmpty() || !cleanLine.startsWith("{")) continue;

                try {
                    processJsonLine(filename, cleanLine, processedRequestCount);
                } catch (Exception e) {
                    addTestFailure(filename, "System Error", "Execution of " + filename, "Success", e.getMessage());
                    break;
                }
                if (testFailures.containsKey(filename)) break;
            }
            executionTime.put(filename, System.currentTimeMillis() - fileStartTime);
        }

        generateConsoleReport(System.currentTimeMillis() - startTime);
        if (!testFailures.isEmpty()) {
            fail("Dynamics tests failed. See console failure report.");
        }
    }

    /**
     * Limpia la base de datos y reinicia los contadores de ID para garantizar
     * consistencia entre diferentes archivos de prueba.
     */
    private void resetDatabaseState() {
        productRepository.deleteAll();
        productRepository.flush();

        try {
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            try {
                entityManager.createNativeQuery("TRUNCATE TABLE products RESTART IDENTITY").executeUpdate();
            } catch (Exception e) {
                entityManager.createNativeQuery("TRUNCATE TABLE product RESTART IDENTITY").executeUpdate();
            }
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
        } catch (Exception e) {
            System.err.println("Note: ID reset skipped or handled by deleteAll: " + e.getMessage());
        }
    }

    private void processJsonLine(String filename, String jsonLine, AtomicInteger count) throws Exception {
        JsonNode jsonObject = OBJECT_MAPPER.readTree(jsonLine);
        JsonNode request = jsonObject.get("request");
        JsonNode response = jsonObject.get("response");

        String method = request.get("method").asText();
        String url = request.get("url").asText();
        String expectedStatus = response.get("status_code").asText();
        String body = request.has("body") && !request.get("body").isEmpty() ? request.get("body").toString() : "";

        System.out.println(Colors.BLUE_BOLD + "Processing request " + count.getAndIncrement() + " " + Colors.RESET +
                Colors.WHITE_BOLD + method + " " + url + Colors.RESET);

        ResultActions resultActions = switch (method) {
            case "POST" -> mockMvc.perform(post(url).content(body).contentType(CONTENT_TYPE_JSON));
            case "PUT" -> mockMvc.perform(put(url).content(body).contentType(CONTENT_TYPE_JSON));
            case "DELETE" -> mockMvc.perform(delete(url));
            default -> mockMvc.perform(get(url));
        };

        MockHttpServletResponse mockResponse = resultActions.andReturn().getResponse();
        String actualStatus = String.valueOf(mockResponse.getStatus());

        // Validación de Status Code
        if (!expectedStatus.equals(actualStatus)) {
            addTestFailure(filename, method + " " + url, "Status code mismatch", expectedStatus, actualStatus);
            return;
        }

        // Validación de Body (solo si es GET 200 y el JSON tiene un body esperado)
        if (method.equals("GET") && actualStatus.equals("200") && response.has("body")) {
            String actualBody = mockResponse.getContentAsString();
            JsonNode expectedBody = response.get("body");
            JsonNode actualBodyJson = OBJECT_MAPPER.readTree(actualBody);

            if (!expectedBody.equals(actualBodyJson)) {
                addTestFailure(filename, method + " " + url, "Response body mismatch", expectedBody.toString(), actualBody);
            }
        }
    }

    private void addTestFailure(String filename, String testCase, String reason, String expected, String found) {
        testFailures.putIfAbsent(filename, new FailureInfo(testCase, reason, expected, found));
    }

    private void loadDescriptions() {
        ClassPathResource resource = new ClassPathResource("testcases/description.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines().forEach(line -> {
                String[] parts = line.split(": ");
                if (parts.length >= 2) httpJsonAndTestname.put(parts[0], parts[1]);
            });
        } catch (IOException ignored) {
        }
    }

    private List<String> loadFileLines(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        }
    }

    private void generateConsoleReport(long totalTime) {
        if (!testFailures.isEmpty()) {
            System.err.println("\n" + Colors.RED_BOLD + "=== FAILURE REPORT ===" + Colors.RESET);
            testFailures.forEach((file, info) -> {
                System.err.println(Colors.WHITE_BOLD + "File: " + Colors.RESET + file);
                System.err.println(Colors.WHITE_BOLD + "Step: " + Colors.RESET + info.testCase);
                System.err.println(Colors.WHITE_BOLD + "Reason: " + Colors.RED_BOLD + info.reason + Colors.RESET);
                System.err.println(Colors.WHITE_BOLD + "Expected: " + Colors.RESET + info.expected);
                System.err.println(Colors.WHITE_BOLD + "Found: " + Colors.RESET + info.found);
                System.err.println("----------------------");
            });
        }
        System.out.println(Colors.GREEN_BOLD + "\nTOTAL TIME: " + totalTime + "ms" + Colors.RESET);
    }

    private static class Colors {
        public static final String RESET = "\033[0m";
        public static final String BLUE_BOLD = "\033[1;34m";
        public static final String WHITE_BOLD = "\033[1;37m";
        public static final String GREEN_BOLD = "\033[1;32m";
        public static final String RED_BOLD = "\033[1;31m";
    }
}