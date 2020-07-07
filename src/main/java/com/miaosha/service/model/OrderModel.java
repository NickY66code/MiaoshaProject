package com.miaosha.service.model;

import java.math.BigDecimal;

//用户下单的交易模型
public class OrderModel {

    //2020051000456
    private String id;

    //购买的用户id
    private Integer userId;

    //购买的商品id
    private Integer itemId;

    //购买数量
    private Integer amount;

    //若非空则表示已秒杀商品方式下单
    private Integer promoId;

    //购买商品单价,若promoId非空则表示秒杀价格
    private BigDecimal itemPrice;

    //购买金额，,若promoId非空则表示秒杀价格
    private BigDecimal orderPrice;

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }
    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
