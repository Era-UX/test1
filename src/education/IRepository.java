package education;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    void add(T item);
    List<T> getAll();
    void update(int id, int newValue);
    void delete(int id);
    //Java: Optional (to avoid null error)
    Optional<T> findById(int id);
}