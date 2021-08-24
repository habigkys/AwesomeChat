package com.awesome.dtos;

import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.domains.user.dtos.UserDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDetail {
    @JsonProperty("projectId")
    private Long projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("status")
    private ProjectStatus status;

    @JsonProperty("projectPriority")
    private ProjectPriority projectPriority;

    @JsonProperty("startDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonProperty("endDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    List<UserDTO> users;
}
