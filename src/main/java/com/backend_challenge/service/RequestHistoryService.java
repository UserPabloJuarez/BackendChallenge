package com.backend_challenge.service;

import com.backend_challenge.model.RequestHistory;
import com.backend_challenge.repository.RequestHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RequestHistoryService {

    private final RequestHistoryRepository repository;

    public RequestHistoryService(RequestHistoryRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<Void> saveAsync(String endpoint, String parameters, String response, String error) {
        RequestHistory history = new RequestHistory(endpoint, parameters, response, error);
        repository.save(history);
        return CompletableFuture.completedFuture(null);
    }

    public List<RequestHistory> getAllHistory() {
        return repository.findAllByOrderByTimestampDesc();
    }

    public Page<RequestHistory> getHistoryWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByOrderByTimestampDesc(pageable);
    }
}
