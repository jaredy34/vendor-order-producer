package com.jared.warehouse.vendor.vendororderproducer.config;

import java.util.HashMap;
import java.util.Map;

import com.jared.warehouse.vendor.vendororderproducer.model.VendorOrder;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ToStringSerializer;

@Configuration
public class KafkaConfig {
    
    /**
     * If Topic doesn't exist in the broker, create the vendor-order-topic with the specified configurations
     * @return
     */
    @Bean
    public NewTopic buildTopic() {
        return TopicBuilder.name("vendor-order-topic")
            .partitions(10)
            .replicas(1)
            .compact()
            .build();
    }

    @Bean
    public ProducerFactory<String, VendorOrder> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ToStringSerializer.class);

        // See https://kafka.apache.org/documentation/#producerconfigs for more properties

        return props;
    }

    @Bean
    public KafkaTemplate<String, VendorOrder> kafkaTemplate() {
        return new KafkaTemplate<String, VendorOrder>(producerFactory());
    }
}
