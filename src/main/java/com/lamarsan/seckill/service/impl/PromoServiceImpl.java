package com.lamarsan.seckill.service.impl;

import com.lamarsan.seckill.dao.PromoDAO;
import com.lamarsan.seckill.dto.PromoDTO;
import com.lamarsan.seckill.entities.PromoDO;
import com.lamarsan.seckill.service.PromoService;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public PromoDTO getPromoByItemId(Long itemId) {
        PromoDO promoDO = promoDAO.selectByItemId(itemId);
        return transferToPromoDTO(promoDO);
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
            promoDTO.setStatus(1);
        } else if (promoDTO.getEndDate().isBeforeNow()) {
            promoDTO.setStatus(3);
        } else {
            promoDTO.setStatus(2);
        }
        return promoDTO;
    }
}
