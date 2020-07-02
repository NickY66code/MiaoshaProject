package com.miaosha.controller;


import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.respone.CommonReturnType;
import com.miaosha.service.OrderService;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class OrderController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    @RequestMapping(value = "/createOrder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "amount")Integer amount) throws BusinessException {

        Boolean isLogin= (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
       // System.out.println(isLogin.booleanValue());
        if (isLogin==null||!isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户未登录，不能下单");
        }
        //获取用户的登录信息
        UserModel userModel =(UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel=orderService.createOrder(userModel.getId(),itemId,amount);

         return CommonReturnType.create(orderModel);
    }
}
