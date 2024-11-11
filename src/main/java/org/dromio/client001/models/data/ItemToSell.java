package org.dromio.client001.models.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class ItemToSell {

    String id;
    InventoryItem inventoryItem;
    Integer selectedQuantity = 1;

    Logger logger = LoggerFactory.getLogger(ItemToSell.class);

    public ItemToSell(InventoryItem inventoryItem) {
        id = UUID.randomUUID().toString();
        this.inventoryItem = inventoryItem;
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
        //logger.info("Quantity for {} is ", selectedQuantity);
    }

    public Double getTotalPriceOfQuantitySelected() {
        return this.selectedQuantity * this.inventoryItem.getSellingPrice();
    }

    public String getItemName() {
        return this.inventoryItem.getItemName();
    }

    public Double getItemSellingPrice() {
        return this.inventoryItem.getSellingPrice();
    }

    public Double getItemBuyingPrice() {
        return this.inventoryItem.getBuyingPrice();
    }

    public String getSellingUnit() {
        return "unit";
    }

    public String getComparingId() {
        return this.inventoryItem.getInventoryItemId();
    }

}
