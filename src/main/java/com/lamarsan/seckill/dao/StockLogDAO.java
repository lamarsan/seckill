package com.lamarsan.seckill.dao;

import com.lamarsan.seckill.entities.StockLogDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StockLogDAO {
    int deleteByPrimaryKey(String stockLogId);

    int insert(StockLogDO record);

    int insertSelective(StockLogDO record);

    StockLogDO selectByPrimaryKey(String stockLogId);

    int updateByPrimaryKeySelective(StockLogDO record);

    int updateByPrimaryKey(StockLogDO record);
}