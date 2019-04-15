package cn.yxd.controller;

import cn.yxd.common.pojo.EasyUiTheeNode;
import cn.yxd.common.util.E3Result;
import cn.yxd.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
public class ContentCategoryController {
    ///content/query/list
    //展示数据节点
    @Autowired
    private ContentCategoryService  contentCategoryService;
    @RequestMapping(value = "/content/category/list")
    @ResponseBody
    public List<EasyUiTheeNode> getContentCategoryTree(@RequestParam(value = "id",required = true,defaultValue = "0") Long id){
        List<EasyUiTheeNode> easyUiTheeNodes = contentCategoryService.getContentCategory(id);
        return  easyUiTheeNodes;
    }
    //添加数据节点到数据库

    @RequestMapping(value = "/content/category/create")
    @ResponseBody
    public E3Result createCategory(Long parentId, String name){
        //调用服务
        E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
        return e3Result;
    }
    @RequestMapping(value = "/content/category/delete/")
    @ResponseBody
    public E3Result deleteCategory(Long id){
        E3Result e3Result = contentCategoryService.deleteContentCategoryById(id);
        return  e3Result;
    }

}
