package com.awesome.domains.services;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectTask;
import com.awesome.domains.entities.ProjectTaskDAO;
import com.awesome.domains.enums.TaskType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ProjectTaskService {
    private ProjectTaskDAO projectTaskDAO;

    public ProjectTaskService(ProjectTaskDAO projectTaskDAO) {
        this.projectTaskDAO = projectTaskDAO;
    }

    /** Project Task에 대한 서비스 정의
     *  Project Task 기간 설정 (end가 start보다 클수 없음)
     *  Project Task의 소속 Project 설정
     *  Project Task의 ISSUE 설정
     *  Project Task의 소속 인원 설정
     */

    public void updateProjectTaskDate(ProjectTask projectTask){
        Optional<ProjectTask> byId = projectTaskDAO.findById(projectTask.getId());

        ProjectTask toDateUpdateOne = byId.get();
        toDateUpdateOne.setTaskStartDate(projectTask.getTaskStartDate());
        toDateUpdateOne.setTaskEndDate(projectTask.getTaskEndDate());

        projectTaskDAO.save(toDateUpdateOne);
    }

    public void setProjectTaskParent(ProjectTask projectTask){
        Optional<ProjectTask> byId = projectTaskDAO.findById(projectTask.getId());

        ProjectTask toParentUpdateOne = byId.get();
        toParentUpdateOne.setProjectId(projectTask.getProjectId());
        toParentUpdateOne.setType(TaskType.TASK);

        projectTaskDAO.save(toParentUpdateOne);
    }

    public void setProjectTaskIssue(ProjectTask projectTask){
        Optional<ProjectTask> byId = projectTaskDAO.findById(projectTask.getId());

        ProjectTask toIssueUpdateOne = byId.get();
        toIssueUpdateOne.setSummary(projectTask.getSummary());
        toIssueUpdateOne.setType(TaskType.ISSUE);

        projectTaskDAO.save(toIssueUpdateOne);
    }

    public void setProjectTaskPerson(ProjectTask projectTask){
        Optional<ProjectTask> byId = projectTaskDAO.findById(projectTask.getId());

        ProjectTask toPersonUpdateOne = byId.get();
        toPersonUpdateOne.setPersons(projectTask.getPersons());

        projectTaskDAO.save(toPersonUpdateOne);
    }
}
