package com.lamarsan.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.lamarsan.seckill.common.RestResponseModel;
import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.em.EmBusinessErrorEnum;
import com.lamarsan.seckill.form.OrderInsertForm;
import com.lamarsan.seckill.mq.ProducerMQ;
import com.lamarsan.seckill.service.ItemService;
import com.lamarsan.seckill.service.OrderService;
import com.lamarsan.seckill.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * className: OrderController
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 19:58
 */
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@Api(tags = "订单")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProducerMQ producerMQ;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "下单")
    @PostMapping(value = "/insert")
    @ResponseBody
    public RestResponseModel orderInsert(@RequestBody @Validated OrderInsertForm orderInsertForm) {
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(EmBusinessErrorEnum.USER_NOT_LOGIN);
        }
        UserDTO userDTO = (UserDTO) redisUtil.get(token);
        if (userDTO == null) {
            throw new BusinessException(EmBusinessErrorEnum.USER_NOT_LOGIN);
        }
        // 加入库存流水init状态
        String stockLogId = itemService.initStockLog(orderInsertForm.getItemId(), orderInsertForm.getAmount());
        // 创建订单
        if (!producerMQ.transactionAsyncReduceStock(userDTO.getId(), orderInsertForm.getItemId(), orderInsertForm.getPromoId(), orderInsertForm.getAmount(), stockLogId)) {
            throw new BusinessException(EmBusinessErrorEnum.UNKNOWN_ERROR, "下单失败");
        }
        return RestResponseModel.create(null);
    }
}
