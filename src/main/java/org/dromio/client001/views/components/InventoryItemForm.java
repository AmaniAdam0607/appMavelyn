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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryItemForm extends FormLayout {

    Logger logger = LoggerFactory.getLogger(InventoryItemForm.class);

    TextField itemName = new TextField("Item Name");
    NumberField quantity = new NumberField("Quantity");
    NumberField sellingPrice = new NumberField("Selling Price");
    NumberField buyingPrice = new NumberField("Buying Price");

    Button save = new Button("Save");
    Button disable = new Button("Disable");

    BeanValidationBinder<InventoryItem> binder = new BeanValidationBinder<>(InventoryItem.class);
    private InventoryItem inventoryItem;

    public InventoryItemForm() {
        bindFields();
        add(
                itemName,
                sellingPrice,
                buyingPrice,
                quantity,
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
    }

    private void bindFields() {
        binder.bind(itemName, InventoryItem::getItemName, InventoryItem::setItemName);
        binder.bind(sellingPrice, InventoryItem::getSellingPrice, InventoryItem::setSellingPrice);
        binder.bind(buyingPrice, InventoryItem::getBuyingPrice, InventoryItem::setBuyingPrice);
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
        disable.addClickListener( event -> fireEvent(new DisableEvent(this, inventoryItem)));

        return new HorizontalLayout(save, disable);
    }

    private void validateAndSave() {
        try {
            logger.info("Save clicked with inventory item {}", inventoryItem);
            binder.writeBean(inventoryItem);
            fireEvent(new SaveEvent(this, inventoryItem));
        } catch (ValidationException e) {
            logger.error("Error saving inventory item {}", e.getBeanValidationErrors());
        }
    }

    public static abstract class InventoryItemFormEvent extends ComponentEvent<InventoryItemForm> {
        private InventoryItem item;

        protected InventoryItemFormEvent(InventoryItemForm source, InventoryItem item) {
            super(source, false);
            this.item = item;
        }

        public InventoryItem getInventoryItem() {
            return item;
        }
    }

    public static class SaveEvent extends InventoryItemFormEvent {
        SaveEvent(InventoryItemForm source, InventoryItem item) {
            super(source, item);
        }
    }

    public static class DisableEvent extends InventoryItemFormEvent {
        DisableEvent(InventoryItemForm source, InventoryItem item) {
            super(source, item);
        }

    }

    public static class CloseEvent extends InventoryItemFormEvent {
        CloseEvent(InventoryItemForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DisableEvent> listener) {
        return addListener(DisableEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

}
