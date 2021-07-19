package com.awesome.controllers.api.v1;

import com.awesome.applications.tx.ProjectTXService;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ProjectTXService projectTXService;

    /**
     * 1. 유저 리스트
     * @return
     */
    @GetMapping("/")
    public List<UserDTO> userList() {
        List<UserDTO> userList = userService.getUserList();

        return userList;
    }

    /**
     * 2. 특정 유저
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public UserDTO userOne(@PathVariable("userId") Long userId) {
        UserDTO user = userService.getUser(userId);

        return user;
    }

    /**
     * 3. 유저 Like 검색
     * @param userName
     * @return
     */
    @GetMapping("/nameLike/{userName}")
    public List<UserDTO> userNameLike(@PathVariable("userName") String userName) {
        List<UserDTO> userNameLike = userService.getUserNameLike(userName);

        return userNameLike;
    }

    /**
     * 5. 유저 생성
     * @param UserDTO
     * @return
     */
    @PostMapping
    public UserDTO userCreate(@RequestBody UserDTO UserDTO) {
        UserDTO createduser = userService.createUser(UserDTO);

        return createduser;
    }

    /**
     * 6. 유저 수정
     * @param UserDTO
     * @return
     */
    @PutMapping("/{id}")
    public UserDTO userUpdate(@RequestBody UserDTO UserDTO) {
        UserDTO updateduser = userService.updateUser(UserDTO);

        return updateduser;
    }

    /**
     * 7. 유저 삭제
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public String userDelete(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);

        return null;
    }

    /**
     * 8. 특정 프로젝트의 유저 리스트 조회
     * @param projectId
     * @return
     */
    @GetMapping("/{projectId}/users")
    public List<UserDTO> projectUserList(@PathVariable("projectId") Long projectId) {
        List<UserDTO> userList = projectTXService.getProjectUserList(projectId);

        return userList;
    }
}