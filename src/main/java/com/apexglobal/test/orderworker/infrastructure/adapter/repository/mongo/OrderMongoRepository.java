package com.apexglobal.test.orderworker.infrastructure.adapter.repository.mongo;

import com.apexglobal.test.orderworker.domain.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("orderMongoRepository")
public interface OrderMongoRepository extends MongoRepository<Order, String> {

    Optional<Order> findByOrderId(String orderId);
}