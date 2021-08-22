package com.awesome.domains.user.services;

import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    private UserDAO userDao;

    /**
     * 1. 유저 리스트 - UserController
     * @return
     */
    public List<UserDTO> getUserList(){
        List<UserEntity> UserEntityList = userDao.findAll();

        return UserEntityList.stream().map(UserDTO::convertEntityToDto).collect(Collectors.toList());
    }

    /**
     * 2. 특정 유저 - UserController
     * @param userId
     * @return
     */
    public UserDTO getUser(Long userId){
        return UserDTO.convertEntityToDto(userDao.findById(userId).get());
    }

    /**
     * 3. 유저 Like 검색 - UserController
     * @param userName
     * @return
     */
    public List<UserDTO> getUserNameLike(String userName){
        List<UserEntity> UserEntityNameLikeList = userDao.findAllByUserNameLike(userName);

        return UserEntityNameLikeList.stream().map(UserDTO::convertEntityToDto).collect(Collectors.toList());
    }

    /**
     * 4. 유저 생성 - UserController
     * @param UserDto
     * @return
     */
    public UserDTO createUser(UserDTO UserDto){
        UserEntity toCreateUserEntity = new UserEntity();
        toCreateUserEntity.setUserName(UserDto.getUserName());
        toCreateUserEntity.setUserPosition(UserDto.getUserPosition());
        toCreateUserEntity.setUserYear(UserDto.getUserYear());

        return UserDTO.convertEntityToDto(userDao.save(toCreateUserEntity));
    }

    /**
     * 5. 유저 수정 - UserController
     * @param UserDto
     * @return
     */
    public UserDTO updateUser(UserDTO UserDto, Long userId){
        Optional<UserEntity> byId = userDao.findById(userId);
        UserEntity toUpdateOne = byId.get();

        toUpdateOne.setUserName(UserDto.getUserName());
        toUpdateOne.setUserPosition(UserDto.getUserPosition());
        toUpdateOne.setUserYear(UserDto.getUserYear());

        return UserDTO.convertEntityToDto(userDao.save(toUpdateOne));
    }

    /**
     * 6. 유저 삭제 - UserController
     * @param userId
     */
    public void deleteUser(Long userId){
        userDao.deleteById(userId);
    }

    /**
     * 유저 연차 +1 - UserScheduler
     */
    @Transactional
    public void updateUserYear(){
        List<UserEntity> userList = userDao.findAll();

        for(UserEntity user : userList){
            user.setUserYear(user.getUserYear()+1);
        }

        userDao.saveAll(userList);
    }
}
