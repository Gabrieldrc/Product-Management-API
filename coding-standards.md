# -----------------------------------------------------------------------------
# JAVA 21 & SPRING BOOT 3 - CODING STANDARDS CHECKLIST
# -----------------------------------------------------------------------------

1. [SYNTAX] Use 'var' for local variables when the type is obvious.
2. [SYNTAX] Use 'records' for DTOs, Configuration, and immutable data holders.
3. [SYNTAX] Mark all method parameters and class dependencies as 'final'.
4. [SYNTAX] Use Text Blocks (""" """) for multi-line strings (SQL, JSON, HTML).

5. [SPRING] Prefer Constructor Injection over @Autowired field injection.
6. [SPRING] Use @ConfigurationProperties with records and @Validated for config.
7. [SPRING] Centralize error handling using @RestControllerAdvice and ProblemDetail (RFC 7807).
8. [SPRING] Use @EnableConfigurationPropertiesScan or @EnableConfigurationProperties to register config records.

9. [TIME] Use 'java.time.Instant' or 'OffsetDateTime' exclusively. NEVER use 'java.util.Date'.

10. [CLEAN CODE] Use 'Guard Clauses' (Early Return) to avoid deep nesting and 'if-else' ladders.
11. [CLEAN CODE] All code (classes, variables, methods) must be in ENGLISH.
12. [CLEAN CODE] Comments only in ENGLISH.
13. [CLEAN CODE] No "what" comments (code must be self-explanatory).
14. [CLEAN CODE] Use Javadoc ONLY for public methods or complex business logic (explain "why").
15. [CLEAN CODE] Zero Dead Code policy: remove unused imports, variables, and commented-out code.

16. [SECURITY] Secrets (keys, passwords) must be in Environment Variables, never hardcoded.
17. [SECURITY] Use 'jakarta.validation' (@NotBlank, @Min, etc.) for DTOs and Config records.

# -----------------------------------------------------------------------------
# "If it's not final, not documented in English, or uses Date... it's not ready."
# -----------------------------------------------------------------------------