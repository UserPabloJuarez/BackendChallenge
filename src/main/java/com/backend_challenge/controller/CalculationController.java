package com.backend_challenge.controller;

import com.backend_challenge.service.CalculationService;
import com.backend_challenge.service.RequestHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class CalculationController {

    private final CalculationService calculationService;
    private final RequestHistoryService historyService;

    public CalculationController(CalculationService calculationService,
                                 RequestHistoryService historyService) {
        this.calculationService = calculationService;
        this.historyService = historyService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(
            @RequestParam Double num1,
            @RequestParam Double num2) {

        // ... código existente sin cambios ...
        CalculationService.CalculationResult result = calculationService.calculateWithPercentage(num1, num2);

        // Guardar historial de forma asíncrona
        String parameters = String.format("num1=%.2f, num2=%.2f", num1, num2);
        String response = result.getError() == null ?
                String.format("sum=%.2f, percentage=%.2f, final=%.2f",
                        result.getSum(), result.getPercentage(), result.getFinalResult()) :
                "Error: " + result.getError();

        CompletableFuture<Void> historyFuture = historyService.saveAsync(
                "/api/calculate", parameters, response, result.getError());

        if (result.getError() != null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", result.getError(),
                    "num1", num1,
                    "num2", num2
            ));
        }

        return ResponseEntity.ok(Map.of(
                "num1", num1,
                "num2", num2,
                "sum", result.getSum(),
                "percentage", result.getPercentage(),
                "percentageAmount", result.getPercentageAmount(),
                "finalResult", result.getFinalResult()
        ));
    }

    // ENDPOINT ACTUAL - MANTENER para compatibilidad
    @GetMapping("/history")
    public ResponseEntity<?> getHistory() {
        try {
            var history = historyService.getAllHistory();

            CompletableFuture<Void> historyFuture = historyService.saveAsync(
                    "/api/history", "", "Retrieved " + history.size() + " records", null);

            return ResponseEntity.ok(history);
        } catch (Exception e) {
            CompletableFuture<Void> historyFuture = historyService.saveAsync(
                    "/api/history", "", "", "Error retrieving history: " + e.getMessage());

            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // NUEVO ENDPOINT CON PAGINACIÓN
    @GetMapping("/history/paged")
    public ResponseEntity<?> getHistoryPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<?> historyPage = historyService.getHistoryWithPagination(page, size);

            // Guardar esta llamada en el historial también
            CompletableFuture<Void> historyFuture = historyService.saveAsync(
                    "/api/history/paged",
                    "page=" + page + ", size=" + size,
                    "Retrieved page " + page + " with " + historyPage.getNumberOfElements() + " records",
                    null);

            return ResponseEntity.ok(Map.of(
                    "content", historyPage.getContent(),
                    "currentPage", historyPage.getNumber(),
                    "totalItems", historyPage.getTotalElements(),
                    "totalPages", historyPage.getTotalPages(),
                    "pageSize", historyPage.getSize()
            ));

        } catch (Exception e) {
            CompletableFuture<Void> historyFuture = historyService.saveAsync(
                    "/api/history/paged",
                    "page=" + page + ", size=" + size,
                    "",
                    "Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
