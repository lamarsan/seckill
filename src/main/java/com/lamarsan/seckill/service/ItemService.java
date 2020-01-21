package com.lamarsan.seckill.service;

import com.lamarsan.seckill.dto.ItemDTO;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * className: ItemService
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 15:30
 */
public interface ItemService {
    /**
     * 创建商品
     */
    ItemDTO createItem(ItemDTO itemDTO);

    /**
     * 商品列表浏览
     */
    List<ItemDTO> listItem();

    /**
     * 商品详情浏览
     */
    ItemDTO getItemById(Long id);

    /**
     * item及promo缓存模型
     */
    ItemDTO getItemByIdInCache(Long id);

    boolean decreaseStock(Long itemId, Integer amount);
}
