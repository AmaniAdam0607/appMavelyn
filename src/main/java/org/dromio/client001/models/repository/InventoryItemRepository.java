package org.dromio.client001.models.repository;

import org.dromio.client001.models.data.InventoryItem;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface InventoryItemRepository extends ListCrudRepository<InventoryItem, String>{

    List<InventoryItem> findInventoryItemsByInventoryId(String inventoryId);

}
