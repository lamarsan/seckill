package com.lamarsan.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * className: UserVO
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/3 22:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserVO implements Serializable {
    private Long id;

    private String name;

    private Integer gender;

    private Integer age;

    private String telphone;
}
