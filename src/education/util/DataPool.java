package education.util;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// GENERIC CLASS (Требование по Generics)
public class DataPool<T> {
    private List<T> data;

    public DataPool(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    // Фильтрация (например, найти всех старше 20 лет)
    public List<T> filter(Predicate<T> criteria) {
        return data.stream()
                .filter(criteria)
                .collect(Collectors.toList());
    }

    // Сортировка (например, по имени)
    public List<T> sort(Comparator<T> comparator) {
        return data.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public long count() {
        return data.size();
    }
}