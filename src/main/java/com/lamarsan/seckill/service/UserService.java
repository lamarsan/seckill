package com.lamarsan.seckill.service;

import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.entities.UserDO;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.vo.UserVO;

/**
 * className: UserService
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/3 22:18
 */
public interface UserService {
    UserDTO getUserById(Long id);

    void register(UserDTO userDTO);

    /**
     * @param telphone 注册手机
     * @param encrptPassword 加密密码
     */
    UserDTO validateLogin(String telphone, String encrptPassword);

    UserDTO getUserByIdInCache(Long id);
}
