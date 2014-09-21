package yamenko.abcgrocery.entities;

import java.math.BigDecimal;

/**
 * Quotation entity
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class Quotation extends Entity {
    // Fields
    private Integer quantity;
    private BigDecimal unitPrice;
    private Supplier supplier;
    private Requirement requirement;

    // Constrcutors
    public Quotation() {
        this(null, 0, BigDecimal.ZERO, null, null);
    }

    public Quotation(Integer id, Integer quantity, BigDecimal unitPrice, Supplier supplier,
            Requirement requirement) {
        super(id);
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.requirement = requirement;
    }

    // Getters & Setters
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    // Override methods
    @Override
    public boolean isValid() {
        // validate supplier
        if (getSupplier() == null) {
            return false;
        }

        // validate requirement
        if (getRequirement() == null) {
            return false;
        }

        // validate quantity
        if (getQuantity().compareTo(getRequirement().getQuantity()) < 0) {
            return false;
        }

        // validate unit price
        if (getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        return true;
    }
}
