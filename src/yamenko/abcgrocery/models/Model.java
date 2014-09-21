package yamenko.abcgrocery.models;

import java.util.List;
import java.util.Observable;
import yamenko.abcgrocery.db.DBManager;
import yamenko.abcgrocery.db.Parsable;
import yamenko.abcgrocery.entities.Entity;

/**
 * Base class for all models
 *
 * @param <T> entity
 * @author Eugene Yamenko <yamenko@me.com>
 */
public abstract class Model<T extends Entity> extends Observable {
    // Fields
    protected DBManager<T> dbManager;

    // Constructors
    public Model() {
        dbManager = new DBManager<>();
    }

    // Protected methods
    protected void notifyObservers(T entity) {
        setChanged();
        super.notifyObservers(entity);
    }

    // Public methods
    /**
     * Find all instances
     *
     * @return list of found instances
     */
    public final List<T> findAll() {
        return dbManager.executeQuery(getFindAllQuery(), getParsable());
    }

    /**
     * Find instance by id
     *
     * @param id to be found
     * @return found instance or null if instance was not found
     */
    public final T find(Integer id) {
        List<T> items = dbManager.executeQuery(getFindQuery(id), getParsable());

        // if list is not empty
        if (!items.isEmpty()) {
            return items.get(0); // return the first item
        }

        return null;
    }

    /**
     * Update instance
     *
     * @param item to be updated
     * @return updated instance
     */
    public final T update(T item) {
        // if item is valid
        if (item.isValid()) {
            dbManager.executeUpdate(getUpdateQuery(item));
            notifyObservers(item);
        }

        return item;
    }

    /**
     * Delete instance
     *
     * @param item to be deleted
     * @return deleted instance
     */
    public final T delete(T item) {
        dbManager.executeUpdate(getDeleteQuery(item));
        notifyObservers(item);

        return item;
    }

    /**
     * Create instance
     *
     * @param item to be created
     * @return created instance
     */
    public final T create(T item) {
        // if item is valid
        if (item.isValid()) {
            dbManager.executeUpdate(getCreateQuery(item));
            notifyObservers(item);
        }

        return item;
    }

    // Abstract methods
    protected abstract String getFindAllQuery();

    protected abstract String getFindQuery(Integer id);

    protected abstract String getUpdateQuery(T item);

    protected abstract String getDeleteQuery(T item);

    protected abstract String getCreateQuery(T item);

    protected abstract Parsable<T> getParsable();
}
