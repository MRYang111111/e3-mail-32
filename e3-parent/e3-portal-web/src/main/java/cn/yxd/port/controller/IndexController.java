package cn.yxd.port.controller;

import cn.yxd.common.util.JsonUtils;
import cn.yxd.content.service.ContentService;
import cn.yxd.pojo.TbContent;
import cn.yxd.port.pojo.AD1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {
    @Autowired
    private ContentService contentService;
    //取出轮播图的id
    @Value("${Content_LUNBO_ID}")
    public Long Content_LUNBO_ID;
    //设置宽度和高度
    @Value("${AD1_WIDTH}")
    public Integer AD1_WIDTH;

    @Value("${AD1_WIDTH_B}")
    public Integer AD1_WIDTH_B;

    @Value("${AD1_HEIGHT}")
    public Integer AD1_HEIGHT;

    @Value("${AD1_HEIGHT_B}")
    public Integer AD1_HEIGHT_B;


    @RequestMapping(value = "/index")
    public  String  getIndex( Model model){
        //取出首页的伦布图的路径
        List<TbContent> contentList = contentService.getContentList(Content_LUNBO_ID);
        List<AD1Node> list=new ArrayList<AD1Node>();
        for (TbContent tbContent:contentList){
            AD1Node ad1Node=new AD1Node();
           ad1Node.setAlt(tbContent.getTitle());
           ad1Node.setHref(tbContent.getUrl());
           ad1Node.setSrc(tbContent.getPic());
           ad1Node.setHeight(AD1_HEIGHT);
           ad1Node.setHeightB(AD1_HEIGHT_B);
            ad1Node.setWidth(AD1_WIDTH);
            ad1Node.setHeightB(AD1_WIDTH_B);

            System.out.println("Content表中的内容为"+tbContent);
            list.add(ad1Node);
        }
        //转换成字符串
        String s = JsonUtils.objectToJson(list);
        model.addAttribute("ad1List",s);
        return  "index";
    }


}
