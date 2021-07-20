package com.awesome.domains.project.services;

import com.awesome.applications.tx.ProjectTXService;
import com.awesome.domains.project.entities.AwesomeBootTest;
import com.awesome.domains.user.dtos.UserDTO;
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