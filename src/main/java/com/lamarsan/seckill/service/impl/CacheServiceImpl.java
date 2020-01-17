package com.lamarsan.seckill.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lamarsan.seckill.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * className: CacheServiceImpl
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/17 21:34
 */
@Service
public class CacheServiceImpl implements CacheService {
    private Cache<String, Object> commonCache = null;

    @PostConstruct
    public void init() {
        commonCache = CacheBuilder.newBuilder()
                // 初始容量
                .initialCapacity(10)
                // 设置缓存中最大可以存储的100个key，超过100个按照LRU策略移除
                .maximumSize(100)
                // 一分钟后过期
                .expireAfterAccess(60, TimeUnit.SECONDS).build();
    }


    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key, value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return commonCache.getIfPresent(key);
    }
}
