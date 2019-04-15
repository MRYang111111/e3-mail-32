package cn.yxd.controller;

import cn.yxd.common.pojo.EasyUiDataGridResult;
import cn.yxd.common.util.E3Result;
import cn.yxd.content.service.ContentService;
import cn.yxd.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 对内容表进行分页
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/content/query/list")
    @ResponseBody
    public EasyUiDataGridResult selectList(@RequestParam(value = "page",required = true,defaultValue = "1") Integer page,Integer rows){
        EasyUiDataGridResult easyUiDataGridResult = contentService.selectContentPage(page, rows);
        return  easyUiDataGridResult;

    }
    //TbContent表的添加
    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContent(TbContent tbContent){
        //调用服务
        E3Result e3Result = contentService.addContent(tbContent);
        return  e3Result;
    }
    //TbContent表的删除
    @RequestMapping(value = "/content/delete")
    @ResponseBody
    public E3Result deleteContent(String  ids){
        //调用服务
        E3Result e3Result = contentService.deleteContent(ids);
        return  e3Result;
    }





}
