package yamenko.abcgrocery.views;

/**
 * Base interface of all views
 *
 * @param <T> view components
 * @author Eugene Yamenko <yamenko@me.com>
 */
public interface View<T extends Enum> {
    /**
     * Get view component
     *
     * @param component to get
     * @return component as an object
     */
    public Object getComponent(T component);
}
