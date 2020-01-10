package com.lamarsan.seckill.service.impl;

import com.lamarsan.seckill.dao.*;
import com.lamarsan.seckill.dto.ItemDTO;
import com.lamarsan.seckill.dto.OrderDTO;
import com.lamarsan.seckill.entities.*;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.error.EmBusinessError;
import com.lamarsan.seckill.service.OrderService;
import com.lamarsan.seckill.utils.TransferUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * className: OrderServiceImpl
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/9 18:50
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ItemStockDAO itemStockDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private SequenceDAO sequenceDAO;
    @Autowired
    private TransferUtil transferUtil;

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, Long itemId, Long promoId, Integer amount) {
        // 1、校验下单状态，下单的商品是否存在，用户是否合法，购买数量是否正确
        ItemDO itemDO = itemDAO.selectByPrimaryKey(itemId);
        ItemStockDO itemStockDO = itemStockDAO.selectByItemId(itemId);
        ItemDTO itemDTO = transferUtil.transferToItemDTO(itemDO, itemStockDO);
        if (itemDTO == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }
        UserDO userDO = userDAO.selectByPrimaryKey(userId);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }
        // 校验活动信息
        if (promoId != null) {
            if (!promoId.equals(itemDTO.getPromoDTO().getId())) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            } else if (itemDTO.getPromoDTO().getStatus() != 2) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动还未开始");
            }
        }
        // 2、落单减库存（落单时，直接锁住库存）   支付减库存（支付成功后再减库存）
        int affectedRow = itemStockDAO.decreaseStock(itemId, amount);
        if (affectedRow < 0) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        // 3、订单入库
        OrderDTO orderDTO = new OrderDTO(userId, itemId, itemDO.getPrice(), amount, itemDO.getPrice().multiply(new BigDecimal(amount)));
        if (promoId != null) {
            // 秒杀活动
            orderDTO.setPromoId(promoId);
            orderDTO.setItemPrice(itemDTO.getPromoDTO().getPromoItemPrice());
            orderDTO.setOrderPrice(itemDTO.getPromoDTO().getPromoItemPrice().multiply(new BigDecimal(amount)));
        }
        // 生成交易流水号，订单号
        orderDTO.setId(generateOrderNo());
        OrderDO orderDO = transferUtil.transferToOrderDO(orderDTO);
        orderDAO.insertSelective(orderDO);
        // 加上商品的销量
        itemDAO.increaseSales(itemId, amount);
        // 4、返回前端
        return orderDTO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo() {
        // 订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        // 前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        // 中间6位为自增序列
        long sequence = 0;
        SequenceDO sequenceDO = sequenceDAO.selectByPrimaryKey("order_info");
        sequence = sequenceDO.getCurrentValue();
        long newSequence = sequenceDO.getCurrentValue() + sequenceDO.getStep();
        // 如果超过6位 循环
        if (newSequence >= 1000000) {
            newSequence = newSequence - 1000000;
        }
        sequenceDO.setCurrentValue(newSequence);
        sequenceDAO.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        // 最后2位为分库分表位
        stringBuilder.append("00");
        return stringBuilder.toString();
    }
}
