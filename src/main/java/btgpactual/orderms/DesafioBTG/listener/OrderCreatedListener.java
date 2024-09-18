package btgpactual.orderms.DesafioBTG.listener;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static btgpactual.orderms.DesafioBTG.config.RabbitMqConfig.ORDER_CREATED_QUEUE;
import btgpactual.orderms.DesafioBTG.listener.dto.OrderCreatedEvent;
import btgpactual.orderms.DesafioBTG.service.OrderService;


@Component
public class OrderCreatedListener {

    /*Checar se a mensagem está sendo consumida */
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final OrderService orderService;

    public OrderCreatedListener(OrderService orderService) {
        this.orderService = orderService;
    }


    @RabbitListener(queues= ORDER_CREATED_QUEUE)
    public void Listen(Message<OrderCreatedEvent> message) {
        logger.info("Message consumed: {}", message);

        orderService.save(message.getPayload());
  
        
    }

    public Object itens() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
