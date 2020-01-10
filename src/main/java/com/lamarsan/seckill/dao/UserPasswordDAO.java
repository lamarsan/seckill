package com.lamarsan.seckill.dao;

import com.lamarsan.seckill.entities.UserPasswordDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordDAO {
    int deleteByPrimaryKey(Long id);

    int insert(UserPasswordDO record);

    int insertSelective(UserPasswordDO record);

    UserPasswordDO selectByPrimaryKey(Long id);

    UserPasswordDO selectByUserId(Long id);

    int updateByPrimaryKeySelective(UserPasswordDO record);

    int updateByPrimaryKey(UserPasswordDO record);
}
