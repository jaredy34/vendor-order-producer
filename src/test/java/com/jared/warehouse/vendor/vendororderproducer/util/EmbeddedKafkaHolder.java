package com.jared.warehouse.vendor.vendororderproducer.util;

import org.springframework.kafka.KafkaException;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

public final class EmbeddedKafkaHolder {
    private static EmbeddedKafkaBroker embeddedKafka;

    static {
        embeddedKafka = new EmbeddedKafkaBroker(1, false)
                .brokerListProperty("spring.kafka.bootstrap-servers")
                .brokerProperty("listeners", "PLAINTEXT://:9092");
    }

    private static boolean started;

    public static EmbeddedKafkaBroker getEmbeddedKafka() {
        if (!started) {
            try {
                embeddedKafka.afterPropertiesSet();
            }
            catch (Exception e) {
                throw new KafkaException("Embedded broker failed to start", e);
            }
            started = true;
        }
        return embeddedKafka;
    }

    private EmbeddedKafkaHolder() {
        super();
    }

}