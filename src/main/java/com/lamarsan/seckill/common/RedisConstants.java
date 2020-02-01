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
    public static final String STOCK_NUM = "item_stock_";

    /**
     * 对应商品库存是否售罄
     */
    public static final String STOCK_ZERO = "promo_item_stock_invalid_";

    /**
     * 秒杀令牌
     */
    public static final String PROMO_TOKEN = "promo_item_user_token_";

    /**
     * 将大闸的限制数字设到redis内
     */
    public static final String ITEM_DOOR_COUNT = "item_door_count_";
}
