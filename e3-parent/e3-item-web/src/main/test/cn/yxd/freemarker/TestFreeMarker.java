package cn.yxd.freemarker;

import cn.yxd.item.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestFreeMarker {
    /**
     * 测试freemarker生成文件
     * @throws Exception
     */
    @Test
    public  void  testFreeMarker() throws  Exception{
        //创建一个config对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //设置模板所在额路基
        configuration.setDirectoryForTemplateLoading(new File("D:\\ideaProject\\e3-parent\\e3-item-web\\src\\main\\webapp\\freemarker"));
        //设置编码字符
        configuration.setDefaultEncoding("utf-8");
        //加载模板添加对象
        Template template = configuration.getTemplate("hello.ftl");
        //向数据集中添加数据
        Map map=new HashMap();
        map.put("hello","hello freemarker");
        //	// 第七步：调用模板对象的process方法输出文件。
        Writer out=new FileWriter(new File("D:\\hello\\hello.html"));
        //添加数据
        template.process(map,out);
        //关闭流
        out.close();

    }
    //测试freemarker2
    @Test
    public  void  testFreeMarker2() throws  Exception{
        //创建一个config对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //设置模板所在额路基
        configuration.setDirectoryForTemplateLoading(new File("D:\\ideaProject\\e3-parent\\e3-item-web\\src\\main\\webapp\\freemarker"));
        //设置编码字符
        configuration.setDefaultEncoding("utf-8");
        //加载模板添加对象
        Template template = configuration.getTemplate("student.ftl");
        //向数据集中添加数据
        Map map=new HashMap();
       //将学生信息添加进数据中
        Student student=new Student(1,"小明",20,"北京昌平区");
        //向学生中添加List集合，动态的展示在页面
        List<Student> list=new ArrayList<>();
        list.add(new Student(2,"小明2",21,"北京昌平区1"));
        list.add(new Student(3,"小明3",22,"北京昌平区11"));
        list.add(new Student(4,"小明4",23,"北京昌平区111"));
        list.add(new Student(5,"小明5",24,"北京昌平区111"));
        //	// 第七步：调用模板对象的process方法输出文件。
        map.put("hello","hello freemarker");
        map.put("student",student);
        //从集合中取出数据
        map.put("studentlist",list);
        //日期的转换
        map.put("data",new Date());
        //null值的处理
        map.put("val",123);
        Writer out=new FileWriter(new File("D:\\hello\\student.html"));
        //添加数据
        template.process(map,out);
        //关闭流
        out.close();

    }



}
