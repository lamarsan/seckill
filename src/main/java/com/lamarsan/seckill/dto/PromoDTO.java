package com.lamarsan.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * className: PromoDTO
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/10 18:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PromoDTO implements Serializable {
    private Long id;

    /**
     * 秒杀活动状态 1表示未开始 2表示进行中 3表示已结束
     */
    private Integer status;

    /**
     * 秒杀活动名称
     */
    private String promoName;

    /**
     * 秒杀活动开始时间
     */
    private DateTime startDate;

    private DateTime endDate;

    private Long itemId;

    private BigDecimal promoItemPrice;
}
