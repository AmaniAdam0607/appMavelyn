package org.dromio.client001.models.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class InventoryItem {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    String inventoryItemId;

    @NotNull(message = "Item name can not be empty")
    @NotEmpty(message = "Item name can not be empty")
    String itemName;

    @NotNull(message = "Item name can not be empty")
    @Min(value = 0, message = "Quantity can not be zero")
    Integer quantity;

    @NotNull(message = "Item name can not be empty")
    @Min(value = 0, message = "Selling price can not be zero")
    Double sellingPrice;

    @NotNull(message = "Item name can not be empty")
    @Min(value = 0, message = "Buying price can not be zero")
    Double buyingPrice;

    String unit;

    public InventoryItem(String inventoryItemId, String itemName, Integer quantity, Double sellingPrice, Double buyingPrice, String unit) {
        this.inventoryItemId = inventoryItemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.buyingPrice = buyingPrice;
        this.unit = unit;
    }

    public InventoryItem() {
    }

    public String getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(String inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public @NotNull(message = "Item name can not be empty") @NotEmpty(message = "Item name can not be empty") String getItemName() {
        return itemName;
    }

    public void setItemName(@NotNull(message = "Item name can not be empty") @NotEmpty(message = "Item name can not be empty") String itemName) {
        this.itemName = itemName;
    }

    public @NotNull(message = "Item name can not be empty") @Min(value = 0, message = "Quantity can not be zero") Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "Item name can not be empty") @Min(value = 0, message = "Quantity can not be zero") Integer quantity) {
        this.quantity = quantity;
    }

    public @NotNull(message = "Item name can not be empty") @Min(value = 0, message = "Selling price can not be zero") Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(@NotNull(message = "Item name can not be empty") @Min(value = 0, message = "Selling price can not be zero") Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public @NotNull(message = "Item name can not be empty") @Min(value = 0, message = "Buying price can not be zero") Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(@NotNull(message = "Item name can not be empty") @Min(value = 0, message = "Buying price can not be zero") Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
