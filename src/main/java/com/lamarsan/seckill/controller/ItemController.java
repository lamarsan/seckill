package com.lamarsan.seckill.controller;

import com.lamarsan.seckill.common.RedisConstants;
import com.lamarsan.seckill.common.RestResponseModel;
import com.lamarsan.seckill.dto.ItemDTO;
import com.lamarsan.seckill.form.ItemInsertForm;
import com.lamarsan.seckill.service.ItemService;
import com.lamarsan.seckill.service.PromoService;
import com.lamarsan.seckill.utils.RedisUtil;
import com.lamarsan.seckill.vo.ItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * className: ItemController
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 15:51
 */
@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@Api(tags = "商品")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PromoService promoService;

    @ApiOperation(value = "新增商品")
    @PostMapping(value = "/insert")
    @ResponseBody
    public RestResponseModel itemInsert(@RequestBody @Validated ItemInsertForm itemInsertForm) {
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.copyProperties(itemInsertForm, itemDTO);
        ItemDTO resultDTO = itemService.createItem(itemDTO);
        ItemVO itemVO = transferToItemVO(resultDTO);
        return RestResponseModel.create(itemVO);
    }

    @ApiOperation(value = "获得商品")
    @GetMapping(value = "/get")
    @ResponseBody
    public RestResponseModel getItem(@RequestParam(name = "id") Long id) {
        ItemDTO itemDTO = (ItemDTO) redisUtil.get(RedisConstants.ITEM_DTO + id);
        if (itemDTO == null) {
            itemDTO = itemService.getItemById(id);
            redisUtil.set(RedisConstants.ITEM_DTO + id, itemDTO, 600);
        }
        ItemVO itemVO = transferToItemVO(itemDTO);
        return RestResponseModel.create(itemVO);
    }

    @ApiOperation(value = "商品列表")
    @GetMapping(value = "/list")
    @ResponseBody
    public RestResponseModel listItem() {
        List<ItemDTO> itemDTOList = itemService.listItem();
        List<ItemVO> itemVOList = itemDTOList.stream().map(this::transferToItemVO).collect(Collectors.toList());
        return RestResponseModel.create(itemVOList);
    }

    @ApiOperation(value = "发布活动")
    @GetMapping(value = "/publishPromo")
    @ResponseBody
    public RestResponseModel publishPromo(@RequestParam(name = "id") Long id) {
        promoService.publishPromo(id);
        return RestResponseModel.create(null);
    }

    private ItemVO transferToItemVO(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemDTO, itemVO);
        if (itemDTO.getPromoDTO() != null) {
            if (itemDTO.getPromoDTO().getStatus() != 3) {
                // 有正在进行的秒杀活动
                itemVO.setPromoStatus(itemDTO.getPromoDTO().getStatus());
                itemVO.setPromoId(itemDTO.getPromoDTO().getId());
                itemVO.setStartDate(itemDTO.getPromoDTO().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
                itemVO.setPromoPrice(itemDTO.getPromoDTO().getPromoItemPrice());
            } else {
                // 已结束
                itemVO.setPromoStatus(0);
            }
        } else {
            // 没有秒杀活动
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
