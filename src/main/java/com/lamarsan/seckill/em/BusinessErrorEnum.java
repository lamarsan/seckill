package com.lamarsan.seckill.em;

import com.lamarsan.seckill.error.CommonError;

/**
 * className: BusinessErrorEnum
 * description: 异常枚举
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/4 14:37
 */
public enum BusinessErrorEnum implements CommonError {
    // 通用错误类型10000
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKNOWN_ERROR(10002, "未知错误"),

    // 20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002, "用户手机号或密码不正确"),
    USER_NOT_LOGIN(20003, "用户还未登录"),

    // 30000开头为交易信息错误
    STOCK_NOT_ENOUGH(30001, "库存不足"),
    MQ_SEND_FAIL(30002, "库存异步消息失败"),
    RATE_LIMIT(30003, "活动太火爆，请稍后再试");

    private int errCode;
    private String errMsg;

    BusinessErrorEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
