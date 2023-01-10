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

    var query = "select * from customer where last_name like ? and first_name like ? ";

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

  @Override
  public TopGenre getMostPopularGenreFromOne(int id) {

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

    TopGenre topGenre = null;

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(query);
      statement.setInt(1, id);

      var res = statement.executeQuery();

      if (res.next()) topGenre = new TopGenre(res.getString("genre"), res.getString("count"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    return topGenre;
  }

  @Override
  public TopSpender getTopSpender() {

    TopSpender topSpender = null;

    var query =
        ("select *, sum(total)"
            + " from customer c"
            + " inner join invoice i on c.customer_id = i.customer_id"
            + " group by c.customer_id, i.invoice_id"
            + " order by sum(total) desc"
            + " limit 1");

    try (var conn = getConnection()) {

      var statement = conn.prepareStatement(query);

      var res = statement.executeQuery();
      res.next();

      topSpender = new TopSpender(fetchCustomer(res), res.getInt("sum"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    return topSpender;
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
