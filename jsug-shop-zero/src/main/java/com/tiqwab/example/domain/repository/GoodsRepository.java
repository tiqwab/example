package com.tiqwab.example.domain.repository;

import com.tiqwab.example.domain.model.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long> {

    // TODO: This kind of methods are auto-generated?
    Page<Goods> findByCategoryId(Long categoryId, Pageable pageable);

}
