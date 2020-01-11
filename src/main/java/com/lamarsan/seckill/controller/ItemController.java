package com.lamarsan.seckill.controller;

import com.lamarsan.seckill.common.RestResponseModel;
import com.lamarsan.seckill.dto.ItemDTO;
import com.lamarsan.seckill.form.ItemInsertForm;
import com.lamarsan.seckill.service.ItemService;
import com.lamarsan.seckill.utils.TransferUtil;
import com.lamarsan.seckill.vo.ItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    ItemService itemService;
    @Autowired
    TransferUtil transferUtil;

    @ApiOperation(value = "新增商品")
    @PostMapping(value = "/insert")
    @ResponseBody
    public RestResponseModel itemInsert(@RequestBody @Validated ItemInsertForm itemInsertForm) {
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.copyProperties(itemInsertForm, itemDTO);
        ItemDTO resultDTO = itemService.createItem(itemDTO);
        ItemVO itemVO = transferUtil.transferToItemVO(resultDTO);
        return RestResponseModel.create(itemVO);
    }

    @ApiOperation(value = "获得商品")
    @GetMapping(value = "/get")
    @ResponseBody
    public RestResponseModel getItem(@RequestParam(name = "id") Long id) {
        ItemDTO itemDTO = itemService.getItemById(id);
        ItemVO itemVO = transferUtil.transferToItemVO(itemDTO);
        return RestResponseModel.create(itemVO);
    }

    @ApiOperation(value = "商品列表")
    @GetMapping(value = "/list")
    @ResponseBody
    public RestResponseModel listItem() {
        List<ItemDTO> itemDTOList =  itemService.listItem();
        List<ItemVO> itemVOList = itemDTOList.stream().map(transferUtil::transferToItemVO).collect(Collectors.toList());
        return RestResponseModel.create(itemVOList);
    }
}
