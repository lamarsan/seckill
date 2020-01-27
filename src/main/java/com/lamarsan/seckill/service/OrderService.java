package com.lamarsan.seckill.service;

import com.lamarsan.seckill.dto.OrderDTO;

/**
 * className: OrderService
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 18:49
 */
public interface OrderService {

    /**
     * 通过前端url上传过来的秒杀活动id，然后下单接口内校验对应id是否属于对应商品且活动已开始
     */
    OrderDTO createOrder(Long userId, Long itemId, Long promoId, Integer amount, String stockLogId);
}
