package cn.yxd.item.controller;

import cn.yxd.item.pojo.Item;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbItemDesc;
import cn.yxd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShowItemInfoController {
    @Autowired
    private ItemService itemService;

    /*
    根据商品的id查看商品信息，根据商品的id查询商品的描述

     */
    @RequestMapping(value = "/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model){
        //调用服务查询商品信息
        TbItem tbItem = itemService.selectItemById(itemId);
        //对数据进行包装
        Item item=new Item(tbItem);
        model.addAttribute("item",item);
        //查询商品描述表
        TbItemDesc itemDesc = itemService.selectTbItemDescById(itemId);
        model.addAttribute("itemDesc",itemDesc);
        return  "item";
    }

}
