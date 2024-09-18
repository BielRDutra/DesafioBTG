package btgpactual.orderms.DesafioBTG.controller.dto;

import java.math.BigDecimal;

import btgpactual.orderms.DesafioBTG.entity.OrderEntity;

public record OrderResponse(Long orderId, Long costumerId, BigDecimal total) {

    public static OrderResponse fromEntity(OrderEntity entity) {
        return new OrderResponse(entity.getOrderId(),entity.getCostumerId(), entity.getTotal());
    }

}

