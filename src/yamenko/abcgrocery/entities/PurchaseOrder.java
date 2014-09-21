package yamenko.abcgrocery.entities;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * PurchaseOrder entity
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class PurchaseOrder extends Entity {
    // Fields
    private BigDecimal total;
    private Date date;
    private Quotation quotation;

    // Constructors
    public PurchaseOrder() {
        this(null, BigDecimal.ZERO, new Date(new java.util.Date().getTime()), null);
    }

    public PurchaseOrder(Integer id, BigDecimal total, Date date, Quotation quotation) {
        super(id);
        this.total = total;
        this.date = date;
        this.quotation = quotation;
    }

    // Getters & Setters
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    // Override methods
    @Override
    public String toString() {
        return getId() + ": $" + total + ", " + date;
    }

    @Override
    public boolean isValid() {
        // validate quotation
        if (getQuotation() == null) {
            return false;
        }

        // validate total
        BigDecimal correctTotal = getQuotation().getUnitPrice()
                .multiply(new BigDecimal(getQuotation().getQuantity()));
        if (getTotal().compareTo(correctTotal) != 0) {
            return false;
        }

        // validate date
        if (getDate().after(new java.util.Date())) {
            return false;
        }

        return true;
    }
}
