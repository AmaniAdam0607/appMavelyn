package org.dromio.client001.models.data;

import jakarta.persistence.*;

@Entity
public class UnitConversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String itemId;
    String fromUnitId;
    String toUnitId;

    private double conversionFactor;

    public UnitConversion(Long id, String toUnitId, String fromUnitId, String itemId, double conversionFactor) {
        this.id = id;
        this.toUnitId = toUnitId;
        this.fromUnitId = fromUnitId;
        this.itemId = itemId;
        this.conversionFactor = conversionFactor;
    }

    public UnitConversion(String itemId, String fromUnitId, String toUnitId, double conversionFactor) {
        this.itemId = itemId;
        this.fromUnitId = fromUnitId;
        this.toUnitId = toUnitId;
        this.conversionFactor = conversionFactor;
    }

    public UnitConversion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getFromUnitId() {
        return fromUnitId;
    }

    public void setFromUnitId(String fromUnitId) {
        this.fromUnitId = fromUnitId;
    }

    public String getToUnitId() {
        return toUnitId;
    }

    public void setToUnitId(String toUnitId) {
        this.toUnitId = toUnitId;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }


}

