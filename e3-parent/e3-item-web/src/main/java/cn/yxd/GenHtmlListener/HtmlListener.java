package cn.yxd.GenHtmlListener;

import cn.yxd.item.pojo.Item;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbItemDesc;
import cn.yxd.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品添加成功后发送消息，item-web收到消息后，获取商品的id，生成html的静态页面:静态页面的类型为:商品id+.html
 */

public class HtmlListener  implements MessageListener{
    //注入freemarker
    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;

    @Autowired
    private ItemService itemService;

    @Value(value = "GEN_HTML_PATH")
    private String GEN_HTML_PATH;

    @Override
    public void onMessage(Message message) {
        try {
            //通过freemark对象获取一个模板对象
            Configuration configuration = freemarkerConfig.getConfiguration();
            //获取jsp的模板对象
            //查询出商品的id
            TextMessage textMessage= (TextMessage) message;
            //根据商品的id查询出商品信息及商品描述
            //等待事务提交
            String id = textMessage.getText();
            Thread.sleep(1000);
            Long l=new Long(id);
            TbItem tbItem = itemService.selectItemById(l);
            Item item=new Item(tbItem);
            TbItemDesc tbItemDesc = itemService.getDescById(new Long(l));
            //准备一个map容器
            Map data=new HashMap();
            //将商品信息和商品描述进行分装
            data.put("item",item);
            data.put("itemDesc",tbItemDesc);
            Template template = configuration.getTemplate("item.ftl");
            //创建一个流对象,指定文件名及输出的路径
            Writer writer=new FileWriter(new File(GEN_HTML_PATH+l+".html"));
            //生成html文件
            template.process(data,writer);
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }




    }
}
