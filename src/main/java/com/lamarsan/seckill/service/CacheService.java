package com.lamarsan.seckill.service;

/**
 * className: CacheService
 * description: 封装本地缓存操作类
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/17 21:33
 */
public interface CacheService {
    void setCommonCache(String key, Object value);

    Object getFromCommonCache(String key);
}
