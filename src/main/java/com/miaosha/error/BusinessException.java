package com.miaosha.error;

//包装器业务异常类的实现
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    //直接接受EmBusiness的传参用于构造业务异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }

    //接受自定义的errMsg的方式的构造业务异常
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError =commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
