package org.dromio.client001.models.data;

import org.dromio.client001.models.service.InventoryService;
import org.dromio.client001.models.service.SettingService;
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
    private final SettingService settingService;
    private final boolean LOAD_TEST_ITEM = false;

    public DataInitializer(InventoryService inventoryService, SettingService settingService) {
        this.inventoryService = inventoryService;
        this.settingService = settingService;
    }

    @Override
    public void run(String... args) {
        try {
            initializeAppSettings();
            initializeInventories();
            if (LOAD_TEST_ITEM) {
                inventoryService.addItemsToAnInventoryForTesting("Stationary Inventory", createSampleInventoryItems());
                logger.info("10 InventoryItems added successfully to 'Stationary Inventory'.");
            }
            else {
                logger.info("Inventories added but no test data was initialized");
            }
        }
        catch (Throwable e) {
            logger.error("While initializing data {}", e.getMessage());
        }
    }

    private void initializeInventories() {
        inventoryService.addInventory("Stationary Inventory");
        inventoryService.addInventory("Pharmacy Inventory");
    }

    private void initializeAppSettings() {
        settingService.addSetting("Unit Configurations", false);
        settingService.addSetting("Receive Item", false);
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
                    400 + i * 80d // buying price in TZS
            );
            inventoryItems.add(item);
        }
        return inventoryItems;
    }
}
