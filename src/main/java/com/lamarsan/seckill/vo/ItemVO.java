package com.lamarsan.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * className: ItemVO
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 15:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemVO implements Serializable {
    private Long id;
    private String title;
    private BigDecimal price;
    private Long stock;
    private String description;
    private Long sales;
    private String imgUrl;

    /**
     * 0：没有秒杀 1：秒杀活动待开始 2：秒杀活动正在进行
     */
    private Integer promoStatus;
    private BigDecimal promoPrice;
    private Long promoId;
    private String startDate;
}
