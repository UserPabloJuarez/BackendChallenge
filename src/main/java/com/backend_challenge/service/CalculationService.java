package com.backend_challenge.service;

import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    private final ExternalPercentageService percentageService;

    public CalculationService(ExternalPercentageService percentageService) {
        this.percentageService = percentageService;
    }

    public CalculationResult calculateWithPercentage(Double num1, Double num2) {
        try {
            Double percentage = percentageService.getPercentage();
            Double sum = num1 + num2;
            Double percentageAmount = sum * (percentage / 100);
            Double finalResult = sum + percentageAmount;

            return new CalculationResult(sum, percentage, percentageAmount, finalResult, null);
        } catch (Exception e) {
            return new CalculationResult(null, null, null, null, e.getMessage());
        }
    }

    public static class CalculationResult {
        private final Double sum;
        private final Double percentage;
        private final Double percentageAmount;
        private final Double finalResult;
        private final String error;

        public CalculationResult(Double sum, Double percentage, Double percentageAmount,
                                 Double finalResult, String error) {
            this.sum = sum;
            this.percentage = percentage;
            this.percentageAmount = percentageAmount;
            this.finalResult = finalResult;
            this.error = error;
        }

        // Getters
        public Double getSum() { return sum; }
        public Double getPercentage() { return percentage; }
        public Double getPercentageAmount() { return percentageAmount; }
        public Double getFinalResult() { return finalResult; }
        public String getError() { return error; }
    }
}
