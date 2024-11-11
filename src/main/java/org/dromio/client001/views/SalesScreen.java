package org.dromio.client001.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.dromio.client001.models.data.Sales;
import org.dromio.client001.models.repository.SalesRepository;
import org.dromio.client001.models.service.InventoryItemService;

@PageTitle("My Sales")
@Route("/mySales")
@Menu( order = 0, title = "View my sales")
public class SalesScreen extends VerticalLayout {

    private final SalesRepository salesRepository;
    private final InventoryItemService inventoryItemService;
    Grid<Sales> salesGrid;

    public SalesScreen(SalesRepository salesRepository, InventoryItemService inventoryItemService) {
        this.salesRepository = salesRepository;
        this.inventoryItemService = inventoryItemService;
        initSalesGrid();

        add(
                salesGrid
        );
    }

    private void initSalesGrid() {
        salesGrid = new Grid<>(Sales.class, false);
        salesGrid.addColumn(sale -> sale.getNameOfItem(inventoryItemService.getAllItemNames())).setHeader("Item Name");
        salesGrid.addColumn(Sales::getQuantitySold).setHeader("Quantity");
        salesGrid.addColumn(Sales::getSoldWithPrice).setHeader("Sold with price");
        salesGrid.addColumn(Sales::getSoldWithUnit).setHeader("Sold in units");
        salesGrid.addColumn(Sales::getTotalSingleSalePrice).setHeader("Total");
        salesGrid.addColumn(Sales::getTimeInTimePassed).setHeader("Date");
        salesGrid.setItems(salesRepository.findAll());
        salesGrid.setEmptyStateText("No items sold as of now");
        salesGrid.setMultiSort(true);

        salesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }
}
