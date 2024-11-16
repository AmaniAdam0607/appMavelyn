package org.dromio.client001.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.dromio.client001.models.data.ItemToSell;
import org.dromio.client001.models.data.ItemsToSell;
import org.dromio.client001.utility.AppColor;
import org.dromio.client001.utility.CustomNotification;

public class SingleItemSelectorView extends Dialog {

    private final ItemsToSell itemsToSell;
    private ItemToSell item;
    private TextField selectedUnitField;
    private NumberField selectedQuantityField;
    private NumberField soldWithPriceField;
    private Button cancelButton;
    private Button saveButton;

    public SingleItemSelectorView(ItemsToSell itemsToSell, ItemToSell item) {
        this.itemsToSell = itemsToSell;
        this.item = item;
        // When this view is created populate it with
        // The values from the item which was clicked
        // The changes should be validated when it is saved
        // Changes may include unit, quantity and price.
        initUi(this.item);
        configureDialog();
        configureActionButtons();

        add(
                getContentWrapper()
        );

        open();
    }

    private Component getContentWrapper() {
        HorizontalLayout wrapper = new HorizontalLayout(selectedQuantityField,selectedUnitField, soldWithPriceField, saveButton, cancelButton);
        wrapper.setSizeFull();
        wrapper.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-around")
                .set("align-items", "baseline")
        ;
        return wrapper;
    }

    private void configureActionButtons() {
        saveButton.addClickListener(event -> {
            try {
                Integer quantityToAdd = 0;
                if (selectedQuantityField.getValue() != null) {
                    quantityToAdd = selectedQuantityField.getValue().intValue();
                    if (quantityToAdd == 0) {
                        CustomNotification.simpleWarningNotification("Selecting 0 quantity for " + this.item.getItemName() + "?, Consider clearing it from selections.");
                    }
                    else {
                        itemsToSell.addItemsToSell(this.item, quantityToAdd);
                        close();
                        fireEvent(new SaveSelectionEvent(this));
                    }
                }
                else {
                    //this.selectedQuantityField.setErrorMessage("Please this field");
                    CustomNotification.simpleWarningNotification("Please write the quantity you want to sell");
                }
            }
            catch (IllegalArgumentException e) {
                CustomNotification.simpleWarningNotification(e.getMessage());
            }
        });
        cancelButton.addClickListener(event -> {
            close();
            fireEvent(new CancelSelectionEvent(this));
        });
    }

    private void configureDialog() {
        setHeaderTitle("Selling : " + this.item.getItemName());
        setModal(true);
        setDraggable(true);
        setResizable(false);
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
    }

    private void initUi(ItemToSell item) {
        // TODO the data types for quantities and money needs to be revised across the whole project
        styleActionButtonsLayout();
        selectedQuantityField = new NumberField("Quantity");
        selectedUnitField = new TextField("Unit");
        soldWithPriceField = new NumberField("Price");

        selectedUnitField.setValue(item.getSellingUnit());
        selectedQuantityField.setValue(item.getSelectedQuantity().doubleValue());
        soldWithPriceField.setValue(item.getItemSellingPrice());

    }

    private void styleActionButtonsLayout() {
        cancelButton = new Button("Cancel");
        saveButton = new Button("Save");
        cancelButton.getStyle().set("background-color", AppColor.CANCEL.toString()).set("color", "white");
        saveButton.getStyle().set("background-color", AppColor.CONFIRM.toString()).set("color", "white");
    }


    // Custom event classes for confirm and cancel actions
    public static abstract class SingleItemSelectorViewEvent extends ComponentEvent<SingleItemSelectorView> {
        protected SingleItemSelectorViewEvent(SingleItemSelectorView source) {
            super(source, false);
        }
    }

    public static class SaveSelectionEvent extends SingleItemSelectorViewEvent {
        public SaveSelectionEvent(SingleItemSelectorView source) {
            super(source);
        }
    }

    public static class CancelSelectionEvent extends SingleItemSelectorViewEvent {
        public CancelSelectionEvent(SingleItemSelectorView source) {
            super(source);
        }
    }

    // Listener registration methods for confirm and cancel actions
    public Registration addSaveSelectionConfirmedListener(ComponentEventListener<SaveSelectionEvent> listener) {
        return addListener(SaveSelectionEvent.class, listener);
    }

    public Registration addCancelSelectionCanceledListener(ComponentEventListener<CancelSelectionEvent> listener) {
        return addListener(CancelSelectionEvent.class, listener);
    }
}
