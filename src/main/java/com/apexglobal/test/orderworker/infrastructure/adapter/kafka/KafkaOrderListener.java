package com.apexglobal.test.orderworker.infrastructure.adapter.kafka;

import com.apexglobal.test.orderworker.application.dto.OrderMessageDTO;
import com.apexglobal.test.orderworker.domain.exception.CustomerNotFoundOrInactiveException;
import com.apexglobal.test.orderworker.domain.exception.ProductNotFoundException;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderProcessingUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderListener {

    private final OrderProcessingUseCase orderProcessingService;


    @KafkaListener(topics = "${spring.kafka.consumer.topics.order-topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Retryable(
            retryFor = {ProductNotFoundException.class, CustomerNotFoundOrInactiveException.class},
            maxAttemptsExpression = "#{${spring.kafka.listener.retry.max-attempts}}",
            backoff = @Backoff(
                    delayExpression = "#{${spring.kafka.listener.retry.backoff.initial-interval}}",
                    maxDelayExpression = "#{${spring.kafka.listener.retry.backoff.max-interval}}",
                    multiplierExpression = "#{${spring.kafka.listener.retry.backoff.multiplier}}"
            )
    )
    public void listen(OrderMessageDTO orderMessage) {
        try {
            orderProcessingService.processOrder(orderMessage);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }


}
