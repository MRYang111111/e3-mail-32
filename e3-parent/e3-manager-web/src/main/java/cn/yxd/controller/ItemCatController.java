package cn.yxd.controller;

import cn.yxd.common.pojo.EasyUiTheeNode;
import cn.yxd.content.service.ContentCategoryService;
import cn.yxd.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUiTheeNode> getEasyUiNodeTree(@RequestParam(value = "id",defaultValue = "0") Long  parentId){
        //相应得参数
        List<EasyUiTheeNode> list = itemCatService.getEasyUiTreeNode(parentId);
        return  list;
    }
}
