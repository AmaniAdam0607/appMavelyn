package org.dromio.client001.models.repository;

import org.dromio.client001.models.data.InventoryItem;
import org.springframework.data.repository.ListCrudRepository;

public interface InventoryItemRepository extends ListCrudRepository<InventoryItem, String>{ }
