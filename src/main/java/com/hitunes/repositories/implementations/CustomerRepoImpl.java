package com.hitunes.repositories.implementations;

import com.hitunes.models.customer.Customer;
import com.hitunes.models.customer.TopCountry;
import com.hitunes.models.customer.TopGenre;
import com.hitunes.repositories.interfaces.CustomerRepo;
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

  private final String url;
  private final String username;
  private final String password;

  public CustomerRepoImpl(
      @Value("${spring.datasource.url}") String url,
      @Value("${spring.datasource.username}") String username,
      @Value("${spring.datasource.password}") String password) {

    this.url = url;
    this.username = username;
    this.password = password;
  }

  private List<Customer> fetchCustomers(PreparedStatement statement) throws SQLException {

    List<Customer> customers = new ArrayList<>();

    var res = statement.executeQuery();

    while (res.next()) {

      var customer = fetchCustomer(res);

      customers.add(customer);
    }

    return customers;
  }

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

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, username, password);
  }
  ;

  @Override
  public Optional<Customer> get(Customer customer) {
    return null;
  }

  @Override
  public List<Customer> getByName(String lastName, String firstName) {

    var query = "select * from customer where last_name = ? and first_name = ? ";

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(query);
      statement.setString(1, lastName);
      statement.setString(2, firstName);

      customers = fetchCustomers(statement);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public Optional<Customer> getById(Integer id) throws Exception {

    var query = "select * from customer where customer_id = ?";

    Customer customer = null;

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(query);
      statement.setInt(1, id);

      var res = statement.executeQuery();

      if (res.next()) customer = fetchCustomer(res);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(customer);
  }

  public List<Customer> getPage(int offset, int limit) {

    var query = "select * from customer ORDER BY last_name OFFSET ? LIMIT ?; ";

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(query);
      statement.setInt(1, offset);
      statement.setInt(2, limit);

      customers = fetchCustomers(statement);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public List<Customer> getByIds(List<Integer> ids) {

    var idsInParens = ids.toString().replace("[", "(").replace("]", ")");

    var query = "select * from customer where customer_id in " + idsInParens;

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(query);

      customers = fetchCustomers(statement);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public List<Customer> getAll() {

    var query = "select * from customer";

    List<Customer> customers = new ArrayList<>();

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(query);

      customers = fetchCustomers(statement);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public TopCountry getCountryWithMostCustomers() {

    var query = "select country from customer GROUP BY country ORDER BY count(*) DESC limit 1";

    TopCountry country = null;

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(query);

      var res = statement.executeQuery();

      if (res.next()) country = new TopCountry(res.getString("country"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    return country;
  }

  private void appendWithNL(StringBuilder stringBuilder, String string) {
    stringBuilder.append(string + "\n");
  }

  @Override
  public TopGenre getMostPopularGenreFromOne(int id) {

    var queryBuilder = new StringBuilder();
    appendWithNL(queryBuilder, "SELECT g.name as genre, COUNT(*)");
    appendWithNL(queryBuilder, "FROM genre g");
    appendWithNL(queryBuilder, "INNER JOIN track t ON t.genre_id = g.genre_id");
    appendWithNL(queryBuilder, "INNER JOIN invoice_line il ON il.track_id = t.track_id");
    appendWithNL(queryBuilder, "INNER JOIN invoice i ON i.invoice_id = il.invoice_id");
    appendWithNL(queryBuilder, "WHERE i.customer_id = ?");
    appendWithNL(queryBuilder, "GROUP BY g.name");
    appendWithNL(queryBuilder, "ORDER BY count DESC");
    appendWithNL(queryBuilder, "LIMIT 1;");

    TopGenre topGenre = null;

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(queryBuilder.toString());
      statement.setInt(1, id);

      var res = statement.executeQuery();

      if (res.next()) topGenre = new TopGenre(res.getString("genre"), res.getString("count"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    return topGenre;
  }

  @Override
  public Customer getTopSpender(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createNew(Customer object) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Customer object) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteById(Integer id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(Customer object) {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateById(Integer id) {
    // TODO Auto-generated method stub

  }
}
