package cn.yxd.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.zookeeper.server.SessionTracker;
import org.junit.jupiter.api.Test;

import javax.jms.*;

public class TestQueryProducer {
    /**
     * 总结：队列和广播
     * Topics和Queue和的最大区别在与：
     * Topics如果没有生产者会消息会消失，而Queue不会消失，会一直保留在activeMq的后台
     *
     *
     *
     * @throws Exception
     */
    //测试队列的使用者
    @Test
    public  void  testQueryProducer() throws  Exception{
        //1.创建一个connectionFactory对象，需要服务的id地址
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
        ///2.使用工厂对象创建一个Connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.创建一个Session对象
        //第一个参数：是否开启事务，默认为true，一般都是关闭事务
        //第二个参数：当第一个参数是false，表示开启了自动应答模式，设置参数为1时采用意义
//得到一个session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //// 第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
        Queue textActiveMq = session.createQueue("textActiveMq");
        //6.通过Session对象来创建一个Producer对象
        MessageProducer producer = session.createProducer(textActiveMq);
        // 第七步：创建一个Message对象，创建一个TextMessage对象
        TextMessage message = session.createTextMessage("hello activeMq");

        //  8发送消息
        producer.send(message);
        //关闭资源
        producer.close();
        session.close();
        connection.close();

    }

    /**
     * 测试队列的接受者
     * @throws Exception
     */
    @Test
     public  void testQueueConsumer () throws  Exception{
        ///以工厂模式的方式创建连接对象
         ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
         //根据工厂对象创建连接
         Connection connection = connectionFactory.createConnection();
         //开启连接
         connection.start();
         //根据连接创建Session
         ///false：不开启事务
         //AUTO_ACKNOWLEDGE：模式为自动应答
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         //	// 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。;
         Queue queue = session.createQueue("textActiveMq");
         //使用session对象创建一个Consumer对象
         MessageConsumer consumer = session.createConsumer(queue);
         //接受消息
         consumer.setMessageListener(new MessageListener() {
             @Override
             public void onMessage(Message message) {
                 try {
                     //获取队列的消息(接受消息)
                     TextMessage textMessage= (TextMessage) message;
                     String text = textMessage.getText();
                     //打印消息
                     System.out.println(textMessage);

                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });


     }
     //测试Topics，以广播的形式
    @Test
    public  void testTopics() throws  Exception{
        //1.创建一个connectionFactory对象，需要服务的id地址
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
        ///2.使用工厂对象创建一个Connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.创建一个Session对象
        //第一个参数：是否开启事务，默认为true，一般都是关闭事务
        //第二个参数：当第一个参数是false，表示开启了自动应答模式，设置参数为1时采用意义
//得到一个session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //// 第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
        Topic textTopics = session.createTopic("textTopics");
        //6.通过Session对象来创建一个Producer对象
        MessageProducer producer = session.createProducer(textTopics);
        // 第七步：创建一个Message对象，创建一个TextMessage对象
        TextMessage message = session.createTextMessage("hello Topics大帅哥2");

        //  8发送消息
        producer.send(message);
        System.out.println("topics的服务已经启动。。。。");
        System.in.read();
        //关闭资源
        producer.close();
        session.close();
        connection.close();

    }
    @Test
    public  void testTopCusmuer() throws  Exception{
        ///以工厂模式的方式创建连接对象
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
        //根据工厂对象创建连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //根据连接创建Session
        ///false：不开启事务
        //AUTO_ACKNOWLEDGE：模式为自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //	// 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。;
        Topic textTopics = session.createTopic("textTopics");
        //使用session对象创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(textTopics);
        //接受消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    //获取队列的消息(接受消息)
                    TextMessage textMessage= (TextMessage) message;
                    String text = textMessage.getText();
                    //打印消息
                    System.out.println(textMessage);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        System.out.println("topic的消费端01。。。。。");
        // 等待键盘输入
        System.in.read();
        // 第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();



    }



}
