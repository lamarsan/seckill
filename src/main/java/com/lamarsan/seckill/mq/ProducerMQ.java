package com.lamarsan.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.lamarsan.seckill.dao.StockLogDAO;
import com.lamarsan.seckill.em.StockLogStatusEnum;
import com.lamarsan.seckill.entities.StockLogDO;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.service.OrderService;
import com.lamarsan.seckill.utils.RedisUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * className: ProducerMQ
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/21 21:23
 */
@Component
public class ProducerMQ {
    private DefaultMQProducer producer;

    private TransactionMQProducer transactionMQProducer;

    @Autowired
    OrderService orderService;
    @Autowired
    StockLogDAO stockLogDAO;

    @Value("${mq.nameserver.addr}")
    private String nameAddr;

    @Value(("${mq.topicname}"))
    private String topicName;

    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer("producer_group");
        producer.setNamesrvAddr(nameAddr);
        producer.start();
        transactionMQProducer = new TransactionMQProducer("transaction_producer_group");
        transactionMQProducer.setNamesrvAddr(nameAddr);
        transactionMQProducer.start();
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                // 创建订单
                Long itemId = Long.valueOf((String) ((Map) arg).get("itemId"));
                Long promoId = null;
                if (((Map) arg).get("promoId") != null) {
                    promoId = Long.valueOf((String) ((Map) arg).get("promoId"));
                }
                Long userId = Long.valueOf((String) ((Map) arg).get("userId"));
                Integer amount = Integer.valueOf((String) ((Map) arg).get("amount"));
                String stockLogId = (String) ((Map) arg).get("stockLogId");
                try {
                    orderService.createOrder(userId, itemId, promoId, amount, stockLogId);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    // 设置stockLog为回滚状态
                    StockLogDO stockLogDO = stockLogDAO.selectByPrimaryKey(stockLogId);
                    if (stockLogDO != null) {
                        stockLogDO.setStatus(StockLogStatusEnum.FAIL_STATUS.getStatusCode());
                        stockLogDAO.updateByPrimaryKeySelective(stockLogDO);
                    }
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                // createOrder花费了很长时间，事务处于UNKNOWN状态
                // 根据是否扣减库存成功，来判断要返回COMMIT，ROLLBACK还是继续UNKNOWN
                String jsonString = new String(msg.getBody());
                Map<String, Object> map = JSON.parseObject(jsonString, Map.class);
                Long itemId = Long.valueOf((String) map.get("itemId"));
                Integer amount = Integer.valueOf((String) map.get("amount"));
                String stockLogId = (String) map.get("stockLogId");
                StockLogDO stockLogDO = stockLogDAO.selectByPrimaryKey(stockLogId);
                if (stockLogDO == null) {
                    return LocalTransactionState.UNKNOW;
                }
                if (StockLogStatusEnum.SUCCESS_STATUS.getStatusCode() == stockLogDO.getStatus()) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if (StockLogStatusEnum.INIT_STATUS.getStatusCode() == stockLogDO.getStatus()) {
                    return LocalTransactionState.UNKNOW;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });
    }

    /**
     * 事务型
     * 投递出去状态为prepare，会进入executeLocalTransaction
     */
    public boolean transactionAsyncReduceStock(Long userId, Long itemId, Long promoId, Integer amount, String stockLogId) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("itemId", itemId.toString());
        bodyMap.put("amount", amount.toString());
        bodyMap.put("stockLogId", stockLogId);
        Map<String, Object> argsMap = new HashMap<>();
        argsMap.put("itemId", itemId.toString());
        argsMap.put("amount", amount.toString());
        argsMap.put("userId", userId.toString());
        if (promoId == null) {
            argsMap.put("promoId", null);
        } else {
            argsMap.put("promoId", promoId.toString());
        }
        argsMap.put("stockLogId", stockLogId);
        Message message = new Message(topicName, "increase", JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));
        TransactionSendResult sendResult = null;
        try {
            sendResult = transactionMQProducer.sendMessageInTransaction(message, argsMap);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        }
        if (sendResult.getLocalTransactionState() == LocalTransactionState.ROLLBACK_MESSAGE) {
            return false;
        } else if (sendResult.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE) {
            return true;
        } else {
            return true;
        }
    }

    /**
     * 非事务型
     */
    public boolean asyncReduceStock(Long itemId, Integer amount) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("itemId", itemId.toString());
        bodyMap.put("amount", amount.toString());
        Message message = new Message(topicName, "increase", JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));
        try {
            producer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
