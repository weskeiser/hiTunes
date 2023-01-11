package com.hitunes.services;

import com.hitunes.models.Customer;
import com.hitunes.models.TopCountry;
import com.hitunes.models.TopGenre;
import com.hitunes.models.TopSpender;
import com.hitunes.repositories.CustomerRepo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepoImpl implements CustomerRepo {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  /**
   * Returns a list of Customer records from the database.
   *
   * <p>Expects a PreparedStatement containing a query that returns one or more customers.
   *
   * @param statement a PreparedStatement containing a query that returns one or more customers
   * @return a list of Customer records
   * @throws SQLException if there is an error executing the prepared statement or retrieving the
   *     customers
   */
  private List<Customer> fetchCustomers(PreparedStatement statement) throws SQLException {

    List<Customer> customers = new ArrayList<>();

    var res = statement.executeQuery();

    while (res.next()) {
      var customer = fetchCustomer(res);
      customers.add(customer);
    }

    return customers;
  }

  /**
   * Returns a Customer record.
   *
   * @param res A ResultSet containing the required fields
   * @return A Customer record
   * @throws SQLException If there is an error retrieving the fields
   */
  private Customer fetchCustomer(ResultSet res) throws SQLException {
    return new Customer(
        res.getInt("customer_id"),
        res.getString("phone"),
        res.getString("postal_code"),
        res.getString("address"),
        res.getString("country"),
        res.getString("first_name"),
        res.getString("last_name"),
        res.getString("email"));
  }

  /**
   * Establish a connection to the database.
   *
   * <p>DriverManager.getConnection(url, username, password);
   *
   * @return A Connection object representing the database connection.
   * @throws SQLException If there is an error establishing a connection.
   * @see java.sql.DriverManager
   */
  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, username, password);
  }
  ;

  @Override
  public List<Customer> getByName(String firstName, String lastName) {

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var query = "select * from customer where last_name like ? and first_name like ? ";

      var statement = conn.prepareStatement(query);
      statement.setString(1, lastName);
      statement.setString(2, firstName);

      customers = fetchCustomers(statement);

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public Optional<Customer> getById(Integer customerId) throws Exception {

    Customer customer = null;

    try (var conn = getConnection()) {

      var query = "select * from customer where customer_id = ?";

      var statement = conn.prepareStatement(query);
      statement.setInt(1, customerId);

      var res = statement.executeQuery();

      if (res.next()) customer = fetchCustomer(res);

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(customer);
  }

  @Override
  public List<Customer> getPage(int offset, int limit) {

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var query = "select * from customer ORDER BY last_name OFFSET ? LIMIT ?; ";

      var statement = conn.prepareStatement(query);
      statement.setInt(1, offset);
      statement.setInt(2, limit);

      customers = fetchCustomers(statement);

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public List<Customer> getByIds(List<Integer> customerIds) {

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var idsInParens = customerIds.toString().replace("[", "(").replace("]", ")");

      var query = "select * from customer where customer_id in " + idsInParens;

      var statement = conn.prepareStatement(query);

      customers = fetchCustomers(statement);

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public List<Customer> getAll() {

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var query = "select * from customer";

      var statement = conn.prepareStatement(query);

      customers = fetchCustomers(statement);

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public TopCountry getCountryWithMostCustomers() {

    TopCountry country = null;

    try (var conn = getConnection()) {

      var query = "select country from customer GROUP BY country ORDER BY count(*) DESC limit 1";

      var statement = conn.prepareStatement(query);

      var res = statement.executeQuery();

      if (res.next()) country = new TopCountry(res.getString("country"));

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return country;
  }

  @Override
  public TopGenre getMostPopularGenreFromOne(int customerId) {

    TopGenre topGenre = null;

    try (var conn = getConnection()) {

      var query =
          ("SELECT g.name as genre, COUNT(*)"
              + " FROM genre g"
              + " INNER JOIN track t ON t.genre_id = g.genre_id"
              + " INNER JOIN invoice_line il ON il.track_id = t.track_id"
              + " INNER JOIN invoice i ON i.invoice_id = il.invoice_id"
              + " WHERE i.customer_id = ?"
              + " GROUP BY g.name"
              + " ORDER BY count DESC"
              + " LIMIT 1");

      var statement = conn.prepareStatement(query);
      statement.setInt(1, customerId);

      var res = statement.executeQuery();

      if (res.next()) topGenre = new TopGenre(res.getString("genre"), res.getString("count"));

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return topGenre;
  }

  @Override
  public TopSpender getTopSpender() {

    TopSpender topSpender = null;

    try (var conn = getConnection()) {

      var query =
          ("select *, sum(total)"
              + " from customer c"
              + " inner join invoice i on c.customer_id = i.customer_id"
              + " group by c.customer_id, i.invoice_id"
              + " order by sum(total) desc"
              + " limit 1");

      var statement = conn.prepareStatement(query);

      var res = statement.executeQuery();
      res.next();

      topSpender = new TopSpender(fetchCustomer(res), res.getInt("sum"));

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return topSpender;
  }

  @Override
  public void createNew(Customer customer) {

    try (Connection conn = getConnection()) {

      String query =
          "INSERT INTO customer (first_name, last_name, phone, postal_code, address, country,"
              + " email) VALUES (?, ?, ?, ?, ?, ?, ?)";

      PreparedStatement statement = conn.prepareStatement(query);
      statement.setString(1, customer.firstName());
      statement.setString(2, customer.lastName());
      statement.setString(3, customer.phoneNumber());
      statement.setString(4, customer.postalCode());
      statement.setString(5, customer.address());
      statement.setString(6, customer.country());
      statement.setString(7, customer.email());

      statement.executeUpdate();
      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(Customer customer) {

    var query = "delete from customer where customer_id = ?";

    try (Connection conn = getConnection()) {

      PreparedStatement statement = conn.prepareStatement(query);
      statement.setInt(1, customer.customerId());

      statement.executeUpdate();
      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteById(Integer customerId) {

    var query = "delete from customer where customer_id = ?";

    try (Connection conn = getConnection()) {

      PreparedStatement statement = conn.prepareStatement(query);
      statement.setInt(1, customerId);

      statement.executeUpdate();
      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Customer customer) {
    // TODO Auto-generated method stub

  }
}
