package org.dromio.client001.models.repository;

import org.dromio.client001.models.data.Inventory;
import org.dromio.client001.models.data.InventoryItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends ListCrudRepository<Inventory, String> {

    Optional<Inventory> findByName(String name);

}
