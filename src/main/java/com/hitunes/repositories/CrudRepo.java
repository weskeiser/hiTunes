package com.hitunes.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * Interface for generic CRUD operations on a repository for a specific type.
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

  /**
   * Returns all instances of the type in a list.
   *
   * @return all entities
   * @throws SQLException if there is an error retrieving the customers from the database.
   */
  List<T> getAll() throws SQLException;

  /**
   * @param entity
   * @throws SQLException if there is an error creating the customer.
   */
  void createNew(T entity) throws SQLException;

  void update(T entity) throws SQLException;

  void delete(T entity) throws SQLException;

  void deleteById(U id) throws SQLException;

  /**
   * Retrieves an entity by its id.
   *
   * @param id must not be null.
   * @return the entity with the given id or {@literal Optional#empty()} if none found.
   * @throws SQLException if there is an error retrieving the customer from the database.
   */
  Optional<T> getById(U id) throws SQLException;
}
