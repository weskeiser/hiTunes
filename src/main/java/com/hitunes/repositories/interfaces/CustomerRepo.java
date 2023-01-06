package com.hitunes.repositories.interfaces;

import com.hitunes.models.customer.Customer;
import java.util.*;

public interface CustomerRepo extends CrudRepo<Customer, Integer> {

  List<Customer> getByName(String lastName, String firstName);

  List<Customer> getByIds(List<Integer> ids);

  List<Customer> getPage(int offset, int limit);

  Customer getTopSpender(int id);

  List<String> getMostPopularGenreFromOne(int id);

  String getCountryWithMostCustomers();
}
