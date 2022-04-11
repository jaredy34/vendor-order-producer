package com.jared.warehouse.vendor.vendororderproducer.controller;

import com.jared.warehouse.vendor.vendororderproducer.model.VendorOrder;
import com.jared.warehouse.vendor.vendororderproducer.producer.VendorOrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/vendor/orders")
public class VendorOrderController {
    private VendorOrderProducer vendorOrderProducer;

    @Autowired
    public VendorOrderController(VendorOrderProducer vendorOrderProducer) {
        this.vendorOrderProducer = vendorOrderProducer;    
    }

    @PostMapping()
    public VendorOrder submitOrder(@RequestBody @Valid VendorOrder request) {
        vendorOrderProducer.send(request);

        return request;
    }
}
