package yamenko.abcgrocery.entities;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Product entity
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class Product extends Entity {
    // Fields
    private String name;
    private BigDecimal salePrice;
    private Integer quantity;
    private Date priceChangedDate;

    // Constructors
    public Product() {
        this(null, "", BigDecimal.ZERO, 0, new Date(new java.util.Date().getTime()));
    }

    public Product(Integer id, String name, BigDecimal salePrice, Integer quantity,
            Date priceChangedDate) {
        super(id);
        this.name = name;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.priceChangedDate = priceChangedDate;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
        // set price changed date to today's date
        Date todaysDate = new Date(new java.util.Date().getTime());
        setPriceChangedDate(todaysDate);
    }

    public Date getPriceChangedDate() {
        return priceChangedDate;
    }

    public void setPriceChangedDate(Date priceChangedDate) {
        this.priceChangedDate = priceChangedDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Override methods
    @Override
    public String toString() {
        return getId() + ": " + name;
    }

    @Override
    public boolean isValid() {
        // validate date
        if (getPriceChangedDate().after(new java.util.Date())) {
            return false;
        }

        // validate quantity
        if (getQuantity() < 0) {
            return false;
        }

        // validate sale price
        if (getSalePrice().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        // validate name
        if (getName().isEmpty()) {
            return false;
        }

        return true;
    }
}
