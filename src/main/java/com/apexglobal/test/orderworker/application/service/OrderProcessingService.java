package com.apexglobal.test.orderworker.application.service;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.application.dto.OrderDTO;
import com.apexglobal.test.orderworker.application.dto.OrderMapper;
import com.apexglobal.test.orderworker.application.dto.OrderMessageDTO;
import com.apexglobal.test.orderworker.application.exception.AlreadyBeenProcessedException;
import com.apexglobal.test.orderworker.application.exception.MaxRetryException;
import com.apexglobal.test.orderworker.domain.exception.CustomerNotFoundOrInactiveException;
import com.apexglobal.test.orderworker.domain.exception.OrderNotFoundException;
import com.apexglobal.test.orderworker.domain.exception.ProductNotFoundException;
import com.apexglobal.test.orderworker.domain.model.Order;
import com.apexglobal.test.orderworker.domain.model.OrderError;
import com.apexglobal.test.orderworker.domain.model.Product;
import com.apexglobal.test.orderworker.domain.port.incoming.CounterUseCase;
import com.apexglobal.test.orderworker.domain.port.incoming.CustomerUseCase;
import com.apexglobal.test.orderworker.domain.port.incoming.LockResourceUseCase;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderErrorUseCase;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderProcessingUseCase;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderUseCase;
import com.apexglobal.test.orderworker.domain.port.incoming.ProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingService implements OrderProcessingUseCase {

    private final OrderUseCase orderService;
    private final OrderErrorUseCase orderErrorService;
    private final OrderMapper orderMapper;
    private final ProductUseCase productService;
    private final CustomerUseCase customerService;
    private final LockResourceUseCase lockResourceService;
    private final CounterUseCase counterService;

    @Value("${spring.kafka.listener.retry.max-attempts}")
    private Long maxAttempts;

    @Override
    public void processOrder(OrderMessageDTO orderMessageDto) throws InterruptedException {
        lockResourceService.executeWithLock(orderMessageDto.getOrderId(), () -> {
            try {
                validateUniqueness(orderMessageDto);
                validateRetries(orderMessageDto);
                Order order = orderMapper.toModel(orderMessageDto);
                validateCustomer(order.getCustomerId());
                List<Product> products = enrichProducts(orderMessageDto.getProducts());
                order.setProducts(products);
                orderService.create(order);
            } catch (CustomerNotFoundOrInactiveException | ProductNotFoundException e) {
                handleError(orderMessageDto, e);
                throw e;
            }
        });
    }

    private void validateUniqueness(OrderMessageDTO orderMessageDto) {
        try {
            OrderDTO o = orderService.findById(orderMessageDto.getOrderId());
            String msg = "Order "+ orderMessageDto.getOrderId()+" has already been processed";
            OrderError orderError = orderMapper.toModelOrderError(orderMessageDto);
            orderError.setError(msg);
            orderErrorService.save(orderError);
            throw new AlreadyBeenProcessedException(msg);
        } catch (OrderNotFoundException e){
            log.info(e.getMessage(), e);
        }
    }

    private void validateRetries(OrderMessageDTO orderMessageDto) {
        long retries = counterService.getCounterValue(orderMessageDto.getOrderId());
        if(retries>= maxAttempts){
            String msg = "Order "+ orderMessageDto.getOrderId()+" has reached the maximum number of allowed retries";
            OrderError orderError = orderMapper.toModelOrderError(orderMessageDto);
            orderError.setError(msg);
            orderError.setRetries(retries);
            orderErrorService.save(orderError);
            throw new MaxRetryException(msg);
        }
    }

    private List<Product> enrichProducts(List<String> productIds) {
        return productIds.stream()
                .map(this::getProduct)
                .peek(product -> product.setProductId(product.getProductId()))
                .collect(Collectors.toList());
    }

    private void validateCustomer(String customerId) {
        customerService.findById(customerId)
                .filter(CustomerDTO::isActive)
                .orElseThrow(() -> new CustomerNotFoundOrInactiveException("Customer no encontrado o inactivo: " + customerId));
    }

    private Product getProduct(String productId) {
        return productService.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Producto not found " + productId));
    }

    private void handleError(OrderMessageDTO orderMessageDto, Exception e) {
        OrderError orderError = orderMapper.toModelOrderError(orderMessageDto);
        orderError.setError(e.getLocalizedMessage());
        orderError.setRetries(counterService.incrementCounter(orderMessageDto.getOrderId()));
        orderErrorService.save(orderError);
    }
}