package com.jared.warehouse.vendor.vendororderproducer.producer;

import com.jared.warehouse.vendor.vendororderproducer.model.VendorOrder;
import com.jared.warehouse.vendor.vendororderproducer.util.EmbeddedKafkaHolder;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class VendorOrderProducerTest {

    static {
        EmbeddedKafkaBroker broker = EmbeddedKafkaHolder.getEmbeddedKafka();

        boolean topicExists
                = broker.getTopics()
                .stream()
                .anyMatch(topic -> topic.equals("vendor-order-topic"));

        if (!topicExists) {
            broker.addTopics("vendor-order-topic");
        }
    }

    private static final EmbeddedKafkaBroker broker = EmbeddedKafkaHolder.getEmbeddedKafka();

    @Autowired
    private VendorOrderProducer producer;

    @Test
    public void givenAVendorOrder_whenProducerIsInvoked_thenProduceOneMessageToTopic() {
        VendorOrder vendorOrderMock = new VendorOrder();

        vendorOrderMock.setVendorId("9583095193");
        vendorOrderMock.setDateOrdered(Date.from(Instant.now()));
        vendorOrderMock.setItems(new ArrayList<>());
        vendorOrderMock.setTotal(100.00);

        producer.send(vendorOrderMock);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", broker);
    
        JsonDeserializer<VendorOrder> deserializer = new JsonDeserializer<>(VendorOrder.class);

        ConsumerFactory<String, VendorOrder> cf 
            = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), deserializer);

        Consumer<String, VendorOrder> consumer = cf.createConsumer();

        broker.consumeFromAnEmbeddedTopic(consumer, "vendor-order-topic");

        ConsumerRecords<String, VendorOrder> records = KafkaTestUtils.getRecords(consumer);

        Iterable<ConsumerRecord<String, VendorOrder>> iterable = records.records("vendor-order-topic");

        Map<String, VendorOrder> orderMap = new HashMap<>();

        iterable.forEach((record) -> orderMap.put(record.key(), record.value()));

        VendorOrder order = orderMap.get("9583095193");

        assertNotNull(order);
        assertEquals("9583095193", order.getVendorId());
    }
}
