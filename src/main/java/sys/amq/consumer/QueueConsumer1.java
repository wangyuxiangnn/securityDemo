package sys.amq.consumer;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * author: zf
 * Date: 2016/12/6  13:52
 * Description:队列式消息接收者
 */
@Component
public class QueueConsumer1 implements SessionAwareMessageListener<Message> {

    @Autowired
    private ActiveMQConnectionFactory connectionFactory;

    private Destination destination;//消息返回目的地
    @Override
    public void onMessage(Message message,Session session) {
        try {
            if (message instanceof TextMessage) {
                System.out.println("收到一条消息");
                System.out.println("消息内容是：" + ((TextMessage) message).getText());
                int acknowledgeMode = session.getAcknowledgeMode();
                boolean transacted = session.getTransacted();
                MessageProducer producer = session.createProducer(destination);
                TextMessage textMessage = session.createTextMessage("接收成功！");
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);//非持久化
                producer.send(textMessage);
                message.acknowledge();
            } else {
                System.out.println("Consumer:->Received: " + message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}