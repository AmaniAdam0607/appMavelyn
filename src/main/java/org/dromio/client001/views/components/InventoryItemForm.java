package org.dromio.client001.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.shared.Registration;
import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.service.InventoryService;
import org.dromio.client001.models.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InventoryItemForm extends FormLayout {

    private final UnitService unitService;
    Logger logger = LoggerFactory.getLogger(InventoryItemForm.class);

    private final TextField itemName = new TextField("Item Name");
    private final NumberField quantity = new NumberField("Quantity");
    private final NumberField sellingPrice = new NumberField("Selling Price");
    private final NumberField buyingPrice = new NumberField("Buying Price");
    private final TextField stockingUnit = new TextField("Stocking Unit");
    private String unit;

    Button save = new Button("Save");
    Button disable = new Button("Disable");
    Button clearForm = new Button("Clear");

    BeanValidationBinder<InventoryItem> binder = new BeanValidationBinder<>(InventoryItem.class);
    BeanValidationBinder<InventoryItemForm> unitBinder = new BeanValidationBinder<>(InventoryItemForm.class);
    private InventoryItem inventoryItem;

    public InventoryItemForm(UnitService unitService) {
        this.unitService = unitService;
        bindFields();
        add(
                itemName,
                sellingPrice,
                buyingPrice,
                quantity,
                stockingUnit,
                createButtonLayout()
        );
    }

    public void setInventoryItem(InventoryItem item) {
        this.inventoryItem = item;
        if ( this.inventoryItem.getInventoryItemId() != null ) {
            quantity.setEnabled(false);
            save.setText("Edit");
            disable.setEnabled(true);
        }
        else {
            quantity.setEnabled(true);
            save.setText("Save");
            disable.setEnabled(false);
        }
        binder.readBean(this.inventoryItem);
        unitBinder.readBean(this);
    }

    private void bindFields() {
        binder.bind(itemName, InventoryItem::getItemName, InventoryItem::setItemName);
        binder.bind(sellingPrice, InventoryItem::getSellingPrice, InventoryItem::setSellingPrice);
        binder.bind(buyingPrice, InventoryItem::getBuyingPrice, InventoryItem::setBuyingPrice);
        unitBinder.bind(stockingUnit, this::getUnit, this::setUnit);
        binder.forField(quantity)
                .withConverter(new Converter<Double, Integer>() {
                    @Override
                    public Result<Integer> convertToModel(Double fieldValue, ValueContext context) {
                        if (fieldValue == null) {
                            return Result.ok(null);
                        }
                        return Result.ok(fieldValue.intValue());
                    }

                    @Override
                    public Double convertToPresentation(Integer modelValue, ValueContext context) {
                        return modelValue == null ? null : modelValue.doubleValue();
                    }
                })
                .bind(InventoryItem::getQuantity, InventoryItem::setQuantity);
        binder.setBean(inventoryItem);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        disable.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickShortcut(Key.ENTER);

        save.addClickListener( event -> validateAndSave());
        disable.addClickListener( event -> fireEvent(new DisableEvent(this, inventoryItem, unit)));
        clearForm.addClickListener( event -> {
            setInventoryItem(new InventoryItem());
            fireEvent( new ClearEvent(this));
        }
        );

        return new HorizontalLayout(save, disable, clearForm);
    }

    private void validateAndSave() {
        try {
            logger.info("Save clicked with inventory item {}", inventoryItem);
            binder.writeBean(inventoryItem);
            unitBinder.writeBean(this);
            fireEvent(new SaveEvent(this, inventoryItem, unit));
            setInventoryItem(new InventoryItem());
        } catch (ValidationException e) {
            logger.error("Error saving inventory item {}", e.getBeanValidationErrors());
        }
        catch (IllegalArgumentException ex) {
            logger.error("Error while saving item {}", ex.getMessage());
        }
    }

    public static abstract class InventoryItemFormEvent extends ComponentEvent<InventoryItemForm> {
        private InventoryItem item;
        private String unit;

        protected InventoryItemFormEvent(InventoryItemForm source, InventoryItem item, String unit) {
            super(source, false);
            this.item = item;
            this.unit = unit;
        }

        public InventoryItem getInventoryItem() {
            return item;
        }

        public String getUnit() {
            return unit;
        }
    }

    public static class SaveEvent extends InventoryItemFormEvent {
        SaveEvent(InventoryItemForm source, InventoryItem item, String unit) {
            super(source, item, unit);
        }
    }

    public static class DisableEvent extends InventoryItemFormEvent {
        DisableEvent(InventoryItemForm source, InventoryItem item, String unit) {
            super(source, item, unit);
        }
    }

    public static class ClearEvent extends InventoryItemFormEvent {
        ClearEvent(InventoryItemForm source) {
            super(source, null, null);
        }
    }

    public Registration addDisableListener(ComponentEventListener<DisableEvent> listener) {
        return addListener(DisableEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addClearListener(ComponentEventListener<ClearEvent> listener) {
        return addListener(ClearEvent.class, listener);
    }


    public void setUnit(InventoryItemForm inventoryItemForm, String s) {
        unit = s;
    }

    public String getUnit(InventoryItemForm inventoryItemForm) {
        if (inventoryItem.getInventoryItemId() == null) {
            return "";
        }
        return unitService.getItemStockingUnitName(inventoryItem.getInventoryItemId());
    }

}
