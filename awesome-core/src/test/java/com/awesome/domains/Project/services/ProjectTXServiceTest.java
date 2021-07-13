package com.awesome.domains.Project.services;

import com.awesome.applications.tx.ProjectTXService;
import com.awesome.domains.Project.entities.AwesomeBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@AwesomeBootTest
class ProjectTXServiceTest {

    @Autowired
    ProjectTXService projectTXService;

    @Test
    void testServiceNotNull() {

        assertNotNull(projectTXService);
    }
}