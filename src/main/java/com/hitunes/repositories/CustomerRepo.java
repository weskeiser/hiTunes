package com.hitunes.repositories;

import com.hitunes.models.*;
import java.sql.SQLException;
import java.util.*;

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
public interface CustomerRepo extends CrudRepo<Customer, Integer> {

  /**
   * Retrieves all {@link Customer} entries matching the full name and returns them in a list.
   *
   * @param lastName case sensitive string
   * @param firstName case sensitive string
   * @return a list of Customer entries
   * @throws SQLException if there is an error with the database request.
   */
  List<Customer> getByName(String lastName, String firstName) throws SQLException;

  /**
   * Retrieves all {@link Customer} entries with ID's matching the customerIds param and returns them in a
   * list.
   *
   * @param customerIds a list of customer ID's
   * @return a list of Customer entries
   * @throws SQLException if there is an error with the database request.
   */
  List<Customer> getByIds(List<Integer> customerIds) throws SQLException;

  /**
   * Retrieves a set amount of {@link Customer} entries and returns them in a list.
   *
   * <p>Ordered by last_name.
   *
   * @param offset at what index to begin fetching customers
   * @param limit how many customers to fetch
   * @return an object containaing a list of Customer entries from the database, as well as the
   *     offset and limit.
   * @throws SQLException if there is an error with the database request.
   */
  CustomerPage getPage(int offset, int limit) throws SQLException;

  /**
   * Calculates the {@link Customer} entitity with the highest spending and returns it.
   *
   * @return an object containing the Customer entity with the highest spending, and the amount
   *     spent.
   * @throws SQLException if there is an error with the database request.
   */
  TopSpender getTopSpender() throws SQLException;

  /**
   * Calculates the most popular genre(s) from a given {@link Customer} entity and returns them.
   *
   * <p>Retrieves the top genre, or all the top genres if more than one share top position.
   *
   * <p>Most popular in this context means the genre that corresponds to the most tracks from
   * invoices associated with the customer.
   *
   * @param customerId an integer representing a valid customer id
   * @return a list of genres with the most most popular genre(s)
   * @throws SQLException if there is an error with the database request.
   */
  TopGenre getMostPopularGenreFromOne(int customerId) throws SQLException;

  /**
   * Calculates the country with the most customers in the database and returns it.
   *
   * @return a TopCountry object containing a String representing the top country.
   * @throws SQLException if there is an error with the database request.
   */
  TopCountry getCountryWithMostCustomers() throws SQLException;
}
