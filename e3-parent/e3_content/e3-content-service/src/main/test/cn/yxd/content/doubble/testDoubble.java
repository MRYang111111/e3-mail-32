package cn.yxd.content.doubble;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class testDoubble {

    @Test
    public  void testE3_Content2() throws IOException {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        System.out.println("e3-content服务启动了--------");
        System.in.read();
        System.out.println("e3-content服务启动了服务关闭了--------");
    }

}
