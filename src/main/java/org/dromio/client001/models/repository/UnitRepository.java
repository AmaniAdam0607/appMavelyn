package org.dromio.client001.models.repository;

import org.dromio.client001.models.data.Unit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends CrudRepository<Unit, String> {

    Optional<Unit> findByName(String name);
    Optional<Unit> findByInventoryItemId(String id);
    List<Unit> findAllByInventoryItemId(String id);
}
