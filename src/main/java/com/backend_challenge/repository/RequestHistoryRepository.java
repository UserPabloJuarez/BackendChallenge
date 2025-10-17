package com.backend_challenge.repository;

import com.backend_challenge.model.RequestHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory, Long> {

    List<RequestHistory> findAllByOrderByTimestampDesc();

    Page<RequestHistory> findAllByOrderByTimestampDesc(Pageable pageable);
}
