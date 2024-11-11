package org.dromio.client001.views.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class SaleReceivePaymentView extends Dialog {

    private final String totalAmountSold;
    private HorizontalLayout actionButtonsLayout;
    private HorizontalLayout amountInfoLayout;

    private Button cancelButton;
    private Button confirmButton;

    public SaleReceivePaymentView(String totalAmountSold) {
        this.totalAmountSold = totalAmountSold;
        initUi();
        configureDialog();
        configureActionButtons();

        // Add content to the dialog
        add(new VerticalLayout(amountInfoLayout, actionButtonsLayout));

        // Automatically open the dialog when initialized
        open();
    }

    private void configureActionButtons() {
        confirmButton.addClickListener(event -> {
            close();
            fireEvent(new ConfirmPaymentEvent(this));
        });
        cancelButton.addClickListener(event -> {
            close();
            fireEvent(new CancelPaymentEvent(this));
        });
    }

    private void configureDialog() {
        setHeaderTitle("Final Step!");
        setModal(true);
        setDraggable(true);  // Optional: Allow dragging the dialog
        setResizable(true);  // Optional: Allow resizing the dialog
    }

    private void initUi() {
        amountInfoLayout = new HorizontalLayout();
        actionButtonsLayout = new HorizontalLayout();

        // Wrap the "Total amount" text in a Div for mutable styling
        Div label = new Div(new Text("Total amount"));
        label.getStyle().set("font-weight", "bold").set("margin-right", "10px");

        Div amountText = new Div(new Text(totalAmountSold));
        amountText.getStyle().set("color", "green"); // Example: change color for amount

        cancelButton = new Button("Cancel");
        confirmButton = new Button("Confirm");

        // Add theme variants for buttons to make them visually appealing
        cancelButton.getStyle().set("background-color", "#f44336").set("color", "white");
        confirmButton.getStyle().set("background-color", "#4CAF50").set("color", "white");

        // Set up layouts for amount information and action buttons
        amountInfoLayout.add(label, amountText);
        actionButtonsLayout.add(cancelButton, confirmButton);
    }

    // Custom event classes for confirm and cancel actions
    public static abstract class SaleReceivePaymentViewEvent extends ComponentEvent<SaleReceivePaymentView> {
        protected SaleReceivePaymentViewEvent(SaleReceivePaymentView source) {
            super(source, false);
        }
    }

    public static class ConfirmPaymentEvent extends SaleReceivePaymentViewEvent {
        public ConfirmPaymentEvent(SaleReceivePaymentView source) {
            super(source);
        }
    }

    public static class CancelPaymentEvent extends SaleReceivePaymentViewEvent {
        public CancelPaymentEvent(SaleReceivePaymentView source) {
            super(source);
        }
    }

    // Listener registration methods for confirm and cancel actions
    public Registration addPaymentConfirmedListener(ComponentEventListener<ConfirmPaymentEvent> listener) {
        return addListener(ConfirmPaymentEvent.class, listener);
    }

    public Registration addPaymentCanceledListener(ComponentEventListener<CancelPaymentEvent> listener) {
        return addListener(CancelPaymentEvent.class, listener);
    }
}
