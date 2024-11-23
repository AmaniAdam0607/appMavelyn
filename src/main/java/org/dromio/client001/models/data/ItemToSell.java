package org.dromio.client001.models.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ItemToSell {

    String id;
    InventoryItem inventoryItem;
    Integer selectedQuantity = 1;
    String sellingUnit;
    Integer unitConversionFactor = 1;
    Double soldWithPrice;
    Logger logger = LoggerFactory.getLogger(ItemToSell.class);

    public Integer getUnitConversionFactor() {
        return unitConversionFactor;
    }

    public Double getSoldWithPrice() {
        return soldWithPrice;
    }

    public void setSoldWithPrice(Double soldWithPrice) {
        this.soldWithPrice = soldWithPrice;
    }

    public void setUnitConversionFactor(Integer unitConversionFactor) {
        this.unitConversionFactor = unitConversionFactor;
    }

    public ItemToSell(InventoryItem inventoryItem, String sellingUnit) {
        id = UUID.randomUUID().toString();
        this.inventoryItem = inventoryItem;
        this.sellingUnit = sellingUnit;
        this.setSoldWithPrice(inventoryItem.getSellingPrice());
    }

    public ItemToSell(String id, InventoryItem inventoryItem, Integer selectedQuantity, Logger logger) {
        this.id = id;
        this.inventoryItem = inventoryItem;
        this.selectedQuantity = selectedQuantity;
        this.logger = logger;
    }

    public ItemToSell() {
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public void increaseQuantity() {
        selectedQuantity += 1;
    }

    public Double getTotalPriceOfQuantitySelected() {
        return this.selectedQuantity * this.soldWithPrice;
    }

    public String getItemName() {
        return this.inventoryItem.getItemName();
    }

    public Double getItemSellingPrice() {
        return this.soldWithPrice;
    }

    public Double getItemBuyingPrice() {
        return this.inventoryItem.getBuyingPrice();
    }

    public String getSellingUnit() {
        return sellingUnit;
    }

    public String getComparingId() {
        return this.inventoryItem.getInventoryItemId();
    }

    public void setSellingUnit(String unit) {
        this.sellingUnit = unit;
    }
}
