package org.dromio.client001.models.repository;

import org.dromio.client001.models.data.Inventory;
import org.dromio.client001.models.data.InventoryItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends ListCrudRepository<Inventory, String> {

    @Query("SELECT i.items FROM Inventory i WHERE i.inventoryId = :inventoryId")
    List<InventoryItem> findItemsByInventoryId(@Param("inventoryId") String inventoryId);
    Inventory findByName(String name);

}
