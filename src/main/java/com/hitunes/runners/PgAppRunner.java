package com.hitunes.runners;

import com.hitunes.repositories.interfaces.CustomerRepo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PgAppRunner implements ApplicationRunner {
  private final CustomerRepo customerRepo;

  public PgAppRunner(CustomerRepo customerRepo) {
    this.customerRepo = customerRepo;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    // Optional<Customer> customer = customerRepo.getById(1);
    // System.out.println(customer);

    // var customersByName = customerRepo.getByName("Gonçalves", "Luís");
    // System.out.println(customersByName);

    // var customers = customerRepo.getAll();
    // System.out.println(customers);

    // List<Integer> ids = List.of(1, 2, 3);
    // var customersByIds = customerRepo.getByIds(ids);
    // System.out.println(customersByIds);

    // var customerPage = customerRepo.getPage(0, 10);
    // System.out.println(customerPage);
  }
}
