package com.lamarsan.seckill.dao;

import com.lamarsan.seckill.entities.OrderDO;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDAO {
    int deleteByPrimaryKey(String id);

    int insert(OrderDO record);

    int insertSelective(OrderDO record);

    OrderDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderDO record);

    int updateByPrimaryKey(OrderDO record);
}