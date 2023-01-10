package com.hitunes.repositories;

import com.hitunes.models.Customer;
import com.hitunes.models.TopCountry;
import com.hitunes.models.TopGenre;
import com.hitunes.models.TopSpender;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends CrudRepo<Customer, Integer> {

  List<Customer> getByName(String lastName, String firstName);

  List<Customer> getByIds(List<Integer> ids);

  List<Customer> getPage(int offset, int limit);

  TopSpender getTopSpender();

  TopGenre getMostPopularGenreFromOne(int id);

  TopCountry getCountryWithMostCustomers();
}
