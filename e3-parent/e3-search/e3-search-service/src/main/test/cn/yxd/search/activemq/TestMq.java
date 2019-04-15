package cn.yxd.search.activemq;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMq {
    /**
     * 测试与spring的整合后接收消息
     */
    @Test
    public  void  testQueueConsumer() throws  Exception{
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        System.in.read();

    }
}
