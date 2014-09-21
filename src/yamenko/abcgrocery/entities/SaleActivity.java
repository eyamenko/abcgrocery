package yamenko.abcgrocery.entities;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * SaleActitivy entity
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class SaleActivity extends Entity {
    // Fields
    private Date date;
    private BigDecimal total;

    // Constructors
    public SaleActivity() {
        this(null, new Date(new java.util.Date().getTime()), BigDecimal.ZERO);
    }

    public SaleActivity(Integer id, Date date, BigDecimal total) {
        super(id);
        this.date = date;
        this.total = total;
    }

    // Getters & Setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    // Override methods
    @Override
    public boolean isValid() {
        // validate date
        if (getDate().after(new java.util.Date())) {
            return false;
        }

        // validate total
        if (getTotal().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        return true;
    }
}
