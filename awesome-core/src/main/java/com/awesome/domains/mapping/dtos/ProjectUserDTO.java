package com.awesome.domains.mapping.dtos;

import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectUserDTO {
    private Long id;

    private String userName;

    private UserPosition userPosition;

    private Long userYear;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ProjectUserDTO convert(UserEntity userEntity) {
        ProjectUserDTO userDto = new ProjectUserDTO();
        userDto.setId(userEntity.getId());
        userDto.setUserName(userEntity.getUserName());
        userDto.setUserYear(userEntity.getUserYear());
        userDto.setUserPosition(userEntity.getUserPosition());
        userDto.setCreatedAt(userEntity.getCreatedAt());
        userDto.setUpdatedAt(userEntity.getUpdatedAt());
        return userDto;
    }
}
