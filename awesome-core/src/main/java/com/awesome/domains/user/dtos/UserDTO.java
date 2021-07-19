package com.awesome.domains.user.dtos;

import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import com.awesome.domains.projecttask.enums.TaskType;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String userName;

    private UserPosition userPosition;

    private Long userYear;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static UserDTO convert(UserEntity userEntity) {
        UserDTO userDto = new UserDTO();
        userDto.setId(userEntity.getId());
        userDto.setUserName(userEntity.getUserName());
        userDto.setUserYear(userEntity.getUserYear());
        userDto.setUserPosition(userEntity.getUserPosition());
        userDto.setCreatedAt(userEntity.getCreatedAt());
        userDto.setUpdatedAt(userEntity.getUpdatedAt());
        return userDto;
    }
}
