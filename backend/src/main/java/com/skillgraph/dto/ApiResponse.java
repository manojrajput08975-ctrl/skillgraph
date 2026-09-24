package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Standard JSON envelope returned by every API endpoint.
 *
 * Success:  { "status": "UP",   "data": {...}, "message": "..." }
 * Error:    { "status": "DOWN", "error": "...", "message": "..." }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;
    private String error;

    private ApiResponse() {}

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.status  = "UP";
        r.data    = data;
        r.message = message;
        return r;
    }

    public static <T> ApiResponse<T> error(String error, String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.status  = "DOWN";
        r.error   = error;
        r.message = message;
        return r;
    }

    public String getStatus()  { return status;  }
    public String getMessage() { return message; }
    public T      getData()    { return data;    }
    public String getError()   { return error;   }
}
