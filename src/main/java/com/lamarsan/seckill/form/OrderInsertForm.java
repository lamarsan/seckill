package com.lamarsan.seckill.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * className: OrderInsertForm
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 20:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderInsertForm {
    @NotNull(message = "商品id不能为空")
    Long itemId;
    @NotNull(message = "商品数量不能为空")
    Integer amount;
    Long promoId;
}
