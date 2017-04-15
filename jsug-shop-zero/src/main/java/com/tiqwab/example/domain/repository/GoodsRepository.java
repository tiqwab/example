package com.tiqwab.example.domain.repository;

import com.tiqwab.example.domain.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
