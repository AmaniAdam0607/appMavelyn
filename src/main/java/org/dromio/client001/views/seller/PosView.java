package org.dromio.client001.views.seller;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.data.ItemToSell;
import org.dromio.client001.models.data.ItemsToSell;
import org.dromio.client001.models.data.Sales;
import org.dromio.client001.models.repository.SalesRepository;
import org.dromio.client001.models.service.InventoryService;
import org.dromio.client001.utility.CustomNotification;
import org.dromio.client001.views.components.SaleReceivePaymentView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Sale Screen")
@Route("")
@Menu(order = 1, title = "Sales Point") // icon = "line-awesome/svg/globe-solid.svg"
public class PosView extends HorizontalLayout {

    private final InventoryService inventoryService;
    private final ItemsToSell itemsToSell;
    private final SalesRepository salesRepository;

    Button confirmSaleButton;
    Button cancelSaleButton;
    private final Logger logger = LoggerFactory.getLogger(PosView.class);

    private Grid<InventoryItem> inventoryGrid;
    private Grid<ItemToSell> selectedItemGrid;

    TextField itemSearchField;

    List<InventoryItem> items;

    public PosView(InventoryService inventoryService, ItemsToSell itemsToSell, SalesRepository salesRepository) {
        this.inventoryService = inventoryService;
        this.itemsToSell = itemsToSell;
        this.salesRepository = salesRepository;

        initUi();
        loadInventoryData();

        setMargin(true);

        add(
                getMainContent()
        );
    }

    private void loadInventoryData() {
        String INVENTORY_NAME = "Stationary Inventory";
        items = inventoryService.getInventoryItems(INVENTORY_NAME);
    }

    private Component getMainContent() {
        HorizontalLayout container = new HorizontalLayout();
        container.add(getSelectedItemContainer());
        container.add(getItemSelectorContainer());
        container.setWidth("100%");
        return container;
    }

    private Component getItemSelectorContainer() {
        VerticalLayout container = new VerticalLayout();
        container.setPadding(false);
        container.add(getItemSelectorAndSearchFieldTable());
        return container;
    }

    private Component getSelectedItemContainer() {
        VerticalLayout container = new VerticalLayout();
        Span containerTitle = new Span("Selected Items");
        container.add(containerTitle);
        container.add(selectedItemGrid);
        container.add(getSaleActions());
        return container;
    }

    private Component getSaleActions() {

        confirmSaleButton = new Button("Confirm Sale");
        cancelSaleButton = new Button("Cancel Sale");

        confirmSaleButton.addClassNames(LumoUtility.Background.PRIMARY, LumoUtility.TextColor.PRIMARY_CONTRAST);
        cancelSaleButton.addClassNames(LumoUtility.Background.ERROR, LumoUtility.TextColor.PRIMARY_CONTRAST);


        confirmSaleButton.addClickListener( event -> {
            if (!itemsToSell.isEmpty()) {
                SaleReceivePaymentView paymentView = new SaleReceivePaymentView(String.valueOf(itemsToSell.getTotalPriceOfSelectedItems()));
                paymentView.addPaymentConfirmedListener( confirmPaymentEvent -> {
                   runConfirmSaleAlgorithm();
                });
                paymentView.addPaymentCanceledListener( cancelPaymentEvent -> {
                   CustomNotification.simpleErrorNotification("Payment Canceled!");
                });
            }
            else {
                CustomNotification.simpleErrorNotification("Please select item first");
            }
        });

        HorizontalLayout container = new HorizontalLayout(confirmSaleButton, cancelSaleButton);
        container.setFlexGrow(1, confirmSaleButton);
        container.setFlexGrow(1, cancelSaleButton);
        return container;
    }

    private void runConfirmSaleAlgorithm() {
        // TODO a pop up that show total amount and allow money to be received in cash or via other means
        try {
            createSalesRecord();
            itemsToSell.clearSelectedItems();
            refreshSelectedItemsGrid();
            refreshInventoryItemsGrid();
            CustomNotification.simpleSuccessNotification("Sale Confirmed!");
        }
        catch (Exception e) {
            logger.error("Error when trying to confirm sale {}", e.getMessage());
            CustomNotification.simpleErrorNotification("Something unexpected have occurred, please communicate this issue to the administrator.");
        }
    }

    private void createSalesRecord() {
        List<Sales> sales = new ArrayList<>();
        for (ItemToSell itemSelected : itemsToSell.getSoldItems()) {
            Sales sale = new Sales(
                    itemSelected.getComparingId(),
                    itemSelected.getSellingUnit(),
                    itemSelected.getSelectedQuantity(),
                    itemSelected.getItemSellingPrice()
            );
            sales.add(sale);
        }
        salesRepository.saveAll(sales);
    }

    private void initUi() {
        initSelectedItemsGrid();
    }

    private void initSelectedItemsGrid() {
        selectedItemGrid = new Grid<>(ItemToSell.class, false);
        selectedItemGrid.addColumn(ItemToSell::getItemName).setHeader("Item Name");
        selectedItemGrid.addColumn(ItemToSell::getSelectedQuantity).setHeader("Selected Quantity");
        selectedItemGrid.addColumn(ItemToSell::getSellingUnit).setHeader("Selling Unit");
        selectedItemGrid.addColumn(ItemToSell::getItemSellingPrice).setHeader("Selling Price");
        selectedItemGrid.addColumn(ItemToSell::getTotalPriceOfQuantitySelected).setHeader("Total Price of Quantity Selected");
        selectedItemGrid.setItems(itemsToSell.getSelectedItems());
        selectedItemGrid.setEmptyStateText("No Item Selected");
        selectedItemGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private Component getItemSelectorAndSearchFieldTable() {
        inventoryGrid = new Grid<>(InventoryItem.class, false);
        inventoryGrid.addColumn(InventoryItem::getItemName).setHeader("Item Name");
        inventoryGrid.addColumn(InventoryItem::getQuantity).setHeader("Quantity");
        inventoryGrid.addColumn(InventoryItem::getSellingPrice).setHeader("Selling Price");
        inventoryGrid.addItemClickListener( event -> {
           InventoryItem inventoryItem = event.getItem();
           try {
               itemsToSell.addItemToSell(new ItemToSell(inventoryItem));
           }
           catch (IllegalArgumentException e) {
               //TODO somehow check the error returned, if the error returned is not the one expected then info maybe exposed to the user
                CustomNotification.simpleErrorNotification(e.getMessage());
           }
           refreshSelectedItemsGrid();
           //logger.info("Item " + inventoryItem.getItemName() + " selected");
        });
        inventoryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        GridListDataView<InventoryItem> dataView = inventoryGrid.setItems(items);

        itemSearchField = new TextField();
        itemSearchField.setWidth("100%");
        itemSearchField.setPlaceholder("Search for an item...");
        itemSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        itemSearchField.setValueChangeMode(ValueChangeMode.LAZY);
        itemSearchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(item -> {
            String searchTerm = itemSearchField.getValue().trim();
            if (searchTerm.isEmpty())
                return true;

            return matchesTerm(item.getItemName(),
                    searchTerm);
        });

        return new VerticalLayout(itemSearchField, inventoryGrid);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private void refreshSelectedItemsGrid() {
        selectedItemGrid.getDataProvider().refreshAll();
    }

    private void refreshInventoryItemsGrid() {
        loadInventoryData();
        inventoryGrid.setItems(items);
    }

}
