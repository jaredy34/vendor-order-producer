package com.jared.warehouse.vendor.vendororderproducer.config;

import com.jared.warehouse.vendor.vendororderproducer.model.VendorOrder;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
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
        return new DefaultKafkaProducerFactory<>(producerAndConsumerConfigs(), new StringSerializer(), new JsonSerializer<VendorOrder>());
    }

    @Bean
    public Map<String, Object> producerAndConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();

        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        return props;
    }

    @Bean
    public KafkaTemplate<String, VendorOrder> kafkaTemplate() {
        return new KafkaTemplate<String, VendorOrder>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, VendorOrder> consumerFactory() {
        JsonDeserializer<VendorOrder> deserializer = new JsonDeserializer<>(VendorOrder.class);

        return new DefaultKafkaConsumerFactory<>(producerAndConsumerConfigs(), new StringDeserializer(), deserializer);
    }
}
