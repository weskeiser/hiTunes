package com.hitunes.runners;

import com.hitunes.models.Customer;
import com.hitunes.repositories.CustomerRepo;
import java.util.Optional;
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
            63, "0118999811999119", "12345", "Address", "Country", "Raft", "Punk", "daft@punk.no");
    // customerRepo.createNew(punk);
    // customerRepo.delete(punk);

    Optional<Customer> customer = customerRepo.getById(62);
    System.out.println(customer);

    // var customersByName = customerRepo.getByName( "Luís", "Gonçalves");
    // System.out.println(customersByName);

    // var customers = customerRepo.getAll();
    // System.out.println(customers);

    // List<Integer> ids = List.of(1, 2, 3);
    // var customersByIds = customerRepo.getByIds(ids);
    // System.out.println(customersByIds);

    // var customerPage = customerRepo.getPage(0, 10);
    // System.out.println(customerPage);

    // var topCountry = customerRepo.getCountryWithMostCustomers();
    // System.out.println(topCountry);

    // var topGenre = customerRepo.getMostPopularGenreFromOne(1);
    // System.out.println(topGenre);
    //
    // var topSpender = customerRepo.getTopSpender();
    // System.out.println(topSpender);
  }
}
