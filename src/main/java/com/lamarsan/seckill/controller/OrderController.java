package com.lamarsan.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.lamarsan.seckill.common.RestResponseModel;
import com.lamarsan.seckill.dto.ItemDTO;
import com.lamarsan.seckill.dto.OrderDTO;
import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.entities.UserDO;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.error.EmBusinessError;
import com.lamarsan.seckill.form.ItemInsertForm;
import com.lamarsan.seckill.form.OrderInsertForm;
import com.lamarsan.seckill.service.OrderService;
import com.lamarsan.seckill.utils.RedisUtil;
import com.lamarsan.seckill.vo.ItemVO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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
    private HttpServletRequest httpServletRequest;

    @ApiOperation(value = "下单")
    @PostMapping(value = "/insert")
    @ResponseBody
    public RestResponseModel orderInsert(@RequestBody @Validated OrderInsertForm orderInsertForm) {
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserDTO userDTO = (UserDTO) redisUtil.get(token);
        if (userDTO == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        OrderDTO orderDTO = orderService.createOrder(userDTO.getId(), orderInsertForm.getItemId(), orderInsertForm.getPromoId(), orderInsertForm.getAmount());
        return RestResponseModel.create(orderDTO);
    }
}
