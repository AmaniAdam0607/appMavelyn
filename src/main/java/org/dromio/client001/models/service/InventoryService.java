package org.dromio.client001.models.service;

import jakarta.transaction.Transactional;
import org.dromio.client001.models.data.Inventory;
import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InventoryService {

    Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public List<InventoryItem> getInventoryItems(String inventoryName) {
        try {
            Inventory inventory = getInventoryFromDatabase(inventoryName);
            if (inventory == null) {
                return Collections.emptyList();
            } else {
                int ignoredValue = inventory.getItems().size(); // TODO study what this really does influence here, this triggers the fetching??
                return inventory.getItems();
            }
        }
        catch (Exception e) {
            logger.error("Error while fetching inventory items {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Transactional
    public void addItemsToAnInventory(String inventoryName, List<InventoryItem> inventoryItems) {
        Inventory inventory = inventoryRepository.findByName(inventoryName);
        if (inventory == null) {
            logger.error("Inventory with name {} does not exist", inventoryName);
        }
        else {
            inventory.getItems().addAll(inventoryItems);
            inventoryRepository.save(inventory);
            logger.info("InventoryItems added to inventory  with name {}", inventoryName);
        }
    }

    private boolean inventoryExists(String inventoryName) {
        return inventoryRepository.findByName(inventoryName) != null;
    }

    public void addInventory(String inventoryName) {
        if (inventoryExists(inventoryName)) {
            logger.error("Inventory with name {} already exists", inventoryName);
        }
        else {
            Inventory inventory = new Inventory();
            inventory.setName(inventoryName);
            inventoryRepository.save(inventory);
        }
    }

    public Inventory getInventoryFromDatabase(String inventoryName) {
        if (inventoryExists(inventoryName)) {
            logger.info("Inventory with name {} was found.", inventoryName);
            return inventoryRepository.findByName(inventoryName);
        }
        else {
            logger.info("Inventory with name {} was not found.", inventoryName);
            return null;
        }
    }
}
