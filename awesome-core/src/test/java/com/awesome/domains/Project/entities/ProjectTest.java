package com.awesome.domains.Project.entities;
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

    @Test
    void testSave() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(0L);
        projectEntity.setProjectName("");
        projectEntity.setSummary("");
        projectEntity.setStartDate(LocalDate.now());
        projectEntity.setEndDate(LocalDate.now());
        projectEntity.setCreatedAt(LocalDateTime.now());
        projectEntity.setUpdatedAt(LocalDateTime.now());

        projectDAO.save(projectEntity);
    }

    @Test
    void testUpdate() {
        Optional<ProjectEntity> byId = projectDAO.findById(1L);
        assertFalse(byId.isEmpty());

        ProjectEntity one = byId.get();
        one.setProjectName("testfire");
        projectDAO.save(one);
        Optional<ProjectEntity> after = projectDAO.findById(1L);

        assertEquals("testfire", after.get().getProjectName());
    }

    @Test
    void testSelect() {
        Optional<ProjectEntity> byId = projectDAO.findById(1L);
        assertEquals("testfire", byId.get().getProjectName());
    }
}