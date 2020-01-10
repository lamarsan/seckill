package com.lamarsan.seckill.dao;

import com.lamarsan.seckill.entities.PromoDO;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoDAO {
    int deleteByPrimaryKey(Long id);

    int insert(PromoDO record);

    int insertSelective(PromoDO record);

    PromoDO selectByPrimaryKey(Long id);

    PromoDO selectByItemId(Long itemId);

    int updateByPrimaryKeySelective(PromoDO record);

    int updateByPrimaryKey(PromoDO record);
}