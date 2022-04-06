package com.jared.warehouse.vendor.vendororderproducer.controller;

import com.jared.warehouse.vendor.vendororderproducer.model.VendorOrder;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(value = "/vendor/orders")
public class VendorOrderController {
    
    private KafkaTemplate<String, VendorOrder> vendorOrderProducer;

    @Autowired
    public VendorOrderController(KafkaTemplate<String, VendorOrder> vendorOrderProducer) {
        this.vendorOrderProducer = vendorOrderProducer;    
    }

    @PostMapping()
    public VendorOrder submitOrder(@RequestBody VendorOrder request) {
        ProducerRecord<String, VendorOrder> record 
            = new ProducerRecord<String,VendorOrder>("vender-order-topic", request.getVendorId(), request);

        ListenableFuture<SendResult<String, VendorOrder>> future = vendorOrderProducer.send(record);

        future.addCallback(new KafkaSendCallback<String,VendorOrder>() {

			@Override
			public void onSuccess(SendResult<String, VendorOrder> result) {
				// TODO Auto-generated method stub

                log.info("Successfully sent message to vendor-order-topic" + result.getProducerRecord().value().toString());
				
			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub
                log.error(ex);
			}

			@Override
			public void onFailure(KafkaProducerException ex) {
				// TODO Auto-generated method stub
				log.error(ex);
			}
            
        });

        return request;
    }
}
