package btgpactual.orderms.DesafioBTG.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import btgpactual.orderms.DesafioBTG.entity.OrderEntity;


public interface OrderRepository extends MongoRepository<OrderEntity, Long>{
    
    Page<OrderEntity> findAllByCostumerId(Long costumerId, PageRequest pageRequest);

}

