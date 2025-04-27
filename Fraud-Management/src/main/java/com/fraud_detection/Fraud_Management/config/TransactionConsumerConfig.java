package com.fraud_detection.Fraud_Management.config;


import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.*;

@Slf4j
@Configuration
@EnableKafka
@RequiredArgsConstructor
public class TransactionConsumerConfig {


    @Autowired
    private  MeterRegistry meterRegistry;

    @Value("${kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.consumer.group-id}")
    private String ordersKafkaConsumerGroup;

    @Value("${kafka.maxPollRecords}")
    private String ordersKafkaMaxPollRecords;

    @Value("${kafka.maxPollIntervals}")
    private String ordersKafkaMaxPollIntervalMs;

    @Value("${kafka.sessionTimeout}")
    private String ordersKafkaSessionTimeoutMs;

    @Value("${kafka.concurrency}")
    private String ordersIndexingConcurrency;

    @Value("${kafka.idleEventInterval}")
    private String ordersIdleEventIntervalMs;



//    public KafkaConfig() {
//    }

    private Map<String, Object> transactionConsumerConfigs()
    {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, ordersKafkaConsumerGroup);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, Collections.singletonList(CooperativeStickyAssignor.class));


        return props;
    }


    private ConsumerFactory<String, TransactionDTO> transactionConsumerFactory()
    {
        Map<String, Object> sosGOMOrdersConsumerConfigMap = transactionConsumerConfigs();
        sosGOMOrdersConsumerConfigMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, ordersKafkaMaxPollRecords);
        sosGOMOrdersConsumerConfigMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, ordersKafkaMaxPollIntervalMs);
        sosGOMOrdersConsumerConfigMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, ordersKafkaSessionTimeoutMs);

        DefaultKafkaConsumerFactory<String, TransactionDTO> consumerFactory = new DefaultKafkaConsumerFactory<>(sosGOMOrdersConsumerConfigMap,
                new StringDeserializer(),
                new JsonDeserializer<>(TransactionDTO.class,false));
        consumerFactory.addListener(new MicrometerConsumerListener<>(meterRegistry));
        return consumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionDTO> transactionKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConcurrency(Integer.parseInt(ordersIndexingConcurrency));
        factory.getContainerProperties().setIdleEventInterval(Long.valueOf(ordersIdleEventIntervalMs));
        factory.setBatchListener(Boolean.TRUE);
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        return factory;

    }

}
