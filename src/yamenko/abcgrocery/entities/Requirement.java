package yamenko.abcgrocery.entities;

import java.sql.Date;

/**
 * Requirement entity
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class Requirement extends Entity {
    // Fields
    private Integer quantity;
    private Date date;
    private Product product;

    // Constructors
    public Requirement() {
        this(null, 0, new Date(new java.util.Date().getTime()), null);
    }

    public Requirement(Integer id, Integer quantity, Date date, Product product) {
        super(id);
        this.quantity = quantity;
        this.date = date;
        this.product = product;
    }

    // Getters & Setters
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Override methods
    @Override
    public boolean isValid() {
        // validate product
        if (getProduct() == null) {
            return false;
        }

        // validate date
        if (getDate().after(new java.util.Date())) {
            return false;
        }

        // validate quantity
        if (getQuantity() < 0) {
            return false;
        }

        return true;
    }
}
