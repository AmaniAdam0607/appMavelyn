package org.dromio.client001.models.service;

import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.repository.InventoryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    Logger logger = LoggerFactory.getLogger(InventoryItemService.class);

    public InventoryItemService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
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
            logger.info("Failed to update quantity of item with id of {}", inventoryItemId);
        }
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
