package com.backend_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testRestTemplateBean() {
        assertNotNull(context.getBean("restTemplate"));
    }

    @Test
    void testCacheManagerBean() {
        assertNotNull(context.getBean("cacheManager"));
    }
}
