package com.skillgraph.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Logging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Creates and manages the Neo4j Driver bean that connects to CognoDB.
 *
 * CognoDB is fully compatible with the official Neo4j Java Driver via the
 * Bolt protocol. Credentials are read exclusively from environment variables —
 * nothing is hardcoded here.
 *
 * Environment variables required:
 *   COGNODB_URI       – bolt:// or neo4j:// URI from the CognoDB dashboard
 *   COGNODB_USERNAME  – defaults to "cognodb" if not set
 *   COGNODB_PASSWORD  – password from the CognoDB dashboard
 */
@Configuration
public class Neo4jConfig {

    @Value("${cognodb.uri}")
    private String uri;

    @Value("${cognodb.username}")
    private String username;

    @Value("${cognodb.password}")
    private String password;

    @Bean
    public Driver neo4jDriver() {
        Config driverConfig = Config.builder()
                .withMaxConnectionPoolSize(10)
                .withConnectionAcquisitionTimeout(30, TimeUnit.SECONDS)
                .withConnectionTimeout(15, TimeUnit.SECONDS)
                .withMaxTransactionRetryTime(30, TimeUnit.SECONDS)
                .withLogging(Logging.javaUtilLogging(Level.WARNING))
                .build();

        return GraphDatabase.driver(uri, AuthTokens.basic(username, password), driverConfig);
    }
}
