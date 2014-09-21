package yamenko.abcgrocery.models;

import java.math.BigDecimal;
import java.util.List;
import yamenko.abcgrocery.db.Parsable;
import yamenko.abcgrocery.entities.Quotation;
import yamenko.abcgrocery.entities.Requirement;
import yamenko.abcgrocery.entities.Supplier;

/**
 * Quotation model
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class QuotationModel extends Model<Quotation> {
    @Override
    protected String getFindAllQuery() {
        return "select * from QUOTATION";
    }

    @Override
    protected String getFindQuery(Integer id) {
        return "select * from QUOTATION where QUOTATION_ID = " + id;
    }

    @Override
    protected String getUpdateQuery(Quotation item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getDeleteQuery(Quotation item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getCreateQuery(Quotation item) {
        return "insert into QUOTATION (SUPPLIER_ID, REQUIREMENT_ID, QUANTITY, UNIT_PRICE) values (" +
                item.getSupplier().getId() + ", " + item.getRequirement().getId() + ", " + item.
                getQuantity() + ", " + item.getUnitPrice() + ")";
    }

    @Override
    protected Parsable<Quotation> getParsable() {
        return new Parsable<Quotation>() {

            @Override
            public Quotation parse(List<Object> items) {
                // supplier
                Supplier supplier = new SupplierModel().find((Integer) items.get(1));

                // requirement
                Requirement requirement = new RequirementModel().find((Integer) items.get(2));

                // quotation
                return new Quotation((Integer) items.get(0), (Integer) items.get(3),
                        (BigDecimal) items.get(4), supplier, requirement);

            }
        };
    }

    // custom queries
    public List<Quotation> findAll(Requirement requirement) {
        // find all quotations by referenced requirement
        String query = "select * from QUOTATION where REQUIREMENT_ID = " + requirement.getId();

        return dbManager.executeQuery(query, getParsable());
    }
}
