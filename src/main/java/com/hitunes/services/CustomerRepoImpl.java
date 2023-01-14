package com.hitunes.services;

import com.hitunes.models.*;
import com.hitunes.repositories.CustomerRepo;
import java.sql.*;
import java.util.*;
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
  public List<Customer> getByName(String firstName, String lastName) throws SQLException {

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {
      var query = "select * from customer where last_name like ? and first_name like ? ";

      var statement = conn.prepareStatement(query);
      statement.setString(1, lastName);
      statement.setString(2, firstName);

      customers = fetchCustomers(statement);

      statement.close();

    } catch (Exception e) {
      throw e;
    }

    return customers;
  }

  @Override
  public Optional<Customer> getById(Integer customerId) throws SQLException {

    Customer customer = null;

    try (var conn = getConnection()) {

      var query = "select * from customer where customer_id = ?";

      var statement = conn.prepareStatement(query);
      statement.setInt(1, customerId);

      var res = statement.executeQuery();

      if (res.next()) customer = fetchCustomer(res);

      statement.close();

    } catch (Exception e) {
      throw e;
    }

    return Optional.ofNullable(customer);
  }

  @Override
  public CustomerPage getPage(int offset, int limit) throws SQLException {

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var query = "select * from customer ORDER BY last_name OFFSET ? LIMIT ?; ";

      var statement = conn.prepareStatement(query);
      statement.setInt(1, offset);
      statement.setInt(2, limit);

      customers = fetchCustomers(statement);

      statement.close();

    } catch (Exception e) {
      throw e;
    }

    return new CustomerPage(customers, offset, limit);
  }

  @Override
  public List<Customer> getByIds(List<Integer> customerIds) throws SQLException {

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var idsInParens = customerIds.toString().replace("[", "(").replace("]", ")");

      var query = "select * from customer where customer_id in " + idsInParens;

      var statement = conn.prepareStatement(query);

      customers = fetchCustomers(statement);

      statement.close();

    } catch (Exception e) {
      throw e;
    }

    return customers;
  }

  @Override
  public List<Customer> getAll() throws SQLException {

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var query = "select * from customer";

      var statement = conn.prepareStatement(query);

      customers = fetchCustomers(statement);

      statement.close();

    } catch (Exception e) {
      throw e;
    }

    return customers;
  }

  @Override
  public TopCountry getCountryWithMostCustomers() throws SQLException {

    TopCountry country = null;

    try (var conn = getConnection()) {

      var query = "select country from customer GROUP BY country ORDER BY count(*) DESC limit 1";

      var statement = conn.prepareStatement(query);

      var res = statement.executeQuery();

      if (res.next()) country = new TopCountry(res.getString("country"));

      statement.close();

    } catch (SQLException e) {
      throw e;
    }

    return country;
  }

  @Override
  public TopGenre getMostPopularGenreFromOne(int customerId) throws SQLException {

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
              + " LIMIT 2");

      var statement = conn.prepareStatement(query);
      statement.setInt(1, customerId);

      var res = statement.executeQuery();

      res.next();

      var topGenres = List.of(res.getString("genre"));
      var topGenreCount = res.getInt("count");

      while (res.next()) {

        if (res.getInt("count") == topGenreCount) {
          topGenres.add(res.getString("genre"));
        } else break;
      }
      ;

      topGenre = new TopGenre(customerId, topGenres, topGenreCount);

      statement.close();

    } catch (SQLException e) {
      throw e;
    }

    return topGenre;
  }

  @Override
  public TopSpender getTopSpender() throws SQLException {

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
      throw e;
    }

    return topSpender;
  }

  @Override
  public void createNew(Customer customer) throws SQLException {

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
      throw e;
    }
  }

  @Override
  public void delete(Customer customer) throws SQLException {

    var query = "delete from customer where customer_id = ?";

    try (Connection conn = getConnection()) {

      PreparedStatement statement = conn.prepareStatement(query);
      statement.setInt(1, customer.customerId());

      statement.executeUpdate();
      statement.close();

    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public void deleteById(Integer customerId) throws SQLException {

    var query = "delete from customer where customer_id = ?";

    try (Connection conn = getConnection()) {

      PreparedStatement statement = conn.prepareStatement(query);
      statement.setInt(1, customerId);

      statement.executeUpdate();
      statement.close();

    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public void update(Customer customer) throws SQLException {
    try (Connection conn = getConnection()) {

      String query =
          "UPDATE customer SET"
              + " first_name = ?,"
              + " last_name = ?,"
              + " phone = ?,"
              + " postal_code = ?,"
              + " address = ?,"
              + " country = ?,"
              + " email = ?"
              + " WHERE customer_id = ?";

      PreparedStatement statement = conn.prepareStatement(query);
      statement.setString(1, customer.firstName());
      statement.setString(2, customer.lastName());
      statement.setString(3, customer.phoneNumber());
      statement.setString(4, customer.postalCode());
      statement.setString(5, customer.address());
      statement.setString(6, customer.country());
      statement.setString(7, customer.email());
      statement.setInt(8, customer.customerId());

      statement.executeUpdate();
      statement.close();

    } catch (Exception e) {
      throw e;
    }
  }
}
