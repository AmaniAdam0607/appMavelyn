package org.dromio.client001.models.service;

import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.data.Unit;
import org.dromio.client001.models.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Unit addUnit(Unit unit) {
        return unitRepository.save(unit);
    }

    public String getItemStockingUnitName(String inventoryItemId) {
        List<Unit> units = unitRepository.findAllByInventoryItemId(inventoryItemId);
        if (!units.isEmpty()) {
            for (Unit unit : units) {
                if (unit.isStockingUnit()) {
                    return unit.getName();
                }
            }
        }
        return "unit";
    }

    public Unit getItemStockingUnit(String inventoryItemId) {
        List<Unit> units = unitRepository.findAllByInventoryItemId(inventoryItemId);
        if (!units.isEmpty()) {
            for (Unit unit : units) {
                if (unit.isStockingUnit()) {
                    return unit;
                }
            }
        }
        return new Unit();
    }

    public void updateUnit(Unit currentStockingUnit) {
        // this is just a wrapper to differentiate update from add usages
        addUnit(currentStockingUnit);
    }

    public List<Unit> getThisItemUnits(String inventoryItemId) {
        List<Unit> units = unitRepository.findAllByInventoryItemId(inventoryItemId);
        if (!units.isEmpty()) {
            return units;
        }
        return Collections.emptyList();
    }
}
