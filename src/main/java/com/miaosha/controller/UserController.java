package com.miaosha.controller;

import com.miaosha.controller.viewobject.UserVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.respone.CommonReturnType;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;


@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class UserController extends BaseController{

    @Autowired
    UserService userService;

    @Autowired
    HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                      @RequestParam(name ="password")String password) throws BusinessException {
        //检验是否为空
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)|| StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，校验用户登录是否合法
        UserModel userModel=userService.validateLogin(telphone,password);

        //将登陆凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }


    //用户注册接口
    @RequestMapping(value = "/register",method ={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name ="telphone")String telphone,
                                     @RequestParam(name ="otpCode")String otpCode,
                                     @RequestParam(name ="username")String username,
                                     @RequestParam(name ="age")Integer age,
                                     @RequestParam(name ="gender")Integer gender,
                                     @RequestParam(name = "password")String password) throws BusinessException {
        //验证手机号与对应的otpCode是否对应
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不正确");
        }
        //用户的注册流程
        UserModel userModel=new UserModel();
        userModel.setGender(gender);
        userModel.setTelphone(telphone);
        userModel.setUsername(username);
        userModel.setAge(age);
        userModel.setRegisterMode("ByPhone");
        userModel.setEncrptPassword(password);

        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    //用户获取OTP短信接口
    @RequestMapping(value = "/getotp",method ={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name ="telphone")String telphone){
        //需要按照一定的规则生成opt验证码
        Random random =new Random();
        int randomInt= random.nextInt(99999);
        randomInt+=10000;
        String otpCode=String.valueOf(randomInt);

        //将opt验证码与用户的手机号进行关联,使用HttpSession的方式绑定他的手机号与OtpCode
        httpServletRequest.getSession().setAttribute(telphone,otpCode);


        //将opt验证码通过手机短信通道发送给用户 /省略
        System.out.println("telPhone :"+telphone+"&optCode"+otpCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
        //调用service服务对象获取对应id的用户对象并返回给前端
        UserModel userModel=userService.GetUserById(id);

        //若获取的用户信息不存在
        if(userModel==null){
            //userModel.setEncrptPassword("123");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户对象转化为可供UI使用的ViewObject
        UserVO userVO=conventFromModel(userModel);

        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO conventFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }

        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }
}
