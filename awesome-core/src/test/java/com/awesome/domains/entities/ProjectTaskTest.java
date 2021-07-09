package com.awesome.domains.entities;

import com.awesome.domains.enums.TaskType;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AwesomeBootTest
class ProjectTaskTest {

    @Autowired
    private ProjectTaskDAO projectTaskDAO;
    @Test
    void testAssertNotNull() {
        assertNotNull(projectTaskDAO);
    }

    @Test
    void testSave() {
        ProjectTask projectTask = new ProjectTask();
        projectTask.setProjectId(1L);
        projectTask.setParentTaskId(0L);
        projectTask.setSummary("");
        projectTask.setTaskStartDate(LocalDate.now());
        projectTask.setTaskEndDate(LocalDate.now());
        projectTask.setPersons("");
        projectTask.setType(TaskType.TASK);
        projectTask.setCreatedAt(LocalDateTime.now());
        projectTask.setUpdatedAt(LocalDateTime.now());

        projectTaskDAO.save(projectTask);
    }

    @Test
    void testSaveList() {
        List<ProjectTask> byIdList = projectTaskDAO.findAllByProjectId(1L);

        assertFalse(CollectionUtils.isEmpty(byIdList));
    }
}