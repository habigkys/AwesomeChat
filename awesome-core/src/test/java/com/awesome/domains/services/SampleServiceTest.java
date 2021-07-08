package com.awesome.domains.services;

import com.awesome.domains.entities.AwesomeBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@AwesomeBootTest
class SampleServiceTest {

    @Autowired
    SampleService sampleService;

    @Test
    void testServiceNotNull() {

        assertNotNull(sampleService);
    }
}