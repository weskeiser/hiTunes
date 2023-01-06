package com.hitunes.repositories.interfaces;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface CrudRepo<T, U> extends Repository<T, U> {

  List<T> getAll();

  void createNew(T object);

  void update(T object);

  void updateById(U id);

  void delete(T object);

  void deleteById(U id);

  Optional<T> get(T object);

  Optional<T> getById(U id) throws Exception;
}
