package yamenko.abcgrocery.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import yamenko.abcgrocery.db.Parsable;
import yamenko.abcgrocery.entities.PurchaseOrder;
import yamenko.abcgrocery.entities.Quotation;

/**
 * Purchase order model
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class PurchaseOrderModel extends Model<PurchaseOrder> {
    @Override
    protected String getFindAllQuery() {
        return "select * from PURCHASE_ORDER";
    }

    @Override
    protected String getFindQuery(Integer id) {
        return "select * from PURCHASE_ORDER where PURCHASE_ORDER_ID = " + id;
    }

    @Override
    protected String getUpdateQuery(PurchaseOrder item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getDeleteQuery(PurchaseOrder item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getCreateQuery(PurchaseOrder item) {
        return "insert into PURCHASE_ORDER (QUOTATION_ID, TOTAL, DATE) values (" + item.
                getQuotation().getId() + ", " + item.getTotal() + ", '" + item.getDate() + "')";
    }

    @Override
    protected Parsable<PurchaseOrder> getParsable() {
        return new Parsable<PurchaseOrder>() {

            @Override
            public PurchaseOrder parse(List<Object> items) {
                // quotation
                Quotation quotation = new QuotationModel().find((Integer) items.get(1));

                // purchase order
                return new PurchaseOrder((Integer) items.get(0), (BigDecimal) items.get(2),
                        (Date) items.get(3), quotation);
            }
        };
    }
}
