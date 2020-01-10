package com.lamarsan.seckill.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * className: UserRigisterForm
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/4 17:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRigisterForm {
    @NotBlank(message = "用户名不能为空")
    private String name;
    @NotNull(message = "性别不能不填写")
    private Integer gender;
    @NotNull(message = "年龄不能不填写")
    @Min(value = 0, message = "年龄必须大于0岁")
    @Max(value = 150, message = "年龄必须小于150岁")
    private Integer age;
    @NotBlank(message = "手机号不能为空")
    private String telphone;
    private String registerMode;
    private String thirdPartyId;
    @NotBlank(message = "密码不能为空")
    private String encrptPassword;
    @NotBlank(message = "验证码不能为空")
    private String otpCode;
}
