package com.lamarsan.seckill.service.impl;

import com.lamarsan.seckill.dao.ItemDAO;
import com.lamarsan.seckill.dao.ItemStockDAO;
import com.lamarsan.seckill.dao.PromoDAO;
import com.lamarsan.seckill.dto.ItemDTO;
import com.lamarsan.seckill.dto.PromoDTO;
import com.lamarsan.seckill.entities.ItemDO;
import com.lamarsan.seckill.entities.ItemStockDO;
import com.lamarsan.seckill.entities.PromoDO;
import com.lamarsan.seckill.service.ItemService;
import com.lamarsan.seckill.utils.TransferUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * className: ItemServiceImpl
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 15:32
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private ItemStockDAO itemStockDAO;
    @Autowired
    private TransferUtil transferUtil;

    @Override
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO) {
        ItemDO itemDO = transferUtil.transferToItemDO(itemDTO);
        itemDAO.insertSelective(itemDO);
        itemDTO.setId(itemDO.getId());
        ItemStockDO itemStockDO = transferUtil.transferToItemStockDO(itemDTO);
        itemStockDAO.insertSelective(itemStockDO);
        return getItemById(itemDTO.getId());
    }

    @Override
    public List<ItemDTO> listItem() {
        List<ItemDO> itemDOList = itemDAO.listItem();
        List<Long> itemIds = itemDOList.stream().map(ItemDO::getId).collect(Collectors.toList());
        List<ItemStockDO> itemStockDOS = itemStockDAO.selectByItemIds(itemIds);
        // item_id=>itemStock
        Map<Long, ItemStockDO> itemStockMap = itemStockDOS.stream().collect(Collectors.toMap(ItemStockDO::getItemId, itemStockDO -> itemStockDO));
        return itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockMap.get(itemDO.getId());
            return transferUtil.transferToItemDTO(itemDO, itemStockDO);
        }).collect(Collectors.toList());
    }

    @Override
    public ItemDTO getItemById(Long id) {
        ItemDO itemDO = itemDAO.selectByPrimaryKey(id);
        if (itemDO == null) {
            return null;
        }
        // 获取库存数量
        ItemStockDO itemStockDO = itemStockDAO.selectByItemId(id);
        return transferUtil.transferToItemDTO(itemDO, itemStockDO);
    }
}
