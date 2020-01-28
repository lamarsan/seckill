package com.lamarsan.seckill.service.impl;

import com.lamarsan.seckill.common.RedisConstants;
import com.lamarsan.seckill.dao.UserDAO;
import com.lamarsan.seckill.dao.UserPasswordDAO;
import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.em.BusinessErrorEnum;
import com.lamarsan.seckill.entities.UserDO;
import com.lamarsan.seckill.entities.UserPasswordDO;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.service.UserService;
import com.lamarsan.seckill.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
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
    private RedisUtil redisUtil;

    @Override
    public UserDTO getUserById(Long id) {
        UserDO userDO = userDAO.selectByPrimaryKey(id);
        UserPasswordDO userPasswordDO = userPasswordDAO.selectByUserId(id);
        return transferToUserDTO(userDO, userPasswordDO);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void register(UserDTO userDTO) {
        if (userDTO == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR);
        }
        UserDO userDO = transeferToUserDO(userDTO);
        try {
            userDAO.insertSelective(userDO);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "手机号已存在");
        }
        userDTO.setId(userDO.getId());
        UserPasswordDO userPasswordDO = transferToUserPasswordDO(userDTO);
        userPasswordDAO.insertSelective(userPasswordDO);
    }

    @Override
    public UserDTO validateLogin(String telphone, String encrptPassword) {
        // 通过用户手机获取用户信息
        UserDO userDO = userDAO.selectByTelphone(telphone);
        if (userDO == null) {
            throw new BusinessException(BusinessErrorEnum.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDAO.selectByUserId(userDO.getId());
        UserDTO userDTO = transferToUserDTO(userDO, userPasswordDO);
        // 比对密码
        if (!com.alibaba.druid.util.StringUtils.equals(encrptPassword, userDTO.getEncrptPassword())) {
            throw new BusinessException(BusinessErrorEnum.USER_LOGIN_FAIL);
        }
        return userDTO;
    }

    @Override
    public UserDTO getUserByIdInCache(Long id) {
        UserDTO userDTO = (UserDTO) redisUtil.get(RedisConstants.USER_DTO + id);
        if (userDTO == null) {
            userDTO = this.getUserById(id);
            redisUtil.set(RedisConstants.USER_DTO + id, userDTO, 600);
        }
        return userDTO;
    }

    private UserPasswordDO transferToUserPasswordDO(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userDTO.getEncrptPassword());
        userPasswordDO.setUserId(userDTO.getId());
        return userPasswordDO;
    }

    private UserDO transeferToUserDO(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userDTO, userDO);
        return userDO;
    }

    private UserDTO transferToUserDTO(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDO, userDTO);
        if (userPasswordDO != null) {
            userDTO.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userDTO;
    }
}
