package com.awesome.domains.project.entities;

import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@AwesomeBootTest
class UserTest {
    @Autowired
    private UserDAO userDAO;

    @Test
    void testAssertNotNull() {
        assertNotNull(userDAO);
    }

    @Test
    @Transactional
    void createUserTest(){
        UserEntity user = new UserEntity();

        user.setId(1L);
        user.setUserName("경력직 김씨");
        user.setUserPosition(UserPosition.MEMBER);
        user.setCreatedAt(LocalDateTime.now());

        userDAO.save(user);
    }
}