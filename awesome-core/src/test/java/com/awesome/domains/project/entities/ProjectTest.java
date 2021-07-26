package com.awesome.domains.project.entities;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@AwesomeBootTest
class ProjectTest {
    @Autowired
    private ProjectDAO projectDAO;
    @Test
    void testAssertNotNull() {
        assertNotNull(projectDAO);
    }
}