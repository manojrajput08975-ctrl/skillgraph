package com.skillgraph.service;

import com.skillgraph.dto.HealthDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Verifies that the application can communicate with CognoDB.
 *
 * The health check runs a minimal Cypher query ("RETURN 1") which is
 * the standard way to ping a Neo4j-compatible database without touching
 * any application data.
 */
@Service
public class HealthService {

    private static final Logger log = LoggerFactory.getLogger(HealthService.class);

    private final Driver driver;

    public HealthService(Driver driver) {
        this.driver = driver;
    }

    /**
     * Attempts a lightweight query against CognoDB.
     *
     * @return HealthDto with status "UP" on success, "DOWN" on failure.
     *         Credentials and URIs are never included in the returned object.
     */
    public HealthDto checkHealth() {
        try (Session session = driver.session()) {
            // Minimal ping query — works on any Neo4j-compatible database
            var result = session.run("RETURN 1 AS ping");
            result.single(); // consumes the result

            // Fetch the server version for informational purposes
            String version = fetchServerVersion(session);

            log.info("CognoDB health check passed");
            return new HealthDto(
                    "UP",
                    "CognoDB",
                    "Database connection successful",
                    version
            );

        } catch (ServiceUnavailableException ex) {
            log.error("CognoDB health check failed — service unavailable");
            return new HealthDto(
                    "DOWN",
                    "CognoDB",
                    "CognoDB is unreachable. Verify COGNODB_URI and network access.",
                    null
            );
        } catch (Neo4jException ex) {
            // Covers AuthenticationException, ClientException, etc.
            // We log the code but NOT the message (may contain URI/credentials)
            log.error("CognoDB health check failed — Neo4j error code: {}", ex.code());
            return new HealthDto(
                    "DOWN",
                    "CognoDB",
                    "Database error: " + sanitizeErrorCode(ex.code()),
                    null
            );
        } catch (Exception ex) {
            log.error("CognoDB health check failed — unexpected error", ex);
            return new HealthDto(
                    "DOWN",
                    "CognoDB",
                    "Unexpected error during health check.",
                    null
            );
        }
    }

    /**
     * Tries to retrieve the server version string.
     * Returns null gracefully if the query is not supported by CognoDB.
     */
    private String fetchServerVersion(Session session) {
        try {
            var versionResult = session.run("CALL dbms.components() YIELD versions RETURN versions[0] AS version");
            if (versionResult.hasNext()) {
                return versionResult.single().get("version").asString(null);
            }
        } catch (Exception ex) {
            // CognoDB may not support dbms.components() — that is fine
            log.debug("Could not retrieve server version: {}", ex.getMessage());
        }
        return null;
    }

    /**
     * Returns only the Neo4j error code category, never the full message
     * which might contain connection details.
     */
    private String sanitizeErrorCode(String code) {
        if (code == null) return "Unknown";
        // Neo4j codes look like "Neo.ClientError.Security.Unauthorized"
        // We return only the last segment
        String[] parts = code.split("\\.");
        return parts[parts.length - 1];
    }
}
