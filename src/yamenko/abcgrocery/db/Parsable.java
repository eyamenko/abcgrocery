package yamenko.abcgrocery.db;

import java.util.List;

/**
 * Interface for custom parsing
 *
 * @param <T>
 * @author Eugene Yamenko <yamenko@me.com>
 */
public interface Parsable<T> {
    /**
     * Parse items
     *
     * @param items to be parsed
     * @return parsed instance
     */
    T parse(List<Object> items);
}
