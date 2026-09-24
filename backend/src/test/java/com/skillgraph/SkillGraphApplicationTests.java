package com.skillgraph;

import com.skillgraph.service.HealthService;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies that:
 *  1. The Spring context loads correctly (all beans wire up).
 *  2. HealthService returns "DOWN" gracefully when CognoDB is unreachable.
 *
 * A real CognoDB connection is NOT required to run these tests.
 * The Neo4j Driver bean is mocked so no network call is made.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "cognodb.uri=bolt://localhost:7687",
        "cognodb.username=test",
        "cognodb.password=test"
})
class SkillGraphApplicationTests {

    // Mock the driver so Spring context loads without a real CognoDB instance
    @MockBean
    private Driver driver;

    @Test
    void contextLoads() {
        // If this passes, all beans wired correctly
        assertNotNull(driver);
    }

    @Test
    void healthServiceReturnsDownWhenDatabaseUnreachable() {
        // Arrange: driver.session() throws ServiceUnavailableException
        when(driver.session()).thenThrow(new ServiceUnavailableException("Connection refused"));

        HealthService healthService = new HealthService(driver);

        // Act
        var result = healthService.checkHealth();

        // Assert
        assertEquals("DOWN", result.getStatus());
        assertEquals("CognoDB", result.getDatabase());
        assertNotNull(result.getMessage());
    }

    @Test
    void healthServiceReturnsUpWhenDatabaseReachable() {
        // Arrange: mock a successful session and query result
        Session mockSession = mock(Session.class);
        var mockResult = mock(org.neo4j.driver.Result.class);
        var mockRecord = mock(org.neo4j.driver.Record.class);

        when(driver.session()).thenReturn(mockSession);
        when(mockSession.run("RETURN 1 AS ping")).thenReturn(mockResult);
        when(mockResult.single()).thenReturn(mockRecord);
        // version query throws — that is fine, version is optional
        when(mockSession.run("CALL dbms.components() YIELD versions RETURN versions[0] AS version"))
                .thenThrow(new RuntimeException("not supported"));

        HealthService healthService = new HealthService(driver);

        // Act
        var result = healthService.checkHealth();

        // Assert
        assertEquals("UP", result.getStatus());
        assertEquals("CognoDB", result.getDatabase());
    }
}
