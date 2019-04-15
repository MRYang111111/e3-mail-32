package cn.yxd.content.jedis;

import cn.yxd.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedis {
    //测试单机版的JedisClient对象

    @Test
    public  void testJedis(){
        //通过spring容器初始化一个初始化一个JedisClient对象
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/jedis.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        //获取key与值
        String set = jedisClient.set("helle", "杨兴雕大帅哥");
        String helle = jedisClient.get("helle");
        System.out.println(helle);
    }
}
