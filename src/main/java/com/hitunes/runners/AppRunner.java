package com.hitunes.runners;

import com.hitunes.models.Customer;
import com.hitunes.repositories.CustomerRepo;
import java.sql.SQLException;
import java.util.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {
  private final CustomerRepo customerRepo;

  public AppRunner(CustomerRepo customerRepo) {
    this.customerRepo = customerRepo;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    Customer punk =
        new Customer(
            65,
            "0118999811999119",
            "12345",
            "Address",
            "Country",
            "Crafty",
            "Punk",
            "daft@punk.no");

    // printAllCustomers();
    // printTopCountry();
    // printTopSpender();
    // printTopGenre(3);
    // printCustomersByName("Luís", "Gonçalves");
    // printCustomersByIds(List.of(1, 2, 3));
    // printCustomerPage(10, 2);
    // createNewCustomer(punk);
    // updateCustomer(punk);
    printCustomerById(65);
  }

  private void printCustomerPage(int offset, int limit) {
    try {
      var customerPage = customerRepo.getPage(offset, limit);
      System.out.println(customerPage);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void printCustomersByName(String firstName, String lastName) {
    try {
      var customersByName = customerRepo.getByName(firstName, lastName);
      System.out.println(customersByName);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void printCustomersByIds(List<Integer> ids) {
    try {

      var customersByIds = customerRepo.getByIds(ids);
      System.out.println(customersByIds);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void printAllCustomers() {
    try {
      var customers = customerRepo.getAll();
      System.out.println(customers);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void printTopSpender() {
    try {
      var topSpender = customerRepo.getTopSpender();
      System.out.println(topSpender);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void printTopCountry() {
    try {
      var topCountry = customerRepo.getCountryWithMostCustomers();
      System.out.println(topCountry);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void printCustomerById(int customerId) {
    try {
      Optional<Customer> customer = customerRepo.getById(customerId);
      System.out.println(customer.orElseThrow());

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
  }

  private void createNewCustomer(Customer customer) {
    try {
      customerRepo.createNew(customer);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateCustomer(Customer customer) {
    try {
      customerRepo.update(customer);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void printTopGenre(int id) {
    try {
      var topGenre = customerRepo.getMostPopularGenreFromOne(id);
      System.out.println(topGenre);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
