package com.lamarsan.seckill.utils;

import com.lamarsan.seckill.dao.PromoDAO;
import com.lamarsan.seckill.dto.ItemDTO;
import com.lamarsan.seckill.dto.OrderDTO;
import com.lamarsan.seckill.dto.PromoDTO;
import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.entities.*;
import com.lamarsan.seckill.vo.ItemVO;
import com.lamarsan.seckill.vo.UserVO;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * className: TransferUtil
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/10 19:21
 */
@Component
public class TransferUtil {
    @Autowired
    PromoDAO promoDAO;

    public ItemDTO transferToItemDTO(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.copyProperties(itemStockDO, itemDTO);
        BeanUtils.copyProperties(itemDO, itemDTO);
        PromoDO promoDO = promoDAO.selectByItemId(itemDO.getId());
        PromoDTO promoDTO = transferToPromoDTO(promoDO);
        if (promoDTO != null && promoDTO.getStatus() != 3) {
            itemDTO.setPromoDTO(promoDTO);
        }
        return itemDTO;
    }

    public ItemDO transferToItemDO(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemDTO, itemDO);
        return itemDO;
    }

    public ItemStockDO transferToItemStockDO(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemDTO.getId());
        itemStockDO.setStock(itemDTO.getStock());
        return itemStockDO;
    }

    public PromoDTO transferToPromoDTO(PromoDO promoDO) {
        if (promoDO == null) {
            return null;
        }
        PromoDTO promoDTO = new PromoDTO();
        BeanUtils.copyProperties(promoDO, promoDTO);
        promoDTO.setStartDate(new DateTime(promoDO.getStartDate()));
        promoDTO.setEndDate(new DateTime(promoDO.getEndDate()));
        // 判断当前时间秒杀活动即将开始或正在进行
        if (promoDTO.getStartDate().isAfterNow()) {
            promoDTO.setStatus(1);
        } else if (promoDTO.getEndDate().isBeforeNow()) {
            promoDTO.setStatus(3);
        } else {
            promoDTO.setStatus(2);
        }
        return promoDTO;
    }

    public OrderDO transferToOrderDO(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderDTO, orderDO);
        return orderDO;
    }

    public UserPasswordDO transferToUserPasswordDO(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userDTO.getEncrptPassword());
        userPasswordDO.setUserId(userDTO.getId());
        return userPasswordDO;
    }

    public UserDO transeferToUserDO(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userDTO, userDO);
        return userDO;
    }

    public UserDTO transferToUserDTO(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDO, userDTO);
        if (userPasswordDO != null) {
            userDTO.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userDTO;
    }

    //---------------------------------------------------------------------------


    public ItemVO transferToItemVO(ItemDTO itemDTO) {
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

    public UserVO transferToUserVO(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        return userVO;
    }
}
