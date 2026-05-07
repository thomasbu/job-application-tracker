package com.tracker.job_application_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Utilise H2 en mémoire (application-test.properties) au lieu de MySQL
@SpringBootTest
@ActiveProfiles("test")
class JobApplicationTrackerApplicationTests {

    @Test
    void contextLoads() {
    }

}
