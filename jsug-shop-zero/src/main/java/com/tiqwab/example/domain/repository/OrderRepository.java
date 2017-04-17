package com.tiqwab.example.domain.repository;

import com.tiqwab.example.domain.model.DemoOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<DemoOrder, Long> {
}
