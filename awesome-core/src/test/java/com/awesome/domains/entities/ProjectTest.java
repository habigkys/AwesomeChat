package com.awesome.domains.entities;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@AwesomeBootTest
class ProjectTest {
    @Autowired
    private ProjectDAO projectDAO;
    @Test
    void testAssertNotNull() {
        assertNotNull(projectDAO);
    }

    @Test
    void testSave() {
        Project project = new Project();
        project.setId(0L);
        project.setProjectName("");
        project.setSummary("");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now());
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        projectDAO.save(project);
    }

    @Test
    void testUpdate() {
        Optional<Project> byId = projectDAO.findById(1L);
        assertFalse(byId.isEmpty());

        Project one = byId.get();
        one.setProjectName("testfire");
        projectDAO.save(one);
        Optional<Project> after = projectDAO.findById(1L);

        assertEquals("testfire", after.get().getProjectName());
    }

    @Test
    void testSelect() {
        Optional<Project> byId = projectDAO.findById(1L);
        assertEquals("testfire", byId.get().getProjectName());
    }
}