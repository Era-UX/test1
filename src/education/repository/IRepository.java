package education.repository;

import education.exception.DatabaseException;
import education.exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    void add(T item) throws DatabaseException;
    List<T> getAll() throws DatabaseException;
    void update(int id, int newValue) throws DatabaseException, EntityNotFoundException;
    void delete(int id) throws DatabaseException, EntityNotFoundException;
    Optional<T> findById(int id) throws DatabaseException;
}