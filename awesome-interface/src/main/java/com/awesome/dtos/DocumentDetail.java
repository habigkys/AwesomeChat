package com.awesome.dtos;

import com.awesome.domains.document.enums.DocumentStatus;
import com.awesome.domains.document.enums.DocumentType;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.entities.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DocumentDetail {
    private Long documentId;

    private Long projectId;

    private String projectName;

    private String summary;

    private DocumentType documentType;

    private DocumentStatus documentStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    List<UserDTO> users;
}
