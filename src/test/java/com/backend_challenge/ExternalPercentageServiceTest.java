package com.backend_challenge;

import com.backend_challenge.service.ExternalPercentageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ExternalPercentageServiceTest.TestConfig.class)
class ExternalPercentageServiceTest {

    @Configuration
    @EnableCaching
    static class TestConfig {
        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("percentageCache");
        }
    }

    @Mock
    private RestTemplate restTemplate;

    private ExternalPercentageService percentageService;
    private final String TEST_URL = "https://mock-external-service.com/percentage";

    @BeforeEach
    void setUp() {
        // Inicializar el servicio con URL fija para tests
        percentageService = new ExternalPercentageService(restTemplate, TEST_URL);
    }

    @Test
    void testGetPercentage_Success() {
        // Arrange
        when(restTemplate.getForObject(eq(TEST_URL), eq(Map.class)))
                .thenReturn(Map.of("percentage", 15.0));

        // Act
        Double result = percentageService.getPercentage();

        // Assert
        assertNotNull(result);
        assertEquals(15.0, result);

        verify(restTemplate, times(1)).getForObject(eq(TEST_URL), eq(Map.class));
    }

    @Test
    void testGetPercentage_ServiceUnavailable() {
        // Arrange
        when(restTemplate.getForObject(eq(TEST_URL), eq(Map.class)))
                .thenThrow(new ResourceAccessException("Service unavailable"));

        // Act
        Double result = percentageService.getPercentage();

        // Assert
        assertNotNull(result);
        assertEquals(10.0, result);

        verify(restTemplate, times(1)).getForObject(eq(TEST_URL), eq(Map.class));
    }

    @Test
    void testGetPercentage_NullResponse() {
        // Arrange
        when(restTemplate.getForObject(eq(TEST_URL), eq(Map.class)))
                .thenReturn(null);

        // Act
        Double result = percentageService.getPercentage();

        // Assert
        assertNotNull(result);
        assertEquals(10.0, result);

        verify(restTemplate, times(1)).getForObject(eq(TEST_URL), eq(Map.class));
    }

    @Test
    void testGetPercentage_EmptyResponse() {
        // Arrange
        when(restTemplate.getForObject(eq(TEST_URL), eq(Map.class)))
                .thenReturn(Map.of("otherKey", "value"));

        // Act
        Double result = percentageService.getPercentage();

        // Assert
        assertNotNull(result);
        assertEquals(10.0, result);

        verify(restTemplate, times(1)).getForObject(eq(TEST_URL), eq(Map.class));
    }

    @Test
    void testGetPercentage_InvalidPercentageType() {
        // Arrange
        when(restTemplate.getForObject(eq(TEST_URL), eq(Map.class)))
                .thenReturn(Map.of("percentage", "not-a-number"));

        // Act
        Double result = percentageService.getPercentage();

        // Assert
        assertNotNull(result);
        assertEquals(10.0, result);

        verify(restTemplate, times(1)).getForObject(eq(TEST_URL), eq(Map.class));
    }

    @Test
    void testGetPercentage_GenericException() {
        // Arrange
        when(restTemplate.getForObject(eq(TEST_URL), eq(Map.class)))
                .thenThrow(new RuntimeException("Generic error"));

        // Act
        Double result = percentageService.getPercentage();

        // Assert
        assertNotNull(result);
        assertEquals(10.0, result);

        verify(restTemplate, times(1)).getForObject(eq(TEST_URL), eq(Map.class));
    }

}
