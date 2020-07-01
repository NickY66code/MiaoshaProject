package com.miaosha.controller;


import com.miaosha.controller.viewobject.ItemVO;
import com.miaosha.error.BusinessException;
import com.miaosha.respone.CommonReturnType;
import com.miaosha.service.ItemService;
import com.miaosha.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/item")
@RequestMapping("/item")
//@CrossOrigin(origins = {"*"},allowedHeaders = "true")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    //创建商品
    @RequestMapping(value = "/create", method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name ="title")String title,
                                       @RequestParam(name ="description")String description,
                                       @RequestParam(name ="price")BigDecimal price,
                                       @RequestParam(name ="stock")Integer  stock,
                                       @RequestParam(name ="imgUrl")String imgUrl) throws BusinessException {

        //封装service请求用来创建商品
        ItemModel itemModel =new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelForReturn=itemService.CreateItem(itemModel);
        ItemVO itemVO =convertVOFromModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);
    }

    //商品详情页浏览
    @RequestMapping(value = "/getItem", method={RequestMethod.GET})
    @ResponseBody
    private CommonReturnType getItem(@RequestParam(name = "id")Integer id){

        ItemModel itemModel =itemService.getItemById(id);

        ItemVO itemVO =convertVOFromModel(itemModel);

        return CommonReturnType.create(itemVO);
    }

    //商品列表页面浏览
    @RequestMapping(value = "/listItem", method={RequestMethod.GET})
    @ResponseBody
    private CommonReturnType listItem() {
        List<ItemModel> itemModelList =itemService.ListItem();

        //使用stream api将list内的itemModel转化为itemVO
        List<ItemVO> itemVOList =itemModelList.stream().map(itemModel -> {
            ItemVO itemVO =this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);
    }

        private ItemVO convertVOFromModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemVO itemVO =new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;
    }


}
