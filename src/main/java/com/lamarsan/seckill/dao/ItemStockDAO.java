package com.lamarsan.seckill.dao;

import com.lamarsan.seckill.entities.ItemStockDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemStockDAO {
    int deleteByPrimaryKey(Long id);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Long id);

    ItemStockDO selectByItemId(Long itemId);

    List<ItemStockDO> selectByItemIds(List<Long> itemIds);

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);

    int decreaseStock(@Param("itemId") Long itemId, @Param("amount") Integer amount);
}