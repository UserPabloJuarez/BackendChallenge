package com.backend_challenge;

import com.backend_challenge.service.CalculationService;
import com.backend_challenge.service.ExternalPercentageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private ExternalPercentageService percentageService;

    private CalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new CalculationService(percentageService);
    }

    @Test
    void testCalculateWithPercentage_Success() {
        // Arrange
        when(percentageService.getPercentage()).thenReturn(10.0);
        Double num1 = 5.0;
        Double num2 = 15.0;

        // Act
        CalculationService.CalculationResult result = calculationService.calculateWithPercentage(num1, num2);

        // Assert
        assertNotNull(result);
        assertEquals(20.0, result.getSum());
        assertEquals(10.0, result.getPercentage());
        assertEquals(2.0, result.getPercentageAmount());
        assertEquals(22.0, result.getFinalResult());
        assertNull(result.getError());

        verify(percentageService, times(1)).getPercentage();
    }

    @Test
    void testCalculateWithPercentage_ExternalServiceError() {
        // Arrange
        when(percentageService.getPercentage()).thenThrow(new RuntimeException("Service unavailable"));
        Double num1 = 5.0;
        Double num2 = 15.0;

        // Act
        CalculationService.CalculationResult result = calculationService.calculateWithPercentage(num1, num2);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getError());
        assertTrue(result.getError().contains("Service unavailable"));

        verify(percentageService, times(1)).getPercentage();
    }
}
