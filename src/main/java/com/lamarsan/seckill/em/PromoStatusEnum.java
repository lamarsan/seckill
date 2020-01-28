package com.lamarsan.seckill.em;

/**
 * className: PromoStatusEnum
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/28 19:52
 */
public enum PromoStatusEnum {
    NOT_BEGIN(1, "活动未开始"),
    ONGOING(2, "活动进行中"),
    FINISH(3, "活动已结束");

    private int statusCode;
    private String statusMsg;

    PromoStatusEnum(int statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
