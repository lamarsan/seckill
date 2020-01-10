package com.lamarsan.seckill.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * className: UserLoginForm
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/4 17:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginForm {
    @NotBlank(message = "手机号不能为空")
    private String telphone;
    @NotBlank(message = "密码不能为空")
    private String encrptPassword;
}
