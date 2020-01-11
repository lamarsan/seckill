package com.lamarsan.seckill.error;

/**
 * className: CommonError
 * description: Error接口
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/4 14:35
 */
public interface CommonError {
    int getErrCode();

    String getErrMsg();

    CommonError setErrMsg(String errMsg);
}
