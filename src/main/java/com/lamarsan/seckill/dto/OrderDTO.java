package com.lamarsan.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * className: OrderDTO
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 18:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO implements Serializable {
    /**
     * 2018102100012828
     */
    private String id;

    private Long userId;

    private Long itemId;

    /**
     * 若非空，则表示以秒杀商品方式下单
     */
    private Long promoId;

    /**
     * 购买时的单价，若promoId非空，则表示秒杀商品价格
     */
    private BigDecimal itemPrice;

    private Integer amount;

    /**
     * 购买金额 = 商品id单价*购买数量 若promoId非空，则表示秒杀商品购买金额
     */
    private BigDecimal orderPrice;

    public OrderDTO(Long userId, Long itemId, BigDecimal itemPrice, Integer amount, BigDecimal orderPrice) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.amount = amount;
        this.orderPrice = orderPrice;
    }
}
