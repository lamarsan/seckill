package com.lamarsan.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.lamarsan.seckill.common.RedisConstants;
import com.lamarsan.seckill.common.RestResponseModel;
import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.em.BusinessErrorEnum;
import com.lamarsan.seckill.form.OrderInsertForm;
import com.lamarsan.seckill.form.OrderTokenForm;
import com.lamarsan.seckill.mq.ProducerMQ;
import com.lamarsan.seckill.service.ItemService;
import com.lamarsan.seckill.service.OrderService;
import com.lamarsan.seckill.service.PromoService;
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
    @Autowired
    private PromoService promoService;

    @ApiOperation(value = "生成秒杀令牌")
    @PostMapping(value = "/generateToken")
    @ResponseBody
    public RestResponseModel generateToken(@RequestBody @Validated OrderTokenForm orderTokenForm) {
        // 根据token获取用户信息
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN);
        }
        UserDTO userDTO = (UserDTO) redisUtil.get(token);
        if (userDTO == null) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN);
        }
        // 如果promoId为null，说明不是活动商品，不产生令牌
        if (orderTokenForm.getPromoId() == null) {
            return RestResponseModel.create(null);
        }
        // 获取秒杀访问令牌
        String promoToken = promoService.generateSecondKillToken(orderTokenForm.getPromoId(), orderTokenForm.getItemId(), userDTO.getId());
        if (promoToken == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "生成令牌失败");
        }
        return RestResponseModel.create(promoToken);
    }


    @ApiOperation(value = "下单")
    @PostMapping(value = "/insert")
    @ResponseBody
    public RestResponseModel orderInsert(@RequestBody @Validated OrderInsertForm orderInsertForm) {
        // 校验用户状态
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN);
        }
        UserDTO userDTO = (UserDTO) redisUtil.get(token);
        if (userDTO == null) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN);
        }
        // 校验秒杀令牌是否正确
        if (orderInsertForm.getPromoId() != null) {
            String redisPromoToken = (String) redisUtil.get(RedisConstants.PROMO_TOKEN + orderInsertForm.getPromoId() + orderInsertForm.getItemId() + userDTO.getId());
            if (!org.apache.commons.lang3.StringUtils.equals(orderInsertForm.getPromoToken(), redisPromoToken)) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "秒杀令牌校验失败");
            }
        }
        // 判断库存是否已售罄，若存在直接失败
        if (redisUtil.hasKey(RedisConstants.STOCK_ZERO + orderInsertForm.getItemId())) {
            throw new BusinessException(BusinessErrorEnum.STOCK_NOT_ENOUGH);
        }
        // 加入库存流水init状态
        String stockLogId = itemService.initStockLog(orderInsertForm.getItemId(), orderInsertForm.getAmount());
        // 创建订单
        if (!producerMQ.transactionAsyncReduceStock(userDTO.getId(), orderInsertForm.getItemId(), orderInsertForm.getPromoId(), orderInsertForm.getAmount(), stockLogId)) {
            throw new BusinessException(BusinessErrorEnum.UNKNOWN_ERROR, "下单失败");
        }
        return RestResponseModel.create(null);
    }
}
