package org.dromio.client001.models.data;

import org.dromio.client001.models.service.InventoryItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemsToSell {

    private final List<ItemToSell> selectedItems = new ArrayList<>();
    private final InventoryItemService inventoryItemService;
    private Logger logger = LoggerFactory.getLogger(ItemsToSell.class);

    public ItemsToSell(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }


    /**
     * If the quantity of the selected item is zero in stock throw an exception
     * If the item was already selected then:
     *              - if the selected quantity will cause the stock quantity to reduce to a negative then throws an exception
     *              - if all is well then update the quantity
     * If the item was not selected before then add it to items to select
     * */
    public void addItemsToSell(ItemToSell itemToSell, Integer quantityToAdd, Unit unit, Double sellingPrice) {
        Integer quantityInStock = inventoryItemService.getItemQuantity(itemToSell.getComparingId());
        Integer quantitiesThatWillBeDeductedFromStock = quantityToAdd * unit.getConversionFactor();
        logger.info("AddingItems :: Quantity in stock for {} is {}", itemToSell.getItemName(), quantityInStock);

        if(quantityInStock <= 0) {
            throw new IllegalArgumentException(itemToSell.getItemName() + " is out of stock");
        }
        ItemToSell existingItem = findItemByComparingId(itemToSell); // TODO write this code in a way that will not confuse me because currently if the item is found its quantity is updated by updating quantity of the reference that is returned here which means the variable created here points to the returned object in memory so changing it affects the item returned also. Its okay programmatically but confusing when I was checking the flow of this function.
        if (existingItem != null) {
            /*
             * Notice that "existingItem" now points to an item that is in itemsToSell
             * Changing anything on the "existingItem" variable propagates its changes to the item returned by findItemByComparingId(itemToSell)
             * this brings the desired effect but is NOT clear at first glance
             * God saving me from possible bugs.
             * */
            if ((quantityInStock - quantitiesThatWillBeDeductedFromStock) < 0) {
                throw new IllegalArgumentException("You can not select more than what is in stock for " + itemToSell.getItemName());
            }
            /*
             * At this point the following should hold true
             *           (a) Item is not out of stock
             *           (b) Selected items will not exceed the amount currently in stock
             * */
            existingItem.setSellingUnit(unit.getName());
            existingItem.setSelectedQuantity(quantityToAdd);
            existingItem.setUnitConversionFactor(unit.getConversionFactor());
            existingItem.setSoldWithPrice(sellingPrice);
        } else {
            selectedItems.add(itemToSell);
        }
    }


    /**
     * expectedOffset means what does the call expect invocation of this method to affect the quantity..if its two for example everytime this method is invoked 2 unit quantity wi; be added
     * If the quantity of the selected item is zero in stock throw an exception
     * If the item was already selected then:
     *              - if the selected quantity will cause the stock quantity to reduce to a negative then throws an exception
     *              - if all is well then update the quantity
     * If the item was not selected before then add it to items to select
     * */
    public void addItemToSell(ItemToSell itemToSell, Integer expectedOffset) {
            Integer quantityInStock = inventoryItemService.getItemQuantity(itemToSell.getComparingId());
            logger.info("Quantity in stock for {} is {}", itemToSell.getItemName(), quantityInStock);

            if(quantityInStock <= 0) {
                throw new IllegalArgumentException(itemToSell.getItemName() + " is out of stock");
            }
            ItemToSell existingItem = findItemByComparingId(itemToSell); // TODO write this code in a way that will not confuse me because currently if the item is found its quantity is updated by updating quantity of the reference that is returned here which means the variable created here points to the returned object in memory so changing it affects the item returned also. Its okay programmatically but confusing when I was checking the flow of this function.
            if (existingItem != null) {
                /*
                * Notice that "existingItem" now points to an item that is in itemsToSell
                * Changing anything on the "existingItem" variable propagates its changes to the item returned by findItemByComparingId(itemToSell)
                * this brings the desired effect but is NOT clear at first glance
                * God saving me from possible bugs.
                * */
                if ((quantityInStock - (existingItem.getSelectedQuantity() + expectedOffset)) < 0) {
                    throw new IllegalArgumentException("You can not select more than what is in stock for " + itemToSell.getItemName());
                }
                if(!existingItem.getSellingUnit().equals(itemToSell.getSellingUnit())) {
                    throw new IllegalArgumentException("Conflicting unit selection");
                }
                /*
                * At this point the following should hold true
                *           (a) Item is not out of stock
                *           (b) Selected items will not exceed the amount currently in stock
                * */
                existingItem.increaseQuantity(); // Increase quantity of the existing item
            } else {
                selectedItems.add(itemToSell);
            }
    }

    private ItemToSell findItemByComparingId(ItemToSell itemToSell) {
        for (ItemToSell item : selectedItems) {
            if (item.getComparingId().equals(itemToSell.getComparingId())) {
                return item; // Return the existing item if a match is found
            }
        }
        return null; // Return null if no match is found
    }

    public int getTotalPriceOfSelectedItems() {
        int totalAmount = 0;
        for (ItemToSell item : selectedItems) {
            totalAmount += item.getTotalPriceOfQuantitySelected();
        }
        return totalAmount;
    }

    public int numberOfSelectedItems() {
        return selectedItems.size();
    }

    public void clearSelectedItems() {
        selectedItems.clear();
    }

    public boolean isEmpty() {
        return selectedItems.isEmpty();
    }

    public List<ItemToSell> getSoldItems() {
        for (ItemToSell item : selectedItems) {
            inventoryItemService.reduceItemQuantity(item.getComparingId(), item.selectedQuantity * item.getUnitConversionFactor());
        }
        return this.selectedItems;
    }

    public List<ItemToSell> getSelectedItems() {
        return this.selectedItems;
    }
}
