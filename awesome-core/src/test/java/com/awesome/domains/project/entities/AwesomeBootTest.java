package com.awesome.domains.project.entities;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(
        RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles(value = {"local"})
@SpringBootTest(properties = "spring.profiles.active:local")
public @interface AwesomeBootTest {
}
