package com.lamarsan.seckill.error;

import com.lamarsan.seckill.common.RestResponseModel;
import com.lamarsan.seckill.em.BusinessErrorEnum;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * className: GlobalExceptionHandler
 * description: 全局异常处理
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/4 14:49
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String, Object> responseData = new HashMap<>(16);
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            responseData.put("errCode", BusinessErrorEnum.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", BusinessErrorEnum.UNKNOWN_ERROR.getErrMsg());
        }
        return RestResponseModel.create(responseData, "fail");
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            BindException.class})
    @ResponseBody
    public RestResponseModel handleValidationException(Exception e) {
        String msg = "";
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException t = (MethodArgumentNotValidException) e;
            msg = t.getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(","));
        } else if (e instanceof BindException) {
            BindException t = (BindException) e;
            msg = t.getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(","));
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException t = (ConstraintViolationException) e;
            msg = t.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(","));
        } else if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException t = (MissingServletRequestParameterException) e;
            msg = t.getParameterName() + " 不能为空";
        } else if (e instanceof MissingPathVariableException) {
            MissingPathVariableException t = (MissingPathVariableException) e;
            msg = t.getVariableName() + " 不能为空";
        } else {
            msg = "必填参数缺失";
        }
        return RestResponseModel.create(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, msg);
    }
}
