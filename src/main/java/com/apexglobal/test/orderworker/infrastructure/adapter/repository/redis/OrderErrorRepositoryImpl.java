package com.apexglobal.test.orderworker.infrastructure.adapter.repository.redis;

import com.apexglobal.test.orderworker.domain.model.OrderError;
import com.apexglobal.test.orderworker.domain.port.outgoing.OrderErrorRepositoryPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderErrorRepositoryImpl implements OrderErrorRepositoryPort {

    private static final String ORDER_ID_LIST = "order_id_list";
    private final RedisTemplate<String, Object> redisTemplate;

    public OrderErrorRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(OrderError order) {
        redisTemplate.opsForValue().set(order.getOrderId(), order);
        redisTemplate.opsForList().rightPush(ORDER_ID_LIST, order.getOrderId());
    }

    @Override
    public Optional<OrderError> findById(String orderId) {
        return Optional.ofNullable((OrderError)redisTemplate.opsForValue().get(orderId));
    }

    @Override
    public List<OrderError> findAll() {
        List<Object> orderIds = redisTemplate.opsForList().range(ORDER_ID_LIST, 0, -1); // Recupera todos los IDs
        List<OrderError> orders = new ArrayList<>();
        if( orderIds != null){
            for (Object orderId : orderIds) {
                Optional<OrderError> order = findById(orderId.toString());
                order.ifPresent(orders::add);
            }
        }
        return orders;
    }


    @Override
    public void delete(String orderId) {
        redisTemplate.delete(orderId);
        redisTemplate.opsForList().remove(ORDER_ID_LIST, 1, orderId); // Elimina el ID de la lista

    }
}