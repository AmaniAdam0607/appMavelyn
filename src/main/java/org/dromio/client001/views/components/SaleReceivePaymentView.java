package org.dromio.client001.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class SaleReceivePaymentView extends Dialog {

    private final String totalAmountSold;
    private HorizontalLayout actionButtonsLayout;
    private HorizontalLayout amountInfoLayout;
    private VerticalLayout paymentMethodLayout;

    private Button cancelButton;
    private Button confirmButton;

    public SaleReceivePaymentView(String totalAmountSold) {
        this.totalAmountSold = totalAmountSold;
        initUi();
        configureDialog();
        configureActionButtons();

        add(
                getContentWrapper()
        );

        open();
    }

    private Component getContentWrapper() {
        VerticalLayout wrapper = new VerticalLayout(amountInfoLayout, paymentMethodLayout,actionButtonsLayout);
        wrapper.setSizeFull();
        wrapper.getStyle().set("display", "flex");
        return wrapper;
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
        setHeaderTitle("Payment");
        setModal(true);
        setDraggable(true);
        setWidth("500px");
        setHeight("500px");
    }

    private void initUi() {
        amountInfoLayout = new HorizontalLayout();
        actionButtonsLayout = new HorizontalLayout();
        paymentMethodLayout = new VerticalLayout();

        styleAmountInfoLayout();
        styleActionButtonsLayout();
        stylePaymentMethodLayout();

    }

    private void stylePaymentMethodLayout() {
        HorizontalLayout questionLayout = new HorizontalLayout();
        HorizontalLayout referenceLayout = new HorizontalLayout();
        referenceLayout.setVisible(false);

        Checkbox paymentMethodQuestion = new Checkbox();
        paymentMethodQuestion.setLabel("Payment received in cash?");
        paymentMethodQuestion.setValue(true);
        paymentMethodQuestion.addValueChangeListener(event -> {
           referenceLayout.setVisible(!referenceLayout.isVisible());
        });

        paymentMethodQuestion.getStyle().set("display", "flex").set("flex-direction", "row-reverse").set("align-items", "center");

        Text referenceNumberLabel = new Text("Enter reference number : ");
        TextField referenceNumberField = new TextField();

        questionLayout.add(paymentMethodQuestion);
        questionLayout.setWidth("100%");

        referenceLayout.add(referenceNumberLabel, referenceNumberField);
        referenceLayout.setWidth("100%");

        paymentMethodLayout.add(paymentMethodQuestion, referenceLayout);
        paymentMethodLayout.setWidth("100%");
    }

    private void styleActionButtonsLayout() {
        cancelButton = new Button("Cancel");
        confirmButton = new Button("Confirm");
        // Style for action layout and its children
        actionButtonsLayout.add(cancelButton, confirmButton);
        actionButtonsLayout.getStyle().set("display", "flex");
        actionButtonsLayout.setWidth("100%");
        cancelButton.getStyle().set("flex-grow", "1");
        confirmButton.getStyle().set("flex-grow", "1");
        cancelButton.getStyle().set("background-color", "#f44336").set("color", "white");
        confirmButton.getStyle().set("background-color", "#4CAF50").set("color", "white");
    }

    private void styleAmountInfoLayout() {
        // Style for amount info layout and its children
        Div label = new Div(new Text("Total amount"));
        label.getStyle().set("font-weight", "bold");

        Div amountText = new Div(new Text(totalAmountSold));
        amountText.getStyle().set("color", "green");

        amountInfoLayout.add(label, amountText);
        amountInfoLayout.getStyle().set("display", "flex").set("border-bottom", "1px solid");
        amountInfoLayout.setWidth("100%");
        label.getStyle().set("flex-grow", "1");
        amountText.getStyle().set("flex-grow", "1");
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
