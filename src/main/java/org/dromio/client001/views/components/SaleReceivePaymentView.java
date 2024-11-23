package org.dromio.client001.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.dromio.client001.utility.AppColor;

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
        wrapper.getStyle().set("display", "flex").set("justify-content", "space-around");
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

        HorizontalLayout paymentMethodQuestion = new HorizontalLayout();
        Text methodInfo = new Text("Payment was receieved in cash?");
        Button paymentToggleButton = new Button("Yes");
        paymentToggleButton.addClickListener(event -> {
            // if the button is currently displaying "Yes" and is clicked
            // then change the displaying text to "No" and show
            // the text field to enter reference number
            // Otherwise, if the button is currently "No" and is clicked
            // return to the normal behaviour, hide the reference no text field
            if (paymentToggleButton.getText().equals("Yes")) {
                paymentToggleButton.setText("No");
                referenceLayout.setVisible(!referenceLayout.isVisible());
            }
            else {
                   paymentToggleButton.setText("Yes");
                   referenceLayout.setVisible(!referenceLayout.isVisible());
            }
        });
        paymentMethodQuestion.add(methodInfo, paymentToggleButton);
        paymentMethodQuestion.setWidth("100%");
        paymentMethodQuestion.getStyle().set("display", "flex").set("justify-content", "space-between").set("align-items", "center");

        Text referenceNumberLabel = new Text("Enter reference number : ");
        TextField referenceNumberField = new TextField();

        questionLayout.add(paymentMethodQuestion);
        questionLayout.setWidth("100%");

        referenceLayout.add(referenceNumberLabel, referenceNumberField);
        referenceLayout.setWidth("100%");
        referenceLayout.getStyle().set("display", "flex").set("justify-content", "space-between").set("align-items", "center");

        paymentMethodLayout.add(paymentMethodQuestion, referenceLayout);
        paymentMethodLayout.setWidth("100%");
        paymentMethodLayout.setPadding(false);
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
        cancelButton.getStyle().set("background-color", AppColor.CANCEL.toString()).set("color", "white");
        confirmButton.getStyle().set("background-color", AppColor.CONFIRM.toString()).set("color", "white");
    }

    private void styleAmountInfoLayout() {
        // Style for amount info layout and its children
        Div label = new Div(new Text("Total amount"));
        label.getStyle().set("font-weight", "bold");

        Div amountText = new Div(new Text(totalAmountSold));
        amountText.getStyle().set("color", "green");

        amountInfoLayout.add(label, amountText);
        amountInfoLayout.getStyle().set("display", "flex").set("border-bottom", "1px solid").set("justify-content", "space-between");
        amountInfoLayout.setWidth("100%");
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
