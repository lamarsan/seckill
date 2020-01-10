package com.lamarsan.seckill.dao;

import com.lamarsan.seckill.entities.UserDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO {
    int deleteByPrimaryKey(Long id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Long id);

    UserDO selectByTelphone(String telphone);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}