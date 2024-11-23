package org.dromio.client001.models.data;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Inventory {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    private String inventoryId;

    private String name;

    public Inventory(String inventoryId, String name, List<InventoryItem> items) {
        this.inventoryId = inventoryId;
        this.name = name;
    }

    public Inventory() {
    }

    public Inventory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

}
