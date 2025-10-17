package com.backend_challenge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Random;

@Service
public class ExternalPercentageService {

    private final RestTemplate restTemplate;
    private final String externalServiceUrl;
    private Double lastSuccessfulPercentage = 10.0; // Valor por defecto

    public ExternalPercentageService(RestTemplate restTemplate,
                                     @Value("${external.percentage.url:https://mock-external-service.com/percentage}")
                                     String externalServiceUrl) {
        this.restTemplate = restTemplate;
        this.externalServiceUrl = externalServiceUrl;
    }

    @Cacheable(value = "percentageCache", unless = "#result == null")
    public Double getPercentage() {
        try {
            System.out.println("Llamando a URL: " + externalServiceUrl);

            Map<String, Object> response = restTemplate.getForObject(externalServiceUrl, Map.class);

            if (response != null && response.containsKey("percentage")) {
                Object percentageObj = response.get("percentage");
                if (percentageObj instanceof Number) {
                    Double percentage = ((Number) percentageObj).doubleValue();
                    lastSuccessfulPercentage = percentage; // Guardar último valor exitoso
                    return percentage;
                }
            }
            // Si no hay respuesta válida, usar último valor exitoso
            return lastSuccessfulPercentage;

        } catch (ResourceAccessException e) {
            // Servicio no disponible - usar último valor exitoso
            System.out.println("Servicio externo no disponible, usando último valor: " + lastSuccessfulPercentage);
            return lastSuccessfulPercentage;
        } catch (Exception e) {
            // Cualquier otro error - usar último valor exitoso
            System.out.println("Error al obtener porcentaje, usando último valor: " + lastSuccessfulPercentage);
            return lastSuccessfulPercentage;
        }
    }

    @CacheEvict(value = "percentageCache", allEntries = true)
    @Scheduled(fixedRate = 30 * 60 * 1000) // 30 minutos
    public void evictPercentageCache() {
        // Limpiar cache cada 30 minutos
    }
}