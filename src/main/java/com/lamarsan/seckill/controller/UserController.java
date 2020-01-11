package com.lamarsan.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.lamarsan.seckill.common.RestResponseModel;
import com.lamarsan.seckill.dto.UserDTO;
import com.lamarsan.seckill.form.UserLoginForm;
import com.lamarsan.seckill.form.UserRigisterForm;
import com.lamarsan.seckill.error.BusinessException;
import com.lamarsan.seckill.error.EmBusinessError;
import com.lamarsan.seckill.service.UserService;
import com.lamarsan.seckill.utils.MD5Util;
import com.lamarsan.seckill.utils.RedisUtil;
import com.lamarsan.seckill.utils.TransferUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Random;

import static com.lamarsan.seckill.common.CommonConstants.CONTENT_TYPE_FORMED;

/**
 * className: UserController
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/3 22:16
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@Api(tags = "用户")
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private TransferUtil transferUtil;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @ResponseBody
    public RestResponseModel login(@RequestBody @Validated UserLoginForm userLoginForm) {
        // 用户登录
        UserDTO userDTO = userService.validateLogin(userLoginForm.getTelphone(), MD5Util.EncodeByMd5(userLoginForm.getEncrptPassword()));
        // 将登录凭证加入到session
        redisUtil.set("IS_LOGIN", true, 300);
        redisUtil.set("LOGIN_USER", userDTO, 300);
        return RestResponseModel.create(null);
    }

    @ApiOperation(value = "注册")
    @PostMapping(value = "/register")
    @ResponseBody
    public RestResponseModel register(@RequestBody @Validated UserRigisterForm userRigisterForm) {
        // 验证手机号和对应的otpcode相符合
        String inSessionOtpCode = (String) redisUtil.get(userRigisterForm.getTelphone());
        if (!StringUtils.equals(userRigisterForm.getOtpCode(), inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }
        // 用户注册流程
        userRigisterForm.setRegisterMode("byphone");
        userRigisterForm.setEncrptPassword(MD5Util.EncodeByMd5(userRigisterForm.getEncrptPassword()));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userRigisterForm, userDTO);
        userService.register(userDTO);
        return RestResponseModel.create(null);
    }

    @ApiOperation(value = "生成验证码")
    @PostMapping(value = "/getotp", consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public RestResponseModel getOtp(@RequestParam(name = "telphone") String telphone) {
        // 按照一定规则生成验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        // 将验证码和用户手机号关联,使用httpSession绑定
        redisUtil.set(telphone, otpCode);
        // 将OTP验证码通过短信发送给用户
        System.out.println("telphone=" + telphone + "&optCode=" + otpCode);
        return RestResponseModel.create(null);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/get")
    @ResponseBody
    public RestResponseModel getUser(@RequestParam(name = "id") Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        return RestResponseModel.create(transferUtil.transferToUserVO(userDTO));
    }
}
