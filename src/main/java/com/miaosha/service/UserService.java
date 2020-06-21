package com.miaosha.service;

import com.miaosha.service.model.UserModel;

public interface UserService {

    //通过用户Id获取用户对象的方法
    UserModel GetUserById(Integer id);
}
