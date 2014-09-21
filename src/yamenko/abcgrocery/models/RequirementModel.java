package yamenko.abcgrocery.models;

import java.sql.Date;
import java.util.List;
import yamenko.abcgrocery.db.Parsable;
import yamenko.abcgrocery.entities.Product;
import yamenko.abcgrocery.entities.Requirement;

/**
 * Requirement model
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class RequirementModel extends Model<Requirement> {
    @Override
    protected String getFindAllQuery() {
        // find all requirements which haven't been closed yet
        return "select R.* from REQUIREMENT R, PRODUCT P where R.PRODUCT_ID = P.PRODUCT_ID and " +
                "R.REQUIREMENT_ID not in (select Q.REQUIREMENT_ID from QUOTATION Q, PURCHASE_ORDER " +
                "PO where PO.QUOTATION_ID = Q.QUOTATION_ID)";
    }

    @Override
    protected String getFindQuery(Integer id) {
        return "select * from REQUIREMENT where REQUIREMENT_ID = " + id;
    }

    @Override
    protected String getUpdateQuery(Requirement item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getDeleteQuery(Requirement item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getCreateQuery(Requirement item) {
        return "insert into REQUIREMENT (PRODUCT_ID, QUANTITY, DATE) values (" + item.getProduct().
                getId() + ", " + item.getQuantity() + ", '" + item.getDate() + "')";
    }

    @Override
    protected Parsable<Requirement> getParsable() {
        return new Parsable<Requirement>() {

            @Override
            public Requirement parse(List<Object> items) {
                // product
                Product product = new ProductModel().find((Integer) items.get(1));

                // requirement
                return new Requirement((Integer) items.get(0), (Integer) items.get(2),
                        (Date) items.get(3), product);
            }
        };
    }
}
