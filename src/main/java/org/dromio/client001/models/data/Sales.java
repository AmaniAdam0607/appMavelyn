package org.dromio.client001.models.data;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.dromio.client001.utility.CustomDateTime;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class Sales {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private String saleId;

    private String itemId;

    private String soldWithUnit;
    //String userId;
    private Integer quantitySold;
    private Double soldWithPrice;
    private Double totalPrice;
    private LocalDateTime soldAtTime;

    public Sales(String saleId, String itemId, String soldWithUnit, Integer quantitySold, Double soldWithPrice, Double totalPrice, LocalDateTime soldAtTime) {
        this.saleId = saleId;
        this.itemId = itemId;
        this.soldWithUnit = soldWithUnit;
        this.quantitySold = quantitySold;
        this.soldWithPrice = soldWithPrice;
        this.totalPrice = totalPrice;
        this.soldAtTime = soldAtTime;
    }

    public Sales() {
    }

    /**
     * When creating a sale pass the id of the inventory item with it, this is modeled as itemId and is used to fetch the details of the inventory item when needed
     * */
    public Sales(String itemId, String soldWithUnit, Integer quantitySold, Double soldWithPrice) {
        this.itemId = itemId;
        this.soldWithUnit = soldWithUnit;
        this.quantitySold = quantitySold;
        this.soldWithPrice = soldWithPrice;
        this.soldAtTime = LocalDateTime.now();
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSoldWithUnit() {
        return soldWithUnit;
    }

    public void setSoldWithUnit(String soldWithUnit) {
        this.soldWithUnit = soldWithUnit;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Double getSoldWithPrice() {
        return soldWithPrice;
    }

    public void setSoldWithPrice(Double soldWithPrice) {
        this.soldWithPrice = soldWithPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getSoldAtTime() {
        return soldAtTime;
    }

    public void setSoldAtTime(LocalDateTime soldAtTime) {
        this.soldAtTime = soldAtTime;
    }

    public String getTimeInTimePassed() {
        return CustomDateTime.getTimeLapse(this.soldAtTime);
    }

    public String getNameOfItem(Map<String, String> itemNameMap) {
        return itemNameMap.getOrDefault(this.itemId, "Unknown Item");
    }

    public Double getTotalSingleSalePrice() {
        return this.quantitySold * this.soldWithPrice;
    }

}
