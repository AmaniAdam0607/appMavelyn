package org.dromio.client001.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.service.InventoryService;
import org.dromio.client001.utility.CustomNotification;
import org.dromio.client001.views.components.InventoryItemForm;

import java.util.List;


@PageTitle("Inventory Management")
@Menu( order = 2, title = "Manage Inventory")
@Route("/manageInventory")
public class InventoryManagementScreen extends HorizontalLayout {

    private final InventoryService inventoryService;
    Grid<InventoryItem> inventoryGrid;
    InventoryItemForm inventoryItemForm;
    String INVENTORY_NAME;
    private List<InventoryItem> items;

    public InventoryManagementScreen(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        loadInventoryData();
        initGrid();
        configureForm();
        configureUi();

        add(
                inventoryGrid,
                inventoryItemForm
        );
    }

    private void loadInventoryData() {
        INVENTORY_NAME = "Stationary Inventory";
        items =  inventoryService.getInventoryItems(INVENTORY_NAME);
    }

    private void configureUi() {
        this.setFlexGrow(2, inventoryGrid);
        this.setFlexGrow(1, inventoryItemForm);
        this.setPadding(true);
        this.setSizeFull();
    }

    private void configureForm() {

        inventoryItemForm = new InventoryItemForm();
        inventoryItemForm.setInventoryItem(new InventoryItem());

        inventoryItemForm.addSaveListener( event -> {
            inventoryService.addItemsToAnInventory(INVENTORY_NAME, List.of(event.getInventoryItem()));
           refreshInventoryGrid();
            CustomNotification.simpleSuccessNotification("Item \"" + event.getInventoryItem().getItemName() + "\" successfully added to inventory");
        });

    }

    private void refreshInventoryGrid() {
        loadInventoryData();
        inventoryGrid.setItems(items);
    }


    private void initGrid() {
        inventoryGrid = new Grid<>(InventoryItem.class, false);
        inventoryGrid.addColumn(InventoryItem::getItemName).setHeader("Item Name").setSortable(true);
        inventoryGrid.addColumn(InventoryItem::getBuyingPrice).setHeader("Buying Price").setSortable(true);
        inventoryGrid.addColumn(InventoryItem::getSellingPrice).setHeader("Selling Price").setSortable(true);
        inventoryGrid.addColumn(InventoryItem::getQuantity).setHeader("Quantity").setSortable(true);
        inventoryGrid.addColumn(InventoryItem::getUnit).setHeader("Unit");
        inventoryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        inventoryGrid.setEmptyStateText("No inventory items found");
        inventoryGrid.setSizeFull();

        GridListDataView<InventoryItem> dataView = inventoryGrid.setItems(items);
        inventoryGrid.asSingleSelect().addValueChangeListener(event -> editItem(event.getValue()));
    }

    private void editItem(InventoryItem inventoryItem) {
        if (inventoryItem == null) {
            inventoryGrid.asSingleSelect().clear();
            inventoryItemForm.setInventoryItem(new InventoryItem());
        }
        else {
            inventoryItemForm.setInventoryItem(inventoryItem);
        }

    }
}
