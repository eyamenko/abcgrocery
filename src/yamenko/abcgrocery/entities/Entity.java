package yamenko.abcgrocery.entities;

import java.util.Objects;

/**
 * Base class for all entities
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public abstract class Entity {
    // Fields
    private Integer id;

    // Constructors
    public Entity() {
    }

    public Entity(Integer id) {
        this.id = id;
    }

    // Getters & Setters
    public final Integer getId() {
        return id;
    }

    public final void setId(Integer id) {
        this.id = id;
    }

    // Public methods
    public abstract boolean isValid();

    // Override methods
    @Override
    public String toString() {
        return Objects.toString(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Entity) {
            Entity anotherEntity = (Entity) obj;

            return Objects.equals(getId(), anotherEntity.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
