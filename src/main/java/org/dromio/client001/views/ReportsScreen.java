package org.dromio.client001.views;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.dromio.client001.models.service.SalesService;
import org.dromio.client001.utility.MoneyFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@PageTitle("My Business Report")
@Route("/reports")
@Menu(title = "Reports")
public class ReportsScreen extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ReportsScreen.class);
    private final SalesService salesService;
    private HorizontalLayout cardTotalSales;


    public ReportsScreen(SalesService salesService) {
        this.salesService = salesService;
        styleMainLayout();
        initializeCards();

        add(
                cardTotalSales,
                createCard("Gross Profit", null),
                createCard("Cost of goods in stock", null),
                createCard("Cost of goods sold", null)
        );
    }

    private void initializeCards() {
        log.info("Total Sales All Time {}", salesService.getTotalSales());
        cardTotalSales = createCard("Sales", salesService.getTotalSales());
    }

    private void styleMainLayout() {
        setSizeFull();
        getStyle().set("display", "flex").set("flex-direction", "column");
        setPadding(true);
    }

    private HorizontalLayout createCard(String title, Double amount) {
        HorizontalLayout card = new HorizontalLayout();
        Span cardTitle = new Span(title);
        cardTitle.getStyle()
                .set("font-weight", "bold")
        ;
        Span cardAmount = new Span(MoneyFormatter.formatMoney(amount));

        card.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("border-bottom", "1px solid green")
        ;
        card.add(
                cardTitle,
                cardAmount
        );
        card.setWidthFull();
        return card;
    }
}
