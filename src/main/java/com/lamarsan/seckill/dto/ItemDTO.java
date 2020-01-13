package com.lamarsan.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * className: ItemDTO
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 15:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDTO implements Serializable {
    private Long id;
    private String title;
    private BigDecimal price;
    private Long stock;
    private String description;
    private Long sales;
    private String imgUrl;

    /**
     * 如果不为空，说明还有未结束的秒杀活动
     */
    private PromoDTO promoDTO;
}
