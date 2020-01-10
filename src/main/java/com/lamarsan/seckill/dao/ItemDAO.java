package com.lamarsan.seckill.dao;

import com.lamarsan.seckill.entities.ItemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDAO {
    int deleteByPrimaryKey(Long id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Long id);

    List<ItemDO> listItem();

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);

    int increaseSales(@Param("id") Long id, @Param("amount") Integer amount);
}