package com.hitunes.repositories;

import com.hitunes.models.Customer;
import com.hitunes.models.TopCountry;
import com.hitunes.models.TopGenre;
import com.hitunes.models.TopSpender;
import java.util.*;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Customer} objects in the persistence store.
 *
 * <p>This interface extends {@link CrudRepo} with additional methods to retrieve customers by name,
 * ids, top spenders, most popular genre from specific id, the country with most customers and
 * customers with pagination.
 *
 * @author Wes Keiser
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface CustomerRepo extends CrudRepo<Customer, Integer> {

  List<Customer> getByName(String lastName, String firstName);

  List<Customer> getByIds(List<Integer> customerIds);

  List<Customer> getPage(int offset, int limit);

  TopSpender getTopSpender();

  TopGenre getMostPopularGenreFromOne(int customerId);

  TopCountry getCountryWithMostCustomers();
}
