package com.hitunes.repositories;

import com.hitunes.models.Customer;
import com.hitunes.models.CustomerPage;
import com.hitunes.models.TopCountry;
import com.hitunes.models.TopGenre;
import com.hitunes.models.TopSpender;
import java.sql.SQLException;
import java.util.*;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for operations on {@link Customer} objects.
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

  /**
   * Retrieves all Customer entities matching the full name and returns them in a list.
   *
   * @param lastName case sensitive string
   * @param firstName case sensitive string
   * @return a list of Customer entities
   * @throws SQLException if there is an error retrieving the customers from the database.
   */
  List<Customer> getByName(String lastName, String firstName) throws SQLException;

  /**
   * Retrieves all Customer entities with ID's matching the customerIds param and returns them in a
   * list.
   *
   * @param customerIds a list of customer ID's
   * @return a list of Customer entities
   * @throws SQLException if there is an error retrieving the customers from the database.
   */
  List<Customer> getByIds(List<Integer> customerIds) throws SQLException;

  /**
   * Retrieves a set amount of Customer entities and returns them in a list.
   *
   * <p>Ordered by last_name.
   *
   * @param offset at what index to begin fetching customers
   * @param limit how many customers to fetch
   * @return an object containaing a list of Customer entities and the offset and limit.
   * @throws SQLException if there is an error retrieving the customers from the database.
   */
  CustomerPage getPage(int offset, int limit) throws SQLException;

  /**
   * Calculates the Customer entitity with the highest spending and returns it.
   *
   * @return an object containing the Customer entity with the highest spending, and the amount
   *     spent.
   * @throws SQLException if there is an error retrieving the customer from the database.
   */
  TopSpender getTopSpender() throws SQLException;

  /**
   * Calculates the most popular genre(s) from a given Customer entity and returns them.
   *
   * <p>Retrieves the top genre, or all the top genres if more than one share top position.
   *
   * <p>Most popular in this context means the genre that corresponds to the most tracks from
   * invoices associated with the customer.
   *
   * @param customerId an integer representing a valid customer id
   * @return a list of genres with the most most popular genre(s)
   * @throws SQLException if there is an error retrieving the genre from the database.
   */
  TopGenre getMostPopularGenreFromOne(int customerId) throws SQLException;

  /**
   * Calculates the country with the most customers in the database and returns it.
   *
   * @return a TopCountry object containing a String representing the top country.
   * @throws SQLException if there is an error retrieving the country from the database.
   */
  TopCountry getCountryWithMostCustomers() throws SQLException;
}
