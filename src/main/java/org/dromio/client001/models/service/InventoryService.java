package org.dromio.client001.models.service;

import jakarta.transaction.Transactional;
import org.dromio.client001.models.data.Inventory;
import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.repository.InventoryRepository;
import org.dromio.client001.utility.CustomNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;
    private final InventoryItemService inventoryItemService;

    public InventoryService(InventoryRepository inventoryRepository, InventoryItemService inventoryItemService) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryItemService = inventoryItemService;
    }

    @Transactional
    public List<InventoryItem> getInventoryItems(String name) {
        Optional<Inventory> inventory = inventoryRepository.findByName(name);
        if (inventory.isPresent()) {
            String inventoryId = inventory.get().getInventoryId();
            return inventoryItemService.getAllItemsOfAnInventory(inventoryId);
        }
        return Collections.emptyList();
    }

    @Transactional
    public void addItemsToAnInventoryForTesting(String inventoryName, List<InventoryItem> inventoryItems) {
        // This is called for testing because notice how unit is inserted here, hidden from the caller
        Optional<Inventory> inventory = inventoryRepository.findByName(inventoryName);
        if (inventory.isPresent()) {
            String inventoryId = inventory.get().getInventoryId();
            for (InventoryItem inventoryItem : inventoryItems) {
                inventoryItem.setInventoryId(inventoryId);
                inventoryItemService.createNewInventoryItem(inventoryItem, "Unit");
            }
        }
    }

    private boolean inventoryExists(String inventoryName) {
        return inventoryRepository.findByName(inventoryName).isPresent();
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
            return inventoryRepository.findByName(inventoryName).get();
        }
        else {
            logger.info("Inventory with name {} was not found.", inventoryName);
            return null;
        }
    }

    public void addItemToInventory(String inventoryName, InventoryItem inventoryItem, String itemUnit) {
        // This is called for testing because notice how unit is inserted here, hidden from the caller
        Optional<Inventory> inventory = inventoryRepository.findByName(inventoryName);
        if (inventory.isPresent()) {
            String inventoryId = inventory.get().getInventoryId();
            inventoryItem.setInventoryId(inventoryId);
            inventoryItemService.createNewInventoryItem(inventoryItem, itemUnit);
        }
    }
}
