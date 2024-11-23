package org.dromio.client001.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.shared.Registration;
import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.data.Unit;
import org.dromio.client001.models.service.InventoryItemService;
import org.dromio.client001.models.service.UnitService;
import org.dromio.client001.utility.AppColor;
import org.dromio.client001.utility.CustomNotification;

public class ReceiveItemView extends Dialog {

    private final InventoryItemService inventoryItemService;
    private final UnitService unitService;
    private ComboBox<Unit> unitComboBox;
    private NumberField quantityField;
    private Button cancelButton;
    private Button receiveButton;
    private final InventoryItem item;

    public ReceiveItemView(InventoryItem inventoryItem, InventoryItemService inventoryItemService, UnitService unitService) {
        this.inventoryItemService = inventoryItemService;
        this.unitService = unitService;
        this.item = inventoryItem;
        initUi();
        configureDialog();
        configureActionButtons();

        add(
                getContentWrapper()
        );

        open();
    }

    private Component getContentWrapper() {
        VerticalLayout wrapper = new VerticalLayout(quantityField, unitComboBox, receiveButton, cancelButton);
        wrapper.setSizeFull();
        wrapper.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-around")
        ;
        return wrapper;
    }

    private void configureActionButtons() {
        receiveButton.addClickListener(event -> {
            try {
                int quantityReceived = 0;
                if (quantityField.getValue() != null) {
                    quantityReceived = quantityField.getValue().intValue();
                    if (quantityReceived == 0) {
                        CustomNotification.simpleWarningNotification("Receiving 0 quantity for " + this.item.getItemName() + "?, consider cancelling this process.");
                    }
                    else if (unitComboBox.getValue() == null) {
                        CustomNotification.simpleWarningNotification("Please select the selling unit");
                    }
                    else {
                        inventoryItemService.increaseItemQuantity(this.item.getInventoryItemId(), quantityReceived * unitComboBox.getValue().getConversionFactor());
                        close();
                        fireEvent(new ReceiveEvent(this));
                    }
                }
                else {
                    //this.selectedQuantityField.setErrorMessage("Please this field");
                    CustomNotification.simpleWarningNotification("Please write the quantity received");
                }
            }
            catch (IllegalArgumentException e) {
                CustomNotification.simpleWarningNotification(e.getMessage());
            }
        });
        cancelButton.addClickListener(event -> {
            close();
            fireEvent(new CancelEvent(this));
        });
    }

    private void configureDialog() {
        setHeaderTitle("Receiving : " + this.item.getItemName());
        setModal(true);
        setDraggable(true);
        setResizable(false);
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
    }

    private void initUi() {
        styleActionButtonsLayout();
        quantityField = new NumberField("Quantity Received");
        unitComboBox = new ComboBox<>("Receiving Unit");

        unitComboBox.setItems(unitService.getThisItemUnits(this.item.getInventoryItemId()));
        unitComboBox.setItemLabelGenerator(Unit::getName);

    }

    private void styleActionButtonsLayout() {
        cancelButton = new Button("Cancel");
        receiveButton = new Button("Receive");
        cancelButton.getStyle().set("background-color", AppColor.CANCEL.toString()).set("color", "white");
        receiveButton.getStyle().set("background-color", AppColor.CONFIRM.toString()).set("color", "white");
    }


    // Custom event classes for confirm and cancel actions
    public static abstract class ReceiveItemViewEvent extends ComponentEvent<ReceiveItemView> {
        protected ReceiveItemViewEvent(ReceiveItemView source) {
            super(source, false);
        }
    }

    public static class ReceiveEvent extends ReceiveItemViewEvent {
        public ReceiveEvent(ReceiveItemView source) {
            super(source);
        }
    }

    public static class CancelEvent extends ReceiveItemViewEvent {
        public CancelEvent(ReceiveItemView source) {
            super(source);
        }
    }

    // Listener registration methods for confirm and cancel actions
    public Registration addItemReceiveEventListener(ComponentEventListener<ReceiveEvent> listener) {
        return addListener(ReceiveEvent.class, listener);
    }

    public Registration addItemReceiveCanceledListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }
}
