package com.miaosha.respone;

public class CommonReturnType {
    //表明对应表里请求的返回处理结果"success"或者"fail"
    private String Status;

    //若status=success，则data返回前端所需要的json格式
    //若status=fail，则data内使用通用的错误格式
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
