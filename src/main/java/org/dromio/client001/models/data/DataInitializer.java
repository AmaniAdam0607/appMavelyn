package org.dromio.client001.models.data;

import org.dromio.client001.models.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final InventoryService inventoryService;

    public DataInitializer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public void run(String... args) {
        try {
            inventoryService.addInventory("Stationary Inventory");
            inventoryService.addItemsToAnInventory("Stationary Inventory", createSampleInventoryItems());
            logger.info("10 InventoryItems added successfully to 'Stationary Inventory'.");
        }
        catch (Throwable e) {
            logger.error("While initializing data {}", e.getMessage());
        }
    }

    private List<InventoryItem> createSampleInventoryItems() {
        List<InventoryItem> inventoryItems = new ArrayList<>();

        // List of common stationery items
        String[] itemNames = {
                "Exercise Book", "Pen", "Pencil", "Ruler", "Eraser",
                "Sharpener", "Notebook", "Glue Stick", "Marker", "Calculator"
        };

        for (int i = 0; i < itemNames.length; i++) {
            InventoryItem item = new InventoryItem(
                    null,                    // inventoryItemId, auto-generated
                    itemNames[i],                 // product association
                    10 + i * 5,              // quantity, example quantity
                    500 + i * 100d,           // selling price in TZS
                    400 + i * 80d, // buying price in TZS
                    "itemUnit"
            );
            inventoryItems.add(item);
        }
        return inventoryItems;
    }
}
