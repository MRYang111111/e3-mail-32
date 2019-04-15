package cn.yxd.search.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接收消息的实现类
 */
public class MyMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage= (TextMessage) message;
        try {
            String text = textMessage.getText();
            System.out.println("接收到的消息是"+text);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
