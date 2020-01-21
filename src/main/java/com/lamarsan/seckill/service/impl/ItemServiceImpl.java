package com.lamarsan.seckill.service.impl;

import com.lamarsan.seckill.dao.ItemDAO;
import com.lamarsan.seckill.dao.ItemStockDAO;
import com.lamarsan.seckill.dao.PromoDAO;
import com.lamarsan.seckill.dto.ItemDTO;
import com.lamarsan.seckill.dto.PromoDTO;
import com.lamarsan.seckill.entities.ItemDO;
import com.lamarsan.seckill.entities.ItemStockDO;
import com.lamarsan.seckill.service.ItemService;
import com.lamarsan.seckill.service.PromoService;
import com.lamarsan.seckill.utils.RedisUtil;
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
    PromoDAO promoDAO;
    @Autowired
    private ItemStockDAO itemStockDAO;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO) {
        ItemDO itemDO = transferToItemDO(itemDTO);
        itemDAO.insertSelective(itemDO);
        itemDTO.setId(itemDO.getId());
        ItemStockDO itemStockDO = transferToItemStockDO(itemDTO);
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
            return transferToItemDTO(itemDO, itemStockDO);
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
        return transferToItemDTO(itemDO, itemStockDO);
    }

    @Override
    public ItemDTO getItemByIdInCache(Long id) {
        ItemDTO itemDTO = (ItemDTO) redisUtil.get("item_validate_" + id);
        if (itemDTO == null) {
            itemDTO = this.getItemById(id);
            redisUtil.set("item_validate_" + id, itemDTO, 600);
        }
        return itemDTO;
    }

    private ItemDTO transferToItemDTO(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.copyProperties(itemStockDO, itemDTO);
        BeanUtils.copyProperties(itemDO, itemDTO);
        PromoDTO promoDTO = promoService.getPromoByItemId(itemDO.getId());
        if (promoDTO != null && promoDTO.getStatus() != 3) {
            itemDTO.setPromoDTO(promoDTO);
        }
        return itemDTO;
    }

    private ItemDO transferToItemDO(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemDTO, itemDO);
        return itemDO;
    }

    private ItemStockDO transferToItemStockDO(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemDTO.getId());
        itemStockDO.setStock(itemDTO.getStock());
        return itemStockDO;
    }


}
