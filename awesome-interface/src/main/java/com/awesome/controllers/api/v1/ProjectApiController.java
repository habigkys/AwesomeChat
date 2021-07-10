package com.awesome.controllers.api.v1;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectDAO;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController("/api/v1/projects")
public class ProjectApiController {

  private final ProjectDAO projectDAO;


  private final ProjectServiceImpl projectService;


  @GetMapping("/{id}")
  public ResponseEntity<ProjectDTO> geById(@PathVariable Long id) {
    Optional<Project> byId = projectDAO.findById(id);

    if (byId.isEmpty()) {
      return (ResponseEntity<ProjectDTO>) ResponseEntity.EMPTY;
    }
    return ResponseEntity.ok(ProjectDTO.convert(byId.get()));
  }

  @GetMapping
  public ResponseEntity<ProjectDTO> getAllByIds() {
    return null;
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProjectDTO> update(@PathVariable Long id) {
    return null;
  }

  @PostMapping
  public ResponseEntity<ProjectDTO> create(@RequestBody ProjectDTO projectDTO) {
    return null;
  }



  @Getter
  @Setter
  public static class ProjectDTO {

    private Long id;

    private String projectName;

    private String summary;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ProjectDTO convert(Project project) {
      ProjectDTO dto = new ProjectDTO();
      dto.setId(project.getId());
      dto.setProjectName(project.getProjectName());
      dto.setSummary(project.getSummary());
      dto.setStartDate(project.getStartDate());
      dto.setEndDate(project.getEndDate());
      dto.setCreatedAt(project.getCreatedAt());
      dto.setUpdatedAt(project.getUpdatedAt());
      return dto;
    }
  }

  @Service
  public class ProjectServiceImpl {
    private ProjectDAO projectDAO;

    public List<Project> getAllProjectByName(String projectName) {

      if (StringUtils.isBlank(projectName)) {
        return Lists.newArrayList();
      }
      return projectDAO.findAllByProjectNameLike(projectName);
    }
  }

}
