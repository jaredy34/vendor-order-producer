package com.jared.warehouse.vendor.vendororderproducer.producer;

import com.jared.warehouse.vendor.vendororderproducer.model.VendorOrder;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VendorOrderProducer {
    private KafkaTemplate<String, VendorOrder> kafkaTemplate;

    @Autowired
    public VendorOrderProducer(KafkaTemplate<String, VendorOrder> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;    
    }

    public void send(VendorOrder vendorOrder) {
        ProducerRecord<String, VendorOrder> record 
            = new ProducerRecord<String,VendorOrder>("vendor-order-topic", vendorOrder.getVendorId(), vendorOrder);

        log.info("Sending vendor order to vendor-order-topic");
        
        ListenableFuture<SendResult<String, VendorOrder>> future = kafkaTemplate.send(record);

        future.addCallback(new KafkaSendCallback<String,VendorOrder>() {

			@Override
			public void onSuccess(SendResult<String, VendorOrder> result) {
				// TODO Auto-generated method stub

                log.info("Successfully sent message to vendor-order-topic" + result.getProducerRecord().value().toString());
				
			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub
                log.error(ex.toString());
			}

			@Override
			public void onFailure(KafkaProducerException ex) {
				// TODO Auto-generated method stub
				log.error(ex.toString());
			}
            
        });
    }
}
