package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Payload returned by GET /api/health.
 * Credentials and full URIs are never included here.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthDto {

    private String status;
    private String database;
    private String message;
    private String neo4jVersion;

    public HealthDto(String status, String database, String message, String neo4jVersion) {
        this.status       = status;
        this.database     = database;
        this.message      = message;
        this.neo4jVersion = neo4jVersion;
    }

    public String getStatus()       { return status;       }
    public String getDatabase()     { return database;     }
    public String getMessage()      { return message;      }
    public String getNeo4jVersion() { return neo4jVersion; }
}
