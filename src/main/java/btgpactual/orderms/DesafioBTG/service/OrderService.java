package btgpactual.orderms.DesafioBTG.service;


import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import btgpactual.orderms.DesafioBTG.controller.dto.OrderResponse;
import btgpactual.orderms.DesafioBTG.entity.OrderEntity;
import btgpactual.orderms.DesafioBTG.entity.OrderItem;
import btgpactual.orderms.DesafioBTG.listener.dto.OrderCreatedEvent;
import btgpactual.orderms.DesafioBTG.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository,
                        MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event) {

        var entity = new OrderEntity();

        entity.setOrderId(event.codigoPedido());
        entity.setCostumerId(event.codigoCliente());
        entity.setItems(getOrderItems(event));
        entity.setTotal(getTotal(event));

        orderRepository.save(entity);

    }

    public Page<OrderResponse> findAllByCustomerId(Long costumerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCostumerId(costumerId, pageRequest);

        /*Convertendo entity para response */
        return orders.map(OrderResponse::fromEntity);
    }
    /*Retorna o smoatório da compra do cliente */
    public BigDecimal findTotalOnOrdersByCostumerId(Long costumerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(costumerId)),
                group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);
        /*Tenta achar a chave total,se não achar retorn 0 */
        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());

    }

    private BigDecimal getTotal(OrderCreatedEvent event){
        return event.itens().stream().map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade()))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
    
    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream().map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco())).toList();


    }

}
