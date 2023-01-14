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
public interface CrudRepo<T, U> extends Repository<T, U> {

  /**
   * Returns all instances of the type in a list.
   *
   * @return all entities
   * @throws SQLException if there is an error with the database request.
   */
  List<T> getAll() throws SQLException;

  /**
   * Inserts a new entry of the entity in the database.
   *
   * @param entity containing all the required fields
   * @throws SQLException if there is an error with the database request.
   */
  void createNew(T entity) throws SQLException;

  /**
   * Updates the entity entry in the database by overwriting it. Entity must contain a valid ID.
   *
   * @param entity containing a valid ID.
   * @throws SQLException if there is an error with the database request.
   */
  void update(T entity) throws SQLException;

  /**
   * Deletes the entity entry in the database. Entity must contain a valid ID.
   *
   * @param entity containing a valid ID.
   * @throws SQLException if there is an error with the database request.
   */
  void delete(T entity) throws SQLException;

  /**
   * Deletes the entity entry in the database.
   *
   * @param id must be a valid ID
   * @throws SQLException if there is an error with the database request.
   */
  void deleteById(U id) throws SQLException;

  /**
   * Retrieves an entity by its id.
   *
   * @param id must not be null.
   * @return the entity with the given id or {@literal Optional#empty()} if none found.
   * @throws SQLException if there is an error with the database request.
   */
  Optional<T> getById(U id) throws SQLException;
}
