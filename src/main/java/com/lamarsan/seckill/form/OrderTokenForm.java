package com.lamarsan.seckill.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * className: OrderTokenForm
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/28 20:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderTokenForm {
    @NotNull(message = "商品id不能为空")
    Long itemId;
    Long promoId;
    @NotBlank(message = "验证码不为空")
    String verifyCode;
}
