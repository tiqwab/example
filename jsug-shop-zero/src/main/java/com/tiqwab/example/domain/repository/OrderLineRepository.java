package com.tiqwab.example.domain.repository;

import com.tiqwab.example.domain.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
}
