package com.tiqwab.example.domain.service;

import com.tiqwab.example.domain.model.Goods;
import com.tiqwab.example.domain.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GoodsService {

    @Autowired
    GoodsRepository goodsRepository;

    @Transactional(readOnly = true)
    public Page<Goods> findByCategoryId(Long categoryId, Pageable pageable) {
        return goodsRepository.findByCategoryId(categoryId, pageable);
    }
}
