package com.lamarsan.seckill.service;

import com.lamarsan.seckill.dto.PromoDTO;

/**
 * className: PromoService
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/10 19:01
 */
public interface PromoService {
    /**
     * 根据itemId获取即将进行或正在进行的秒杀活动
     */
    PromoDTO getPromoByItemId(Long itemId);

    /**
     * 发布活动
     */
    void publishPromo(Long promoId);

    /**
     * 生成秒杀用的令牌
     */
    String generateSecondKillToken(Long promoId, Long itemId, Long userId);
}
