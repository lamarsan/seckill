package com.lamarsan.seckill.error;

/**
 * className: BusinessException
 * description: 包装器业务异常类实现
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/4 14:41
 */
public class BusinessException extends RuntimeException implements CommonError {
    private CommonError commonError;

    /**
     * 直接接受EmBusinessError的传参用于构造业务异常
     */
    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    public BusinessException(CommonError commonError, String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
