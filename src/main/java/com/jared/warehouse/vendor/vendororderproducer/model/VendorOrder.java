package com.jared.warehouse.vendor.vendororderproducer.model;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VendorOrder {
    @NotNull
    private String vendorId;
    @NotEmpty
    private List<VendorOrderLineItem> items;
    @Min(value = 0)
    private double total;
    private Date dateOrdered = Date.from(Instant.now());
}
