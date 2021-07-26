package com.awesome.domains.project.entities;

import com.awesome.domains.projecttask.enums.TaskType;
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AwesomeBootTest
class ProjectTaskTest {

    @Autowired
    private ProjectTaskDAO projectTaskDAO;
    @Test
    void testAssertNotNull() {
        assertNotNull(projectTaskDAO);
    }
}