package btgpactual.orderms.DesafioBTG.listener.dto;

import java.math.BigDecimal;

public record OrderItemEvent( String produto, Integer quantidade, BigDecimal preco) {
    
}
