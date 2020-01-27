package com.lamarsan.seckill.common;

/**
 * className: RedisConstants
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/27 20:13
 */
public class RedisConstants {
    /**
     * 商品详情
     */
    public static final String ITEM_DTO = "item_";

    /**
     * 用户详情
     */
    public static final String USER_DTO = "user_validate_";

    /**
     * 库存详情
     */
    public static final String STOCK_NUM = "promo_item_stock_";

    /**
     * 对应商品库存是否售罄
     */
    public static final String STOCK_ZERO = "promo_item_stock_invalid_";
}
