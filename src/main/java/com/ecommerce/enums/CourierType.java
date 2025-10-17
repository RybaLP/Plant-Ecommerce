package com.ecommerce.enums;

import java.math.BigDecimal;

public enum CourierType {

    DHL("DHL", BigDecimal.valueOf(20.00)),
    INPOST("InPost", BigDecimal.valueOf(16.00)),
    GLS("GLS", BigDecimal.valueOf(18.00));

    private final String displayName;
    private final BigDecimal deliveryPrice;

    CourierType(String displayName , BigDecimal deliveryPrice) {
        this.displayName = displayName;
        this.deliveryPrice = deliveryPrice;
    }

    public String getDisplayName(){
        return this.displayName;
    }

    public BigDecimal getDeliveryPrice() {
        return this.deliveryPrice;
    }
}
