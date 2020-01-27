package com.lamarsan.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.lamarsan.seckill.common.RedisConstants;
import com.lamarsan.seckill.dao.ItemStockDAO;
import com.lamarsan.seckill.utils.RedisUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * className: ConsumerMQ
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/21 21:34
 */
@Component
public class ConsumerMQ {
    @Autowired
    private ItemStockDAO itemStockDAO;

    private DefaultMQPushConsumer consumer;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${mq.nameserver.addr}")
    private String nameAddr;

    @Value(("${mq.topicname}"))
    private String topicName;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer("stock_consumer_group");
        consumer.setNamesrvAddr(nameAddr);
        consumer.subscribe(topicName, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            Message message = list.get(0);
            String jsonString = new String(message.getBody());
            Map<String, Object> map = JSON.parseObject(jsonString, Map.class);
            Long itemId = Long.valueOf((String) map.get("itemId"));
            Integer amount = Integer.valueOf((String) map.get("amount"));
            itemStockDAO.decreaseStock(itemId, amount);
            // 库存发生变化，需要删除缓存
            redisUtil.del(RedisConstants.ITEM_DTO + itemId);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}
