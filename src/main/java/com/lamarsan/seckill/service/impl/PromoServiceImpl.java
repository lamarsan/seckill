package com.lamarsan.seckill.service.impl;

import com.lamarsan.seckill.common.RedisConstants;
import com.lamarsan.seckill.dao.PromoDAO;
import com.lamarsan.seckill.dto.ItemDTO;
import com.lamarsan.seckill.dto.PromoDTO;
import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.em.PromoStatusEnum;
import com.lamarsan.seckill.entities.PromoDO;
import com.lamarsan.seckill.service.ItemService;
import com.lamarsan.seckill.service.PromoService;
import com.lamarsan.seckill.service.UserService;
import com.lamarsan.seckill.utils.RedisUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * className: PromoServiceImpl
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/10 19:02
 */
@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    PromoDAO promoDAO;
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public PromoDTO getPromoByItemId(Long itemId) {
        PromoDO promoDO = promoDAO.selectByItemId(itemId);
        return transferToPromoDTO(promoDO);
    }

    @Override
    public void publishPromo(Long promoId) {
        PromoDO promoDO = promoDAO.selectByPrimaryKey(promoId);
        if (promoDO.getItemId() == null || promoDO.getItemId() == 0) {
            return;
        }
        ItemDTO itemDTO = itemService.getItemById(promoDO.getItemId());
        // 将库存同步到redis
        redisUtil.set(RedisConstants.STOCK_NUM + itemDTO.getId(), itemDTO.getStock());
    }

    @Override
    public String generateSecondKillToken(Long promoId, Long itemId, Long userId) {
        PromoDO promoDO = promoDAO.selectByPrimaryKey(promoId);
        PromoDTO promoDTO = transferToPromoDTO(promoDO);
        if (promoDTO == null) {
            return null;
        }
        // 不是正在进行就不生成
        if (promoDTO.getStatus() != PromoStatusEnum.ONGOING.getStatusCode()) {
            return null;
        }
        // 校验商品和用户
        ItemDTO itemDTO = itemService.getItemByIdInCache(itemId);
        if (itemDTO == null) {
            return null;
        }
        UserDTO userDTO = userService.getUserByIdInCache(userId);
        if (userDTO == null) {
            return null;
        }
        // 生成token，并在redis存入5分钟
        String token = UUID.randomUUID().toString().replace("-", "");
        redisUtil.set(RedisConstants.PROMO_TOKEN + promoId + itemId + userId, token, 300);
        return token;
    }

    private PromoDTO transferToPromoDTO(PromoDO promoDO) {
        if (promoDO == null) {
            return null;
        }
        PromoDTO promoDTO = new PromoDTO();
        BeanUtils.copyProperties(promoDO, promoDTO);
        promoDTO.setStartDate(new DateTime(promoDO.getStartDate()));
        promoDTO.setEndDate(new DateTime(promoDO.getEndDate()));
        // 判断当前时间秒杀活动即将开始或正在进行
        if (promoDTO.getStartDate().isAfterNow()) {
            promoDTO.setStatus(PromoStatusEnum.NOT_BEGIN.getStatusCode());
        } else if (promoDTO.getEndDate().isBeforeNow()) {
            promoDTO.setStatus(PromoStatusEnum.FINISH.getStatusCode());
        } else {
            promoDTO.setStatus(PromoStatusEnum.ONGOING.getStatusCode());
        }
        return promoDTO;
    }
}
