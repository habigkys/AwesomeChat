package com.awesome.domains.services;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectDAO;
import com.awesome.domains.entities.ProjectTask;
import com.awesome.domains.entities.ProjectTaskDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private ProjectDAO projectDAO;
    private ProjectTaskDAO projectTaskDAO;

    public ProjectService(ProjectDAO projectDAO, ProjectTaskDAO projectTaskDao) {
        this.projectDAO = projectDAO;
        this.projectTaskDAO = projectTaskDAO;
    }

    /**
     * 1. 프로젝트 리스트 - ProjectController
     * @return
     */
    public List<ProjectDTO> getProjectList(){
        List<Project> projectList = projectDAO.findAll();
        List<ProjectDTO> projectDTOList = getProjectDTOS(projectList);

        return projectDTOList;
    }

    /**
     * 2. 특정 프로젝트 - ProjectController
     * @param id
     * @return
     */
    public ProjectDTO getProject(Long id){
        return ProjectDTO.convert(projectDAO.findById(id).get());
    }

    /**
     * 3. 프로젝트 Like 검색 - ProjectController
     * @param projectName
     * @return
     */
    public List<ProjectDTO> getProjectNameLike(String projectName){
        List<Project> projectNameLikeList = projectDAO.findAllByProjectNameLike(projectName);
        List<ProjectDTO> projectNameLikeDTOList = getProjectDTOS(projectNameLikeList);

        return projectNameLikeDTOList;
    }

    /**
     * 4. 특정 프로젝트의 타스크 리스트 - ProjectController
     * @param id
     * @return
     */
    public List<ProjectTaskDTO> getProjectTaskListByProject(Long id){
        List<ProjectTask> projectTaskList = projectTaskDAO.findAllByProjectId(id);
        List<ProjectTaskDTO> projectTaskDTOList = getProjectTaskDTOS(projectTaskList);

        return projectTaskDTOList;
    }

    /**
     * 5. 프로젝트 생성 - ProjectController
     * @param projectDto
     * @return
     */
    public ProjectDTO createProject(ProjectDTO projectDto){
        if(!isCorrectProjectDate(projectDto)) {
            // todo
        }

        Project toCreateProject = new Project();
        toCreateProject.setProjectName(projectDto.getProjectName());
        toCreateProject.setSummary(projectDto.getSummary());
        toCreateProject.setStartDate(projectDto.getStartDate());
        toCreateProject.setEndDate(projectDto.getEndDate());
        toCreateProject.setCreatedAt(LocalDateTime.now());
        toCreateProject.setUpdatedAt(LocalDateTime.now());

        return ProjectDTO.convert(projectDAO.save(toCreateProject));
    }

    /**
     * 6. 프로젝트 수정 - ProjectController
     * @param projectDto
     * @return
     */
    public ProjectDTO updateProject(ProjectDTO projectDto){
        if(!isCorrectProjectDate(projectDto)) {
            // todo
        }

        Optional<Project> byId = projectDAO.findById(projectDto.getId());

        Project toUpdateOne = byId.get();
        toUpdateOne.setProjectName(projectDto.getProjectName());
        toUpdateOne.setSummary(projectDto.getSummary());
        toUpdateOne.setStartDate(projectDto.getStartDate());
        toUpdateOne.setEndDate(projectDto.getEndDate());
        toUpdateOne.setUpdatedAt(LocalDateTime.now());

        return ProjectDTO.convert(projectDAO.save(toUpdateOne));
    }

    /**
     * 7. 프로젝트 삭제 - ProjectController
     * @param id
     */
    public void deleteProject(Long id){
        projectDAO.deleteById(id);
    }

    /**
     * 1. 프로젝트 타스크/이슈 리스트 - ProjectTaskController
     * @return
     */
    public List<ProjectTaskDTO> getProjectTaskList(){
        List<ProjectTask> projectTaskList = projectTaskDAO.findAll();
        List<ProjectTaskDTO> projectTaskDTOList = getProjectTaskDTOS(projectTaskList);

        return projectTaskDTOList;
    }

    /**
     * 2. 특정 프로젝트 타스크/이슈 - ProjectTaskController
     * @param id
     * @return
     */
    public ProjectTaskDTO getProjectTask(Long id){
        return ProjectTaskDTO.convert(projectTaskDAO.findById(id).get());
    }

    /**
     * 3. 프로젝트 타스크/이슈 생성 - ProjectTaskController
     * @param projectTaskDto
     * @param projectId
     * @return
     */
    public ProjectTaskDTO createProjectTask(ProjectTaskDTO projectTaskDto, Long projectId){
        if(!isCorrectProjectTaskDate(projectTaskDto)) {
            // todo
        }

        ProjectTask toCreateProjectTask = new ProjectTask();
        toCreateProjectTask.setId(projectTaskDto.getId());
        toCreateProjectTask.setProjectId(projectId);
        toCreateProjectTask.setParentTaskId(projectTaskDto.getParentTaskId());
        toCreateProjectTask.setPersons(projectTaskDto.getPersons());
        toCreateProjectTask.setType(projectTaskDto.getType());
        toCreateProjectTask.setSummary(projectTaskDto.getSummary());
        toCreateProjectTask.setTaskStartDate(projectTaskDto.getTaskStartDate());
        toCreateProjectTask.setTaskEndDate(projectTaskDto.getTaskEndDate());
        toCreateProjectTask.setCreatedAt(LocalDateTime.now());
        toCreateProjectTask.setUpdatedAt(LocalDateTime.now());

        return ProjectTaskDTO.convert(projectTaskDAO.save(toCreateProjectTask));
    }

    /**
     * 4. 프로젝트 타스크/이슈 업데이트 - ProjectTaskController
     * @param projectTaskDto
     * @return
     */
    public ProjectTaskDTO updateProjectTask(ProjectTaskDTO projectTaskDto){
        if(!isCorrectProjectTaskDate(projectTaskDto)) {
            // todo
        }

        Optional<ProjectTask> byId = projectTaskDAO.findById(projectTaskDto.getId());

        ProjectTask toUpdateOne = byId.get();
        toUpdateOne.setSummary(projectTaskDto.getSummary());
        toUpdateOne.setPersons(projectTaskDto.getPersons());
        toUpdateOne.setTaskStartDate(projectTaskDto.getTaskStartDate());
        toUpdateOne.setTaskEndDate(projectTaskDto.getTaskEndDate());
        toUpdateOne.setType(projectTaskDto.getType());
        toUpdateOne.setUpdatedAt(LocalDateTime.now());

        return ProjectTaskDTO.convert(projectTaskDAO.save(toUpdateOne));
    }

    /**
     * 5. 프로젝트 타스크/이슈 삭제- ProjectTaskController
     * @param id
     */
    public void deleteProjectTask(Long id){
        projectTaskDAO.deleteById(id);
    }


    /**
     * Project Entity -> ProjectDTO
     * @param projectList
     * @return
     */
    private List<ProjectDTO> getProjectDTOS(List<Project> projectList) {
        List<ProjectDTO> projectDTOList = new ArrayList<>();

        for(Project projectObj : projectList){
            projectDTOList.add(ProjectDTO.convert(projectObj));
        }
        return projectDTOList;
    }

    /**
     * ProjectTask Entity -> ProjectTaskDTO
     * @param projectTaskList
     * @return
     */
    private List<ProjectTaskDTO> getProjectTaskDTOS(List<ProjectTask> projectTaskList) {
        List<ProjectTaskDTO> projectTaskDTOList = new ArrayList<>();

        for(ProjectTask projectTaskObj : projectTaskList){
            projectTaskDTOList.add(ProjectTaskDTO.convert(projectTaskObj));
        }
        return projectTaskDTOList;
    }

    /**
     * 프로젝트 시작일, 종료일 날짜 체크
     * @param projectDto
     * @return
     */
    private boolean isCorrectProjectDate(ProjectDTO projectDto) {
        if(projectDto.getEndDate().isAfter(projectDto.getStartDate())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 타스크 시작일, 종료일 날짜 체크
     * @param projectTaskDto
     * @return
     */
    private boolean isCorrectProjectTaskDate(ProjectTaskDTO projectTaskDto) {
        if(projectTaskDto.getTaskEndDate().isAfter(projectTaskDto.getTaskStartDate())){
            return true;
        }else{
            return false;
        }
    }
}
