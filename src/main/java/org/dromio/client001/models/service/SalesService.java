package org.dromio.client001.models.service;

import org.dromio.client001.models.data.Sales;
import org.dromio.client001.models.repository.SalesRepository;
import org.dromio.client001.utility.CustomNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesService {

    private static final Logger loggger = LoggerFactory.getLogger(SalesService.class);
    private final SalesRepository salesRepository;



    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    // Get total sales for all time
    public Double getTotalSales() {
        List<Sales> allSales = salesRepository.findAll();
        if (allSales != null && allSales.isEmpty()) {
            return 0.0;
        }
        loggger.info("Sales returned {}", allSales);
        loggger.info("Total Sales All Time Calculated {}", calculateTotalSales(allSales));
        return calculateTotalSales(allSales);
    }

    // Get total sales for today
//    public Double getTotalSalesToday() {
//        LocalDate today = LocalDate.now();
//        LocalDateTime startOfDay = today.atStartOfDay();  // 00:00 of today
//        LocalDateTime endOfDay = today.atTime(23, 59, 59, 999999999); // 23:59:59.999999999 of today
//        List<Sales> salesToday = salesRepository.getSalesToday(startOfDay, endOfDay);
//        return calculateTotalSales(salesToday);
//    }

    // Get total sales for this week
    //public Double getTotalSalesThisWeek() {
    //    List<Sales> salesThisWeek = salesRepository.getSalesThisWeek();
    //    return calculateTotalSales(salesThisWeek);
    //}

    // Get total sales for this month
//    public Double getTotalSalesThisMonth() {
//        LocalDate today = LocalDate.now();
//        LocalDate startOfMonth = today.withDayOfMonth(1);  // First day of the current month
//        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());  // Last day of the current month
//        LocalDateTime startOfMonthDateTime = startOfMonth.atStartOfDay();  // 00:00 of the first day
//        LocalDateTime endOfMonthDateTime = endOfMonth.atTime(23, 59, 59, 999999999); // 23:59:59.999999999 of the last day
//        List<Sales> salesThisMonth = salesRepository.getSalesThisMonth(startOfMonthDateTime, endOfMonthDateTime);
//        return calculateTotalSales(salesThisMonth);
//    }

    // Get total sales between custom date range
    //public Double getTotalSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
    //    List<Sales> salesBetweenDates = salesRepository.getSalesBetweenDates(startDate, endDate);
    //    return calculateTotalSales(salesBetweenDates);
    //}

    // Utility method to calculate the total sales from a list of sales
    private Double calculateTotalSales(List<Sales> salesList) {

        if (salesList == null || salesList.isEmpty()) {
            //CustomNotification.simpleWarningNotification("No Sales made in the time specified");
            return 00.0;
        }
        // its here that means salesList contains something
        Double total = 0.0;
        for (Sales sale : salesList) {
            total += sale.getTotalSingleSalePrice();
        }
        return total;
    }
}
