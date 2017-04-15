package com.tiqwab.example.domain.repository;

import com.tiqwab.example.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
