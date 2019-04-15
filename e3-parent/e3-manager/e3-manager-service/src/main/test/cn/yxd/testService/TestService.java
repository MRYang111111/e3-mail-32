package cn.yxd.testService;

import org.testng.annotations.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public class TestService {
    @Test
    public  void  testE3Manager() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        System.out.println("服务已经启动-------------");
        System.in.read();
        System.out.println("服务已经关闭了111111111111-------------");
    }

}
