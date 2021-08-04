package com.awesome.domains.projecttask.services;

import com.awesome.domains.projecttask.dtos.ProjectTaskDTO;
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectTaskService {
    private ProjectTaskDAO projectTaskDAO;

    /**
     * 1. 프로젝트 타스크/이슈 리스트 - ProjectTaskController
     * @return
     */
    public List<ProjectTaskDTO> getProjectTaskList(){
        List<ProjectTaskEntity> projectTaskEntityList = projectTaskDAO.findAll();

        return projectTaskEntityList.stream().map(ProjectTaskDTO::convert).collect(Collectors.toList());
    }

    /**
     * 2. 특정 프로젝트 타스크/이슈 - ProjectTaskController
     * @param taskId
     * @return
     */
    public ProjectTaskDTO getProjectTask(Long taskId){
        return ProjectTaskDTO.convert(projectTaskDAO.findById(taskId).get());
    }

    /**
     * 3. 특정 프로젝트의 타스크 리스트 - ProjectController
     * @param projectId
     * @return
     */
    public List<ProjectTaskDTO> getProjectTaskListByProject(Long projectId){
        List<ProjectTaskEntity> projectTaskEntityList = projectTaskDAO.findAllByProjectId(projectId);

        return projectTaskEntityList.stream().map(ProjectTaskDTO::convert).collect(Collectors.toList());
    }

    /**
     * 4. 프로젝트 타스크/이슈 생성 - ProjectTaskController
     * @param projectTaskDto
     * @param projectId
     * @return
     */
    public ProjectTaskDTO createProjectTask(ProjectTaskDTO projectTaskDto, Long projectId){
        if(!validateProjectTaskDate(projectTaskDto)) {
            throw new IllegalArgumentException();
        }

        ProjectTaskEntity toCreateProjectTaskEntity = new ProjectTaskEntity();
        toCreateProjectTaskEntity.setId(projectTaskDto.getId());
        toCreateProjectTaskEntity.setProjectId(projectId);
        toCreateProjectTaskEntity.setParentTaskId(projectTaskDto.getParentTaskId());
        toCreateProjectTaskEntity.setType(projectTaskDto.getType());
        toCreateProjectTaskEntity.setSummary(projectTaskDto.getSummary());
        toCreateProjectTaskEntity.setTaskPriority(projectTaskDto.getTaskPriority());
        toCreateProjectTaskEntity.setTaskStartDate(projectTaskDto.getTaskStartDate());
        toCreateProjectTaskEntity.setTaskEndDate(projectTaskDto.getTaskEndDate());

        return ProjectTaskDTO.convert(projectTaskDAO.save(toCreateProjectTaskEntity));
    }

    /**
     * 5. 프로젝트 타스크/이슈 업데이트 - ProjectTaskController
     * @param projectTaskDto
     * @param taskId
     * @return
     */
    public ProjectTaskDTO updateProjectTask(ProjectTaskDTO projectTaskDto, Long taskId){
        if(!validateProjectTaskDate(projectTaskDto)) {
            throw new IllegalArgumentException();
        }

        Optional<ProjectTaskEntity> byId = projectTaskDAO.findById(taskId);

        ProjectTaskEntity toUpdateOne = byId.get();
        toUpdateOne.setSummary(projectTaskDto.getSummary());
        toUpdateOne.setTaskPriority(projectTaskDto.getTaskPriority());
        toUpdateOne.setTaskStartDate(projectTaskDto.getTaskStartDate());
        toUpdateOne.setTaskEndDate(projectTaskDto.getTaskEndDate());
        toUpdateOne.setType(projectTaskDto.getType());

        return ProjectTaskDTO.convert(projectTaskDAO.save(toUpdateOne));
    }

    /**
     * 6. 프로젝트 타스크/이슈 삭제- ProjectTaskController
     * @param taskId
     */
    public void deleteProjectTask(Long taskId){
        projectTaskDAO.deleteById(taskId);
    }

    /**
     * 타스크 시작일, 종료일 날짜 체크
     * @param projectTaskDto
     * @return
     */
    private boolean validateProjectTaskDate(ProjectTaskDTO projectTaskDto) {
        return projectTaskDto.getTaskEndDate().isAfter(projectTaskDto.getTaskStartDate());
    }
}
