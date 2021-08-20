package com.awesome.domains.user.dtos;

import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public static UserDTO convertEntityToDto(UserEntity userEntity) {
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getId());
        dto.setUserName(userEntity.getUserName());
        dto.setUserYear(userEntity.getUserYear());
        dto.setUserPosition(userEntity.getUserPosition());
        dto.setCreatedAt(userEntity.getCreatedAt());
        dto.setUpdatedAt(userEntity.getUpdatedAt());
        return dto;
    }

    public static UserEntity convertDtoToEntity(UserDTO userDTO){
        UserEntity entity = new UserEntity();
        entity.setId(userDTO.getId());
        entity.setUserName(userDTO.getUserName());
        entity.setUserYear(userDTO.getUserYear());
        entity.setUserPosition(userDTO.getUserPosition());
        entity.setCreatedAt(userDTO.getCreatedAt());
        entity.setUpdatedAt(userDTO.getUpdatedAt());
        return entity;
    }
}
