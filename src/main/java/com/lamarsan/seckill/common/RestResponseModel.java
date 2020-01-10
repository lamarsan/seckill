package com.lamarsan.seckill.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * className: RestResponseModel
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/4 14:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestResponseModel {
    private String status;
    private Object data;

    public static RestResponseModel create(Object result) {
        return RestResponseModel.create(result, "success");
    }

    public static RestResponseModel create(Object result, String status) {
        RestResponseModel type = new RestResponseModel();
        type.setStatus(status);
        type.setData(result);
        return type;
    }
}
