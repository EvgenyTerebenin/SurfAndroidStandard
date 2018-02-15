package ru.surfstudio.android.utilktx.util.java;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.surfstudio.android.utilktx.util.Mapper;

/**
 * Обработка коллекций
 */
public class CollectionUtils {

    /**
     * Фильтрует коллекцию по предикату
     */
    public static <T> Collection<T> filter(Collection<T> collection, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        if (collection != null && predicate != null) {
            for (T object : collection) {
                if (predicate.test(object)) {
                    result.add(object);
                }
            }
        }
        return result;
    }

    /**
     * Трансформирует коллекцию, допускается null коллекцию
     */
    public static <T, V> ArrayList<V> mapEmptyIfNull(Collection<T> collection, Mapper<T, V> mapper) {
        return collection != null ? map(collection, mapper) : new ArrayList<>();
    }

    /**
     * Трансформирует коллекцию
     */
    public static <T, V> ArrayList<V> map(Collection<T> collection, Mapper<T, V> mapper) {
        ArrayList<V> result = new ArrayList<>(collection.size());
        for (T origin : collection) {
            result.add(mapper.map(origin));
        }
        return result;
    }

    /**
     * Фильтрует лист по предикату
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return (List<T>) filter((Collection<T>) list, predicate);
    }

    /**
     * @return последний элемент листа или null, если лист пустой
     */
    public static <T> T last(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }

    /**
     * @return первый элемент листа или null, если лист пустой
     */
    public static <T> T first(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * @return второй элемент листа или null, если лист меньшего размера
     */
    public static <T> T second(List<T> list) {
        return list != null && list.size() > 1 ? list.get(1) : null;
    }

    public static int sizeZeroIfNull(Collection collections) {
        return collections != null ? collections.size() : 0;
    }

    public static <T> Set<T> newSet(Collection<T> data) {
        Set<T> set = new HashSet<>(data.size());
        set.addAll(data);
        return set;
    }

    /**
     * Типизированный предикат
     */
    public interface Predicate<T> {
        public boolean test(T t);
    }
}