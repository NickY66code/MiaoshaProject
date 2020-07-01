package com.miaosha.service.impl;

import com.miaosha.dao.ItemDOMapper;
import com.miaosha.dao.ItemStockDOMapper;
import com.miaosha.dataobject.ItemDO;
import com.miaosha.dataobject.ItemStockDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemDO itemDO=new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }

    private ItemStockDO convertItemStockModelFromItemModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemStockDO itemStockDO=new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    @Override
    @Transactional
    public ItemModel CreateItem(ItemModel itemModel) throws BusinessException {

        ValidationResult result= validator.validate(itemModel);
        //校验入参
        if(result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        //转换ItemModel—>DataObject
        ItemDO itemDO=convertItemDOFromItemModel(itemModel);

        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());

        ItemStockDO itemStockDO=this.convertItemStockModelFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        //返回创建完成的对象

        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> ListItem() {
        List<ItemDO> itemDOList=itemDOMapper.listItem();
        List<ItemModel> itemModelList=itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO =itemStockDOMapper.selectByPrimaryKey(itemDO.getId());
            ItemModel itemModel=this.convertModelFromDataObject(itemDO,itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {

        ItemDO itemDO =itemDOMapper.selectByPrimaryKey(id);
        if (itemDO==null){
            return  null;
        }
        //操作获得库存数量
        ItemStockDO itemStockDO=itemStockDOMapper.selectByItemId(itemDO.getId());

        //将dataobject—>mdoel
        ItemModel itemModel=convertModelFromDataObject(itemDO,itemStockDO);

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int affectedRow =itemStockDOMapper.decreaseStock(itemId,amount);
        if (affectedRow>0){
            //更新库存成功
            return true;
        }else {
            //更新库存失败
            return false;
        }

    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel =new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
