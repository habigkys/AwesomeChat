package com.awesome.domains.entities;
import java.time.LocalDate;
import com.awesome.domains.enums.TaskType;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@AwesomeBootTest
class ProjectTest {
    @Autowired
    private ProjectTaskDAO projectTaskDAO;
    @Test
    void testAssertNotNull() {
        assertNotNull(projectTaskDAO);
    }

    @Transactional
    @Test
    void testSave() {
        ProjectTask projectTask = new ProjectTask();
        projectTask.setProjectId(0L);
        projectTask.setParentTaskId(0L);
        projectTask.setSummary("");
        projectTask.setTaskStartDate(LocalDate.now());
        projectTask.setTaskEndDate(LocalDate.now());
        projectTask.setPersons("");
        projectTask.setType(TaskType.ISSUE);
        projectTask.setCreatedAt(LocalDateTime.now());
        projectTask.setUpdatedAt(LocalDateTime.now());

        projectTaskDAO.save(projectTask);

    }

}