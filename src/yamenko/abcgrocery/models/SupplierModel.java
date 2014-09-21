package yamenko.abcgrocery.models;

import java.util.List;
import yamenko.abcgrocery.db.Parsable;
import yamenko.abcgrocery.entities.Requirement;
import yamenko.abcgrocery.entities.Supplier;

/**
 * Supplier model
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class SupplierModel extends Model<Supplier> {
    @Override
    protected String getFindAllQuery() {
        return "select * from SUPPLIER";
    }

    @Override
    protected String getFindQuery(Integer id) {
        return "select * from SUPPLIER where SUPPLIER_ID = " + id;
    }

    @Override
    protected String getUpdateQuery(Supplier item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getDeleteQuery(Supplier item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getCreateQuery(Supplier item) {
        return "insert into SUPPLIER (NAME, ADDRESS, CONTACT_PERSON, CONTACT_PHONE) values ('" +
                item.getName() + "', '" + item.getAddress() + "', '" + item.getContactPerson() +
                "', '" + item.getContactPhone() + "')";
    }

    @Override
    protected Parsable<Supplier> getParsable() {
        return new Parsable<Supplier>() {

            @Override
            public Supplier parse(List<Object> items) {
                return new Supplier((Integer) items.get(0), (String) items.get(1),
                        (String) items.get(2), (String) items.get(3), (String) items.get(4));
            }
        };
    }

    // custom queries
    public List<Supplier> findAll(Requirement requirement) {
        // find all suppliers which haven't yet made a quotation and are able to supply required product
        String query = "select * from SUPPLIER where SUPPLIER_ID in (select SUPPLIER_ID from " +
                "SUPPLIED_PRODUCT where PRODUCT_ID = " + requirement.getProduct().getId() +
                ") and SUPPLIER_ID not in (select SUPPLIER_ID from QUOTATION where REQUIREMENT_ID = " +
                 requirement.getId() + ")";

        return dbManager.executeQuery(query, getParsable());
    }
}
