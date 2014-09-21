package yamenko.abcgrocery.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import yamenko.abcgrocery.db.Parsable;
import yamenko.abcgrocery.entities.Product;
import yamenko.abcgrocery.entities.SaleActivity;
import yamenko.abcgrocery.entities.Supplier;

/**
 * Product model
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class ProductModel extends Model<Product> {
    @Override
    protected String getFindAllQuery() {
        return "select * from PRODUCT";
    }

    @Override
    protected String getFindQuery(Integer id) {
        return "select * from PRODUCT where PRODUCT_ID = " + id;
    }

    @Override
    protected String getUpdateQuery(Product item) {
        return "update PRODUCT set SALE_PRICE = " + item.getSalePrice() + ", QUANTITY = " +
                item.getQuantity() + ", NAME = '" + item.getName() + "', PRICE_CHANGED_DATE = '" +
                item.getPriceChangedDate() + "' where PRODUCT_ID = " + item.getId();
    }

    @Override
    protected String getDeleteQuery(Product item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getCreateQuery(Product item) {
        return "insert into PRODUCT (NAME, SALE_PRICE, QUANTITY, PRICE_CHANGED_DATE) values ('" +
                item.getName() + "', " +
                item.getSalePrice() + ", " +
                item.getQuantity() + ", '" +
                item.getPriceChangedDate() + "')";
    }

    @Override
    protected Parsable<Product> getParsable() {
        return new Parsable<Product>() {

            @Override
            public Product parse(List<Object> items) {
                return new Product((Integer) items.get(0), (String) items.get(1),
                        (BigDecimal) items.get(2), (Integer) items.get(3), (Date) items.get(4));
            }
        };
    }

    // Custom queries
    public List<Product> findSuppliedProducts(Supplier supplier) {
        // find supplied products by referenced supplier
        String query = "select * from PRODUCT where PRODUCT_ID in (select PRODUCT_ID from " +
                "SUPPLIED_PRODUCT where SUPPLIER_ID = " + supplier.getId() + ")";

        return dbManager.executeQuery(query, getParsable());
    }

    public List<Product> findNotSuppliedProducts(Supplier supplier) {
        // find not supplied products by referenced supplier
        String query = "select * from PRODUCT where PRODUCT_ID not in (select PRODUCT_ID from " +
                "SUPPLIED_PRODUCT where SUPPLIER_ID = " + supplier.getId() + ")";

        return dbManager.executeQuery(query, getParsable());
    }

    public Product createSuppliedProduct(Supplier supplier, Product product) {
        // create supplied product query
        String query = "insert into SUPPLIED_PRODUCT (SUPPLIER_ID, PRODUCT_ID) values (" +
                supplier.getId() + ", " + product.getId() + ")";
        dbManager.executeUpdate(query);
        notifyObservers(product);

        return product;
    }

    public Product createSoldProduct(SaleActivity saleActivity, Product product) {
        // create sold product query
        String query = "insert into SOLD_PRODUCT (SALE_ACTIVITY_ID, PRODUCT_ID) values (" +
                saleActivity.getId() + ", " + product.getId() + ")";
        dbManager.executeUpdate(query);
        notifyObservers(product);

        return product;
    }

    public List<Product> findSoldProducts(SaleActivity saleActivity) {
        // find sold products by referenced sale activity
        String query = "select P.* from PRODUCT P, SOLD_PRODUCT SP where SP.SALE_ACTIVITY_ID = " +
                saleActivity.getId() + " and SP.PRODUCT_ID = P.PRODUCT_ID";

        return dbManager.executeQuery(query, getParsable());
    }

    public List<Product> findProductsInStock() {
        // find all products that are in stock
        String query = "select * from PRODUCT where QUANTITY > 0";

        return dbManager.executeQuery(query, getParsable());
    }
}
