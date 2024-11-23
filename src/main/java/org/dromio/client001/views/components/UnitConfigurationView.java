package org.dromio.client001.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.data.Unit;
import org.dromio.client001.models.service.InventoryItemService;
import org.dromio.client001.models.service.InventoryService;
import org.dromio.client001.models.service.UnitService;
import org.dromio.client001.utility.AppColor;
import org.dromio.client001.utility.CustomNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitConfigurationView extends Dialog {

    private static final Logger log = LoggerFactory.getLogger(UnitConfigurationView.class);
    private TextField currentStockingUnitName;
    private TextField newUnitName;
    private NumberField currentStockingUnitQuantityEquivalent;
    private NumberField newUnitQuantityEquivalent;
    private Button cancelButton;
    private Button saveButton;

    InventoryItem inventoryItem;
    private final UnitService unitService;
    private final InventoryItemService inventoryItemService;

    public UnitConfigurationView(InventoryItem itemWhoseUnitIsTobeConfigured, InventoryItemService inventoryItemService, UnitService unitService) {

        inventoryItem = itemWhoseUnitIsTobeConfigured;
        this.unitService = unitService;
        this.inventoryItemService = inventoryItemService;

        initUi(inventoryItem);
        configureDialog();
        configureActionButtons();

        add(
                getContentWrapper()
        );

        open();
    }

    private Component getContentWrapper() {
        HorizontalLayout wrapper = new HorizontalLayout(currentStockingUnitQuantityEquivalent, currentStockingUnitName, new Text(" equals "), newUnitName, newUnitQuantityEquivalent, saveButton, cancelButton);
        wrapper.setSizeFull();
        wrapper.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-around")
                .set("align-items", "baseline");
        return wrapper;
    }

    private void configureActionButtons() {
        saveButton.addClickListener(event -> {
            try {
                runUnitConfigurationAlgorithm();
                CustomNotification.simpleSuccessNotification("Configurations Saved");
                close();
            }
            catch (IllegalArgumentException exception) {
                CustomNotification.simpleWarningNotification(exception.getMessage());
            }
            catch (Exception e) {
                log.error("Failed to add unit configuration : {}", e.getMessage());
                CustomNotification.simpleErrorNotification("Failed to save configurations");
                close();
            }

        });
        cancelButton.addClickListener(event -> {
            close();
            fireEvent(new CancelUnitConfigurationEvent(this));
        });
    }

    private void runUnitConfigurationAlgorithm() {
        // if the proposed unit relation introduce
        // a unit that is small compared
        // to the stocking unit
        // then the stocking unit for this item should be changed to be
        // that small unit
        // and all the quantities should be rewritten
        // in this new stocking unit
        int x = currentStockingUnitQuantityEquivalent.getValue().intValue();
        int y = newUnitQuantityEquivalent.getValue().intValue();
        if ( x == 0 || y == 0 || x < 0 || y < 0) {
            throw new IllegalArgumentException("Configuration values should not be negative or zero");
        }
        else if ( x != 1 && y != 1) {
            throw new IllegalArgumentException("At least one unit should be 1");
        }
        else if ( x < y ) {
            // This means stocking unit is a greater unit than the new unit
            // fetch the current item stocking unit
            Unit currentStockingUnit = unitService.getItemStockingUnit(this.inventoryItem.getInventoryItemId());
            // change it to false
            currentStockingUnit.setStockingUnit(false);
            // change its conversion factor
            currentStockingUnit.setConversionFactor(y);
            unitService.updateUnit(currentStockingUnit);
            // multiply current quantity with conversion factor
            inventoryItem.setQuantity( inventoryItem.getQuantity() * y );
            inventoryItemService.updateItemStockingDetails(inventoryItem);
            // add the new unit
            Unit newStockingUnit = new Unit();
            newStockingUnit.setName(newUnitName.getValue());
            newStockingUnit.setConversionFactor(1); // conversion factor of a stocking unit should always be one, because if we are to remove or sell items from stock in this unit then the actual removed unit is the actual effect on the inventory
            newStockingUnit.setStockingUnit(true);
            newStockingUnit.setInventoryItemId(this.inventoryItem.getInventoryItemId());
            unitService.addUnit(newStockingUnit);
            // set it as the stocking unit for this item
        }
        else if ( x > y ) {
            // This means stocking unit is less than new unit
            Unit unit = new Unit();
            unit.setName(newUnitName.getValue());
            unit.setConversionFactor(x);
            unit.setStockingUnit(false);
            unit.setInventoryItemId(this.inventoryItem.getInventoryItemId());
            unitService.addUnit(unit);
        }
        else {
            throw new IllegalArgumentException("The units appear to have the same value.");
        }
    }


    private void configureDialog() {
        setHeaderTitle("Unit Configuration for : " + inventoryItem.getItemName());
        setModal(true);
        setDraggable(true);
        setResizable(false);
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
    }

    private void initUi(InventoryItem item) {
        styleActionButtonsLayout();
        currentStockingUnitQuantityEquivalent = new NumberField("Value");
        currentStockingUnitName = new TextField("Current Stocking Unit");
        newUnitQuantityEquivalent = new NumberField("Value");
        newUnitName = new TextField("New Unit");

        currentStockingUnitName.setValue(unitService.getItemStockingUnitName(inventoryItem.getInventoryItemId()));
        currentStockingUnitName.setEnabled(false);
        newUnitName.setValue(" ");

        currentStockingUnitQuantityEquivalent.setValue(1d);
        newUnitQuantityEquivalent.setValue(1d);
    }

    private void styleActionButtonsLayout() {
        cancelButton = new Button("Cancel");
        saveButton = new Button("Save");
        cancelButton.getStyle().set("background-color", AppColor.CANCEL.toString()).set("color", "white");
        saveButton.getStyle().set("background-color", AppColor.CONFIRM.toString()).set("color", "white");
    }


    // Custom event classes for confirm and cancel actions
    public static abstract class UnitConfigurationViewEvent extends ComponentEvent<UnitConfigurationView> {
        protected UnitConfigurationViewEvent(UnitConfigurationView source) {
            super(source, false);
        }
    }

    public static class SaveUnitConfigurationEvent extends UnitConfigurationViewEvent {
        public SaveUnitConfigurationEvent(UnitConfigurationView source) {
            super(source);
        }
    }

    public static class CancelUnitConfigurationEvent extends UnitConfigurationViewEvent {
        public CancelUnitConfigurationEvent(UnitConfigurationView source) {
            super(source);
        }
    }

    // Listener registration methods for confirm and cancel actions
    public Registration addSaveConfigurationListener(ComponentEventListener<SaveUnitConfigurationEvent> listener) {
        return addListener(SaveUnitConfigurationEvent.class, listener);
    }

    public Registration addCancelConfigurationListener(ComponentEventListener<CancelUnitConfigurationEvent> listener) {
        return addListener(CancelUnitConfigurationEvent.class, listener);
    }
}
