package com.awesome.domains.user.services;

import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectDAO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.domains.user.entities.UserDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    private UserDAO userDAO;
}
