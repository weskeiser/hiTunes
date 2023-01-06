package com.hitunes.repositories.implementations;

import com.hitunes.models.customer.Customer;
import com.hitunes.repositories.interfaces.CustomerRepo;
import java.sql.DriverManager;
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

  @Override
  public Optional<Customer> get(Customer object) {
    return null;
  }

  @Override
  public List<Customer> getByName(String firstName, String lastName) {

    List<Customer> customers = new ArrayList<>();

    var query = "select * from customer where first_name = ? and last_name = ? ";

    try (var conn = DriverManager.getConnection(url, username, password)) {

      var statement = conn.prepareStatement(query);
      statement.setString(1, firstName);
      statement.setString(2, lastName);

      var res = statement.executeQuery();

      while (res.next()) {
        Customer customer =
            new Customer(
                res.getInt("customer_id"),
                res.getString("phone"),
                res.getString("postal_code"),
                res.getString("address"),
                res.getString("country"),
                res.getString("first_name"),
                res.getString("last_name"),
                res.getString("email"));

        customers.add(customer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public Optional<Customer> getById(Integer id) throws Exception {
    Customer customer = null;

    var query = "select * from customer where customer_id = " + id + ";";

    try (var conn = DriverManager.getConnection(url, username, password)) {

      var statement = conn.prepareStatement(query);

      var res = statement.executeQuery();

      if (res.next())
        customer =
            new Customer(
                res.getInt("customer_id"),
                res.getString("phone"),
                res.getString("postal_code"),
                res.getString("address"),
                res.getString("country"),
                res.getString("first_name"),
                res.getString("last_name"),
                res.getString("email"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(customer);
  }

  @Override
  public List<Customer> getByIds(List<Integer> ids, int limit, int offset) {

    List<Customer> customers = new ArrayList<>();

    var idsInParens = (ids.toString().replace("[", "(").replace("]", ")"));
    var query = "select * from customer where customer_id in " + idsInParens;

    try (var conn = DriverManager.getConnection(url, username, password)) {

      var statement = conn.prepareStatement(query);

      var res = statement.executeQuery();

      while (res.next()) {

        var newCustomer =
            new Customer(
                res.getInt("customer_id"),
                res.getString("phone"),
                res.getString("postal_code"),
                res.getString("address"),
                res.getString("country"),
                res.getString("first_name"),
                res.getString("last_name"),
                res.getString("email"));

        customers.add(newCustomer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public List<Customer> getAll() {

    List<Customer> customers = new ArrayList<>();

    var query = "select * from customer";

    try (var conn = DriverManager.getConnection(url, username, password)) {

      var statement = conn.prepareStatement(query);

      var res = statement.executeQuery();

      while (res.next()) {
        Customer customer =
            new Customer(
                res.getInt("customer_id"),
                res.getString("phone"),
                res.getString("postal_code"),
                res.getString("address"),
                res.getString("country"),
                res.getString("first_name"),
                res.getString("last_name"),
                res.getString("email"));

        customers.add(customer);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return customers;
  }

  @Override
  public String getCountryWithMostCustomers() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getMostPopularGenreFromOne(int id) {
    // TODO Auto-generated method stub
    return null;
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
