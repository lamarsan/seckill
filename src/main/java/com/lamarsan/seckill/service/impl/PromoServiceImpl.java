package com.lamarsan.seckill.service.impl;

import com.lamarsan.seckill.dao.PromoDAO;
import com.lamarsan.seckill.dto.PromoDTO;
import com.lamarsan.seckill.entities.PromoDO;
import com.lamarsan.seckill.service.PromoService;
import com.lamarsan.seckill.utils.TransferUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    TransferUtil transferUtil;

    @Override
    public PromoDTO getPromoByItemId(Long itemId) {
        PromoDO promoDO = promoDAO.selectByItemId(itemId);
        return transferUtil.transferToPromoDTO(promoDO);
    }
}
