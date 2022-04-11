package com.jared.warehouse.vendor.vendororderproducer.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;
import java.util.List;

@Data
public class VendorOrder {
    
    @NotNull
    private String vendorId;
    
    @NotEmpty
    private List<VendorOrderLineItem> items;
    
    @Min(value = 0)
    private double total;
    
    @PastOrPresent
    private Date dateOrdered;
}
