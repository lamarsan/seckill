package com.lamarsan.seckill.dao;

import com.lamarsan.seckill.entities.SequenceDO;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceDAO {
    int deleteByPrimaryKey(String name);

    int insert(SequenceDO record);

    int insertSelective(SequenceDO record);

    SequenceDO selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(SequenceDO record);

    int updateByPrimaryKey(SequenceDO record);
}