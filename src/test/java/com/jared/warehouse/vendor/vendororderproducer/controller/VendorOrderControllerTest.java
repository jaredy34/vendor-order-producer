package com.jared.warehouse.vendor.vendororderproducer.controller;

import com.jared.warehouse.vendor.vendororderproducer.model.VendorOrder;
import com.jared.warehouse.vendor.vendororderproducer.model.VendorOrderLineItem;
import com.jared.warehouse.vendor.vendororderproducer.util.EmbeddedKafkaHolder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VendorOrderControllerTest {

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

    private static VendorOrder mockRequest;

    @BeforeAll
    public static void setup() {
        mockRequest = new VendorOrder();

        VendorOrderLineItem lineItem = new VendorOrderLineItem();
        lineItem.setId("999999999");
        lineItem.setName("Chicken");
        lineItem.setPrice(-1);
        lineItem.setQuantity(150);

        List<VendorOrderLineItem> items = new ArrayList<>();
        items.add(lineItem);
        
        mockRequest.setDateOrdered(Date.from(Instant.now()));
        mockRequest.setItems(items);
        mockRequest.setTotal(500.00);
        mockRequest.setVendorId("0123456789");
    }

    @Test
    public void givenValidRequest_vendorOrderProducer_producesMessageToTopic(@Autowired WebTestClient webClient) {
        webClient
            .post().uri("/vendor/orders")
            .bodyValue(mockRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(VendorOrder.class)
            .value(new VendorOrderAsserter());
    }
}

class VendorOrderAsserter implements Consumer<VendorOrder> {

    @Override
    public void accept(VendorOrder order) {
        assertEquals(500.00, order.getTotal());
        assertEquals("0123456789", order.getVendorId());
        assertNotEquals(0, order.getItems().size());
        assertNotNull(order.getItems());
        assertTrue(order.getDateOrdered().before(Date.from(Instant.now())));
    }

}
