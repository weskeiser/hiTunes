package com.hitunes.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * CRUD interface for managing objects in the persistence store.
 *
 * <p>This interface defines a set of standard CRUD methods for managing objects of the generic type
 * T, with an identifier of the generic type U.
 *
 * <p>The extending interface should specify the actual type of T and U to be used.
 *
 * @author Wes Keiser
 * @version 1.0
 * @since 1.0
 * @param <T> the type of the objects to be managed
 * @param <U> the type of the identifier of the objects
 */
@org.springframework.stereotype.Repository
public interface CrudRepo<T, U> extends Repository<T, U> {

  List<T> getAll();

  void createNew(T object);

  void update(T object);

  void delete(T object);

  void deleteById(U id);

  Optional<T> getById(U id) throws Exception;
}
