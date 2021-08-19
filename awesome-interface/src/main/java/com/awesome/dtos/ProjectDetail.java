package com.awesome.dtos;

import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.domains.user.dtos.UserDTO;
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
    private Long projectId;

    private String projectName;

    private String summary;

    private ProjectStatus status;

    private ProjectPriority projectPriority;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    List<UserDTO> users;
}