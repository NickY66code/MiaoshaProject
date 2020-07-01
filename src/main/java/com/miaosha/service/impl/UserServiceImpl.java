package com.miaosha.service.impl;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dataobject.UserDO;
import com.miaosha.dataobject.UserPasswordDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DuplicateFormatFlagsException;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;


    @Override
    public UserModel GetUserById(Integer id) {
        //调用userDOMapper获取到对应的用户dataObject
        UserDO userDO=userDOMapper.selectByPrimaryKey(id);

        if(userDO==null){
            return null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        return converFromDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
//        if (userModel==null){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
//        if(StringUtils.isEmpty(userModel.getUsername())
//                ||userModel.getAge()==null
//                ||userModel.getGender()==null
//                ||StringUtils.isEmpty(userModel.getTelphone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }


        ValidationResult result= validator.validate(userModel);
        if (result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //实现model-->dataObject方法
        UserDO userDO=convertFromModel(userModel);

        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException((EmBusinessError.PARAMETER_VALIDATION_ERROR),"手机号已重复,请重新注册");
        }

        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO=convertPwdFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    @Override
    public UserModel validateLogin(String telphone, String password) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO=userDOMapper.selectByTelphone(telphone);
        if (userDO==null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel=converFromDataObject(userDO,userPasswordDO);
        //比对
        if(!StringUtils.equals(password,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDO convertPwdFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO=new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserDO convertFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserDO userDO=new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserModel converFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){

        if(userDO==null){
            return null;
        }
        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userDO,userModel);

        if (userPasswordDO!=null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
