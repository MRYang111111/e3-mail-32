package cn.yxd.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GenHtml {

    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;

    /**
     * 相应一个普通的freemarker页面
     */
    @RequestMapping(value = "/genhtml")
    @ResponseBody
    public  String getFreeMarker() throws Exception {
        //重spring容器中初始化yigefreemarker对象
        //获取Configration对象
        Configuration configuration = freemarkerConfig.getConfiguration();
        //重configatiion对象中获取一个模板对象TempLate
        Template template = configuration.getTemplate("hello.ftl");
        //准备数据的容器map
        Map map=new HashMap();
        map.put("hello","杨兴雕大帅哥!!!");
        //创建一个流对象
        Writer out=new FileWriter(new File("D:\\hello\\GenHtml.html"));
        //使用freemarker的posuer的方法生成文件
        template.process(map,out);
        //返回ok
        return  "OK";

    }

}
