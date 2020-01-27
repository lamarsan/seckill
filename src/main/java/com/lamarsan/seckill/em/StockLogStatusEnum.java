package com.lamarsan.seckill.em;

/**
 * className: StockLogStatusEnum
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/27 19:36
 */
public enum StockLogStatusEnum {
    INIT_STATUS(1, "初始状态"),
    SUCCESS_STATUS(2, "库存扣减成功状态"),
    FAIL_STATUS(3, "下单回滚状态");

    private int statusCode;
    private String statusMsg;

    StockLogStatusEnum(int statusCode, String statusMsg) {
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
