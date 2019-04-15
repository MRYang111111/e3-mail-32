package cn.yxd.controller;

;

import cn.yxd.common.pojo.EasyUiDataGridResult;
import cn.yxd.common.util.E3Result;
import cn.yxd.pojo.TbItem;

import cn.yxd.pojo.TbItemDesc;
import cn.yxd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem selectItemById(@PathVariable Long itemId ){
        TbItem item = itemService.selectItemById(itemId);
            return  item;
    }
    //跳转到首页
    @RequestMapping("/")
    public  String toIndex(){
        return "index";
    }
    //商品后台的页面展示
    @RequestMapping("/{page}")
    public  String showPage(@PathVariable String page){
        return  page;
    }


    //商品的列表展示
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUiDataGridResult getItemList(Integer page,Integer rows){
        //调用服务
        EasyUiDataGridResult easyUiDataGridResult = itemService.selectItemList(page, rows);
        return  easyUiDataGridResult;
    }
    //商品列表的插入
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem tbItem,String desc){
        E3Result e3Result = itemService.addItem(tbItem, desc);
        return  e3Result;
    }
    //数据的回显
    @RequestMapping(value = "/rest/page/item-edit")
    //@ResponseBody
    public  String getItem(@RequestParam(value = "id",required =true,defaultValue = "562379") Long id){
       // System.out.println(id);
        TbItem tbItem1 = itemService.selectItemById(id);
        return  "item-edit";
    }
//    //数据的回显
//    @RequestMapping(value = "/rest/item/param/item/query/{itemid}")
//    @ResponseBody
//    public  TbItem getItemInfo(@PathVariable Long itemid){
//
//       TbItem tbItem1 = itemService.selectItemById(itemid);
//     return  tbItem1 ;
//
//  }
//    @RequestMapping(value = "/rest/item/query/item/desc/{itemid}")
//    @ResponseBody
//    public  TbItemDesc getDesc(@PathVariable  Long itemid){
//        TbItemDesc tbItemDesc = itemService.selectTbItemDescById(itemid);
//        return  tbItemDesc;
//
//    }
    @RequestMapping(value = "/rest/item/query/item/desc/{id}")
    public  String getDesc(@PathVariable  String id, ModelMap model){
        Long l=Long.valueOf(id);
        TbItemDesc tbItemDesc = itemService.selectTbItemDescById(l);
        System.out.println(tbItemDesc.toString());
        model.addAttribute("itemDesc",tbItemDesc);
        return  "item-edit";
    }
    //商品列表(TbItem,TbDesc)的修改
    @RequestMapping(value = "/rest/item/update")
    @ResponseBody
    public E3Result updateTbItem(TbItem tbItem,String desc){

        E3Result e3Result = itemService.updateItem(tbItem, desc);
        return  e3Result;
    }
    //商品的删除
    @RequestMapping(value = "/rest/item/delete")
    @ResponseBody
    public E3Result deleteTbItemByIds(String ids){
        E3Result e3Result = itemService.deleteTbItemsByIds(ids);
        return  e3Result;

    }
    //商品的下架
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public  E3Result updateStatue1(String ids){
        E3Result e3Result = itemService.updateStatue1(ids);
        return  e3Result;
    }
    //商品的上架
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public  E3Result updateStatue12(String ids){
        E3Result e3Result = itemService.updateStatue2(ids);
        return  e3Result;
    }


}
