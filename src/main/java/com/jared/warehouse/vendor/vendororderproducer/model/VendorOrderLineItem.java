package com.jared.warehouse.vendor.vendororderproducer.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class VendorOrderLineItem {
    @NotNull
    private String id;
    @NotBlank
    private String name;
    @Positive
    private int quantity;
    @Min(value = 0)
    private double price;
}
