package org.dromio.client001.models.repository;

import org.dromio.client001.models.data.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesRepository extends ListCrudRepository<Sales, String> {
    // Query for sales in a given date range
    //@Query("SELECT s FROM Sales s WHERE s.soldAtTime BETWEEN :startDate AND :endDate")
    //List<Sales> getSalesBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Query for sales today
    //@Query("SELECT s FROM Sales s WHERE s.soldAtTime >= :startOfDay AND s.soldAtTime < :endOfDay")
    //List<Sales> getSalesToday(LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Query for sales this week
    //@Query("SELECT s FROM Sales s WHERE s.soldAtTime >= CURRENT_DATE - 7")
    //List<Sales> getSalesThisWeek();

    // Query for sales this month
    //@Query("SELECT s FROM Sales s WHERE s.soldAtTime >= :startOfMonth AND s.soldAtTime < :endOfMonth")
    //List<Sales> getSalesThisMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
