package org.dromio.client001.models.repository;

import org.dromio.client001.models.data.Sales;
import org.springframework.data.repository.ListCrudRepository;

public interface SalesRepository extends ListCrudRepository<Sales, String> {

}
