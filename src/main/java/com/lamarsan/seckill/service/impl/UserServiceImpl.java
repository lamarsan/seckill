package com.lamarsan.seckill.service.impl;

import com.lamarsan.seckill.dao.UserDAO;
import com.lamarsan.seckill.dao.UserPasswordDAO;
import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.entities.UserDO;
import com.lamarsan.seckill.entities.UserPasswordDO;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.error.EmBusinessError;
import com.lamarsan.seckill.service.UserService;
import com.lamarsan.seckill.utils.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * className: UserServiceImpl
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/3 22:19
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserPasswordDAO userPasswordDAO;
    @Autowired
    private TransferUtil transferUtil;

    @Override
    public UserDTO getUserById(Long id) {
        UserDO userDO = userDAO.selectByPrimaryKey(id);
        UserPasswordDO userPasswordDO = userPasswordDAO.selectByUserId(id);
        return transferUtil.transferToUserDTO(userDO, userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserDTO userDTO) {
        if (userDTO == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserDO userDO = transferUtil.transeferToUserDO(userDTO);
        try {
            userDAO.insertSelective(userDO);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号已存在");
        }
        userDTO.setId(userDO.getId());
        UserPasswordDO userPasswordDO = transferUtil.transferToUserPasswordDO(userDTO);
        userPasswordDAO.insertSelective(userPasswordDO);
    }

    @Override
    public UserDTO validateLogin(String telphone, String encrptPassword) {
        // 通过用户手机获取用户信息
        UserDO userDO = userDAO.selectByTelphone(telphone);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDAO.selectByUserId(userDO.getId());
        UserDTO userDTO = transferUtil.transferToUserDTO(userDO, userPasswordDO);
        // 比对密码
        if (!com.alibaba.druid.util.StringUtils.equals(encrptPassword, userDTO.getEncrptPassword())) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userDTO;
    }
}
