package com.lamarsan.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: UserDTO
 * description: 用户数据传输对象
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/3 22:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO implements Serializable {
    private Long id;
    private String name;
    private Integer gender;
    private Integer age;
    private String telphone;
    private String registerMode;
    private String thirdPartyId;
    private String encrptPassword;
}
