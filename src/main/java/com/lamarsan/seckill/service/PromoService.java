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
    PromoDTO getPromoByItemId(Long itemId);
}
