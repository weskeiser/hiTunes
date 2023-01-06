package com.hitunes.repositories.interfaces;

import com.hitunes.models.customer.Customer;
import java.util.*;

public interface CustomerRepo extends CrudRepo<Customer, Integer> {

  Optional<Customer> getByName(String name);

  List<Customer> getByIds(List<Integer> ids, int limit, int offset);

  Customer getTopSpender(int id);

  List<String> getMostPopularGenreFromOne(int id);

  String getCountryWithMostCustomers();
}
