package com.tiqwab.example.domain.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class CachingCart extends Cart {

    public CachingCart() {
        super();
        loadCache();
    }

    @Override
    public OrderLines getOrderLines() {
        loadCache();
        return super.getOrderLines();
    }

    @Override
    public void add(OrderLine orderLine) {
        withSyncCache(() -> super.add(orderLine), true);
    }

    @Override
    public void remove(Set<Integer> lineNo) {
        withSyncCache(() -> super.remove(lineNo), true);
    }

    @Override
    public void clear() {
        withSyncCache(super::clear, true);
    }

    Cache getCache() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ApplicationContext context = RequestContextUtils.findWebApplicationContext(attributes.getRequest());
        CacheManager cacheManager = context.getBean(CacheManager.class);
        Cache cache = cacheManager.getCache("orderLines");
        return cache;
    }

    void withSyncCache(Runnable action, boolean save) {
        Cache cache = getCache();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // TODO: The key of cache is username?
        OrderLines orderLines = cache.get(username, OrderLines.class);
        if (orderLines != null) {
            log.debug("load {} -> {}", username, orderLines);
            List<OrderLine> lines = new ArrayList<>(orderLines.getList());
            super.getOrderLines().getList().clear();
            super.getOrderLines().getList().addAll(lines);
        }
        action.run();
        if (save) {
            if (log.isDebugEnabled()) {
                log.debug("save {} -> {}", username, super.getOrderLines());
            }
            cache.put(username, super.getOrderLines());
        }
    }

    void loadCache() {
        withSyncCache(() -> {}, false);
    }

}
