package yamenko.abcgrocery.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import yamenko.abcgrocery.db.Parsable;
import yamenko.abcgrocery.entities.SaleActivity;

/**
 * Sale activity model
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class SaleActivityModel extends Model<SaleActivity> {
    @Override
    protected String getFindAllQuery() {
        return "select * from SALE_ACTIVITY";
    }

    @Override
    protected String getFindQuery(Integer id) {
        return "select * from SALE_ACTIVITY where SALE_ACTIVITY_ID = " + id;
    }

    @Override
    protected String getUpdateQuery(SaleActivity item) {
        return "update SALE_ACTIVITY set DATE = '" + item.getDate() + "', TOTAL = " +
                 item.getTotal() + " where SALE_ACTIVITY_ID = " + item.getId();
    }

    @Override
    protected String getDeleteQuery(SaleActivity item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getCreateQuery(SaleActivity item) {
        return "insert into SALE_ACTIVITY (TOTAL, DATE) values (" + item.getTotal() + ", '" +
                 item.getDate() + "')";
    }

    @Override
    protected Parsable<SaleActivity> getParsable() {
        return new Parsable<SaleActivity>() {

            @Override
            public SaleActivity parse(List<Object> items) {
                return new SaleActivity((Integer) items.get(0), (Date) items.get(1),
                        (BigDecimal) items.get(2));
            }
        };
    }
}
