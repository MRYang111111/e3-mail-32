package cn.yxd.jedis;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * 参数Jedis的java客户端连接Redis服务
 */
public class TestJedis {
    @Test
    public  void testJedis(){
 //创建一个Jedis对象，需要端口号和id
        Jedis jedis=new Jedis("192.168.25.133",6379);
        //使用jedis操作redis所有的jedis的名命令
        String set = jedis.set("testJedis", "hello jedis");
        String testJedis = jedis.get("testJedis");
        System.out.println(testJedis);
        //关闭
        jedis.close();

    }
    //使用jedis连接处获取lianjie
    @Test
    public  void  testJedisPool(){
        //创建连接池对象，需要端口号和ip
        JedisPool jedisPool=new JedisPool("192.168.25.133",6379);
        //使用jedis进行连接
        Jedis jedis=jedisPool.getResource();
        String testJedis = jedis.get("testJedis");
        System.out.println(testJedis);
        //需要使用Jedis释放资源
        jedis.close();
        //关闭资源
        jedisPool.close();
    }
    //Redis的集群连接方式
    @Test
    public  void  testJredisCluster(){
        //创建JredisCluster对象，需要一个Node类型的set集合，set集合中包含有HostAndPort
        Set<HostAndPort> set=new HashSet<>();
        set.add(new HostAndPort("192.168.25.133",7001));
        set.add(new HostAndPort("192.168.25.133",7002));
        set.add(new HostAndPort("192.168.25.133",7003));
        set.add(new HostAndPort("192.168.25.133",7004));
        set.add(new HostAndPort("192.168.25.133",7005));
        set.add(new HostAndPort("192.168.25.133",7006));
        JedisCluster jedisCluster=new JedisCluster(set);
        jedisCluster.set("testJedis","hello yxd");
        //直接使用Jredis对象炒操作Redis
        String testJedis = jedisCluster.get("testJedis");
        System.out.println(testJedis);
        jedisCluster.close();
    }







}
