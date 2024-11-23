package org.dromio.client001.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.dromio.client001.models.data.InventoryItem;
import org.dromio.client001.models.service.InventoryItemService;
import org.dromio.client001.models.service.InventoryService;
import org.dromio.client001.models.service.SettingService;
import org.dromio.client001.models.service.UnitService;
import org.dromio.client001.utility.CustomNotification;
import org.dromio.client001.views.components.InventoryItemForm;
import org.dromio.client001.views.components.ReceiveItemView;
import org.dromio.client001.views.components.UnitConfigurationView;
import software.xdev.vaadin.gridfilter.GridFilter;

import java.util.List;


@PageTitle("Inventory Management")
@Menu( order = 2, title = "Manage Inventory")
@Route("/manageInventory")
public class InventoryManagementScreen extends HorizontalLayout {

    private final InventoryService inventoryService;
    private final InventoryItemService inventoryItemService;
    private final UnitService unitService;
    private final SettingService settingService;
    Grid<InventoryItem> inventoryGrid;
    InventoryItemForm inventoryItemForm;
    String INVENTORY_NAME;
    private List<InventoryItem> items;
    private VerticalLayout formAndActionsContainer;
    VerticalLayout actionLayout;
    private InventoryItem selectedItem;

    public InventoryManagementScreen(InventoryService inventoryService, InventoryItemService inventoryItemService, UnitService unitService, SettingService settingService) {
        this.inventoryService = inventoryService;
        this.inventoryItemService = inventoryItemService;
        this.unitService = unitService;
        this.settingService = settingService;
        loadInventoryData();
        initGrid();
        configureForm();
        configureFormAndActionsContainer();
        configureUi();

        add(
                inventoryGrid,
                formAndActionsContainer
        );

    }

    private void loadInventoryData() {
        INVENTORY_NAME = "Stationary Inventory";
        items =  inventoryService.getInventoryItems(INVENTORY_NAME);
    }

    private void configureUi() {
        getStyle().set("display", "flex");
        this.setFlexGrow(2, inventoryGrid);
        this.setFlexGrow(1, formAndActionsContainer);
        this.setPadding(true);
        this.setSizeFull();
    }

    private void configureForm() {

        inventoryItemForm = new InventoryItemForm(unitService);
        inventoryItemForm.setInventoryItem(new InventoryItem());

        inventoryItemForm.addSaveListener( event -> {
            inventoryService.addItemToInventory(INVENTORY_NAME, event.getInventoryItem(), event.getUnit());
            refreshInventoryGrid();
            CustomNotification.simpleSuccessNotification("Item \"" + event.getInventoryItem().getItemName() + "\" successfully added to inventory");
        });

        inventoryItemForm.addClearListener( event -> selectedItem = null);

    }

    private void configureFormAndActionsContainer() {
        formAndActionsContainer = new VerticalLayout();
        styleFormAndActionContainer();

        Text formTitle = new Text("Item Form");
        formAndActionsContainer.add(formTitle, inventoryItemForm, getActions());
    }

    private Component getActions() {
        actionLayout = new VerticalLayout();
        styleActionLayout();

        Button unitConfigureButton = new Button("Configure Unit");
        unitConfigureButton.setEnabled(settingService.isEnabled("Unit Configurations"));

        unitConfigureButton.addClickListener(event -> {
            if ( selectedItem == null ) {
                CustomNotification.simpleWarningNotification("To configure unit for an item please select it from stock first");
            }
            else {
                UnitConfigurationView configurationView = new UnitConfigurationView(selectedItem, inventoryItemService, unitService);
                configurationView.addSaveConfigurationListener( saveEvent -> refreshInventoryGrid());
                configurationView.addCancelConfigurationListener( cancelEvent -> CustomNotification.simpleInfoNotification("Unit Configuration Cancelled"));
            }
        });

        Button receiveItemButton = new Button("Receive");
        receiveItemButton.setEnabled(settingService.isEnabled("Receive Item"));
        receiveItemButton.addClickListener( event -> {
            if (selectedItem == null) {
                CustomNotification.simpleWarningNotification("Please select item you want to receive.");
            }
            else {
                ReceiveItemView receiveItemView = new ReceiveItemView(this.selectedItem, this.inventoryItemService, this.unitService);

                receiveItemView.addItemReceiveEventListener( receiveEvent -> refreshInventoryGrid());
                receiveItemView.addItemReceiveCanceledListener( cancelEvent -> CustomNotification.simpleInfoNotification("Item Receive Canceled"));
            }
        });

        actionLayout.add(
                unitConfigureButton,
                receiveItemButton
        );
        return actionLayout;
    }

    private void styleActionLayout() {
        actionLayout.getStyle()
                .set("display", "flex");
        actionLayout.setPadding(false);
    }

    private void styleFormAndActionContainer() {
        formAndActionsContainer.getStyle()
                .set("display", "flex");
        formAndActionsContainer.setPadding(false);
    }

    private void refreshInventoryGrid() {
        loadInventoryData();
        inventoryGrid.setItems(items);
        inventoryGrid.getDataProvider().refreshAll();
    }

    private void initGrid() {
        inventoryGrid = new Grid<>(InventoryItem.class, false);
        inventoryGrid.addColumn(InventoryItem::getItemName).setHeader("Item Name").setSortable(true);
        inventoryGrid.addColumn(InventoryItem::getBuyingPrice).setHeader("Buying Price").setSortable(true);
        inventoryGrid.addColumn(InventoryItem::getSellingPrice).setHeader("Selling Price").setSortable(true);
        inventoryGrid.addColumn(InventoryItem::getQuantity).setHeader("Quantity").setSortable(true);
        inventoryGrid.addColumn(inventoryItem -> unitService.getItemStockingUnitName(inventoryItem.getInventoryItemId())).setHeader("Stocking Unit");
        inventoryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        inventoryGrid.setEmptyStateText("No inventory items found");
        inventoryGrid.setSizeFull();
        inventoryGrid.setItems(items);
        inventoryGrid.asSingleSelect().addValueChangeListener(event -> editItem(event.getValue()));

    }

    private void editItem(InventoryItem inventoryItem) {
        if (inventoryItem == null) {
            inventoryGrid.asSingleSelect().clear();
            selectedItem = null;
            inventoryItemForm.setInventoryItem(new InventoryItem());
        }
        else {
            inventoryItemForm.setInventoryItem(inventoryItem);
            selectedItem = inventoryItem;
        }
        //inventoryItemForm.setInventoryItem(inventoryItem);

    }
}
