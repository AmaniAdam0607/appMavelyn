package org.dromio.client001.models.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Unit {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    private String id;

    private String name;

    private boolean isStockingUnit;

    private String inventoryItemId;

    private Integer conversionFactor;

    public boolean isStockingUnit() {
        return isStockingUnit;
    }

    public void setStockingUnit(boolean stockingUnit) {
        isStockingUnit = stockingUnit;
    }

    public String getInventoryItemId() {
        return inventoryItemId;
    }

    public Integer getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(Integer conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public void setInventoryItemId(String inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public Unit(String name) {
        this.name = name;
    }

    public Unit(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Unit() {
    }

    public Unit(String id, String name, boolean isStockingUnit, String inventoryItemId, Integer conversionFactor) {
        this.id = id;
        this.name = name;
        this.isStockingUnit = isStockingUnit;
        this.inventoryItemId = inventoryItemId;
        this.conversionFactor = conversionFactor;
    }

    public Unit(String name, boolean isStockingUnit, String inventoryItemId, Integer conversionFactor) {
        this.name = name;
        this.isStockingUnit = isStockingUnit;
        this.inventoryItemId = inventoryItemId;
        this.conversionFactor = conversionFactor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
