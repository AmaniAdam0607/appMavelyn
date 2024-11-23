package org.dromio.client001.models.service;

import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.data.Unit;
import org.dromio.client001.models.repository.InventoryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final UnitService unitService;
    Logger logger = LoggerFactory.getLogger(InventoryItemService.class);

    public InventoryItemService(InventoryItemRepository inventoryItemRepository, UnitService unitService) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.unitService = unitService;
    }

    public Integer getItemQuantity(String itemId) {
        // TODO add a more proper case handling for when the item does not exist in db
        Optional<InventoryItem> item = inventoryItemRepository.findById(itemId);

        if (item.isEmpty()) {
            logger.warn("Failed to find item while trying to fetch its quantity, item id of {} not found", itemId);
            return 0;
        }
        return item.get().getQuantity();
    }

    public void reduceItemQuantity(String inventoryItemId, Integer quantity) {
        Optional<InventoryItem> item =  inventoryItemRepository.findById(inventoryItemId);
        if (item.isPresent()) {
            InventoryItem inventoryItem1 = item.get();
            inventoryItem1.setQuantity(inventoryItem1.getQuantity() - quantity);
            inventoryItemRepository.save(inventoryItem1);
        }
        else {
            logger.info("Failed to decrease quantity of item with id of {}", inventoryItemId);
        }
    }

    public void increaseItemQuantity(String inventoryItemId, Integer quantity) {
        Optional<InventoryItem> item =  inventoryItemRepository.findById(inventoryItemId);
        if (item.isPresent()) {
            InventoryItem inventoryItem1 = item.get();
            inventoryItem1.setQuantity(inventoryItem1.getQuantity() + quantity);
            inventoryItemRepository.save(inventoryItem1);
        }
        else {
            logger.info("Failed to increase quantity of item with id of {}", inventoryItemId);
        }
    }

    public void createNewInventoryItem(InventoryItem inventoryItem, String unitName) {
        InventoryItem inventoryItem1 = inventoryItemRepository.save(inventoryItem);
        Unit unit = new Unit(unitName, true, inventoryItem1.getInventoryItemId(), 1);
        unitService.addUnit(unit);
    }

    @Transactional
    public List<InventoryItem> getAllItemsOfAnInventory(String inventoryId) {
        return inventoryItemRepository.findInventoryItemsByInventoryId(inventoryId);
    }

    /**
     * Updates the stocking details of an InventoryItem.
     *
     * @param inventoryItem the item to update
     */
    public void updateItemStockingDetails(InventoryItem inventoryItem) {
        inventoryItemRepository.save(inventoryItem);
    }

    /**
     * Fetches a map of inventory item IDs to names for quick lookup.
     *
     * @return a map with inventory IDs as keys and item names as values.
     */
    public Map<String, String> getAllItemNames() {
        return inventoryItemRepository.findAll().stream()
                .collect(Collectors.toMap(InventoryItem::getInventoryItemId, InventoryItem::getItemName));
    }
}
