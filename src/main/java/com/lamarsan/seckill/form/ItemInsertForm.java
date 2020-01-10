package com.lamarsan.seckill.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * className: ItemInsertForm
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 15:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemInsertForm {
    @NotBlank(message = "商品名称不能为空")
    private String title;
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品价格必须大于0")
    private BigDecimal price;
    @NotNull(message = "库存不能不填")
    private Long stock;
    @NotBlank(message = "商品描述信息不能为空")
    private String description;
    @NotBlank(message = "图片不能为空")
    private String imgUrl;
}
