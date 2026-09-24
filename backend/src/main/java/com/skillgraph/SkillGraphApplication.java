package com.skillgraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SkillGraphApplication {

    private static final Logger log = LoggerFactory.getLogger(SkillGraphApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SkillGraphApplication.class, args);
    }

    /**
     * Printed once the application context is fully started.
     * Reminds operators which environment variables are required.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("=============================================================");
        log.info("  SkillGraph API is running on http://localhost:8080");
        log.info("  Health check: GET http://localhost:8080/api/health");
        log.info("  Required env vars: COGNODB_URI, COGNODB_USERNAME, COGNODB_PASSWORD");
        log.info("=============================================================");
    }
}
