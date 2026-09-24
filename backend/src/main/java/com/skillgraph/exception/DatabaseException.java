package com.skillgraph.exception;

/**
 * Wraps Neo4j / CognoDB driver exceptions so that internal connection
 * details are never leaked to API consumers.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
