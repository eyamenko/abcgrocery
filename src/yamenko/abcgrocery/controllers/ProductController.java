package yamenko.abcgrocery.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.JTableBinding;
import yamenko.abcgrocery.entities.Product;
import yamenko.abcgrocery.models.Model;
import yamenko.abcgrocery.views.ProductView;
import yamenko.abcgrocery.views.components.ProductViewComponents;

/**
 * Product controller
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class ProductController extends Controller<ProductView> {
    // Fields
    private Model<Product> productModel;
    private JButton createProductButton;
    private Binding nameField;
    private Binding salePriceField;
    private JTableBinding productsTable;
    private ObservableList<Product> products;
    private Product product;
    private Product editedProduct;

    // Constructors
    public ProductController(ProductView view, Model<Product> productModel) {
        super(view);
        // initialize fields
        this.productModel = productModel;
        createProductButton = (JButton) view.getComponent(ProductViewComponents.CreateProductButton);
        nameField = (Binding) view.getComponent(ProductViewComponents.NameFieldBinding);
        salePriceField = (Binding) view.getComponent(ProductViewComponents.SalePriceFieldBinding);
        productsTable = (JTableBinding) view.
                getComponent(ProductViewComponents.ProductsTableBinding);
        products = (ObservableList<Product>) view.getComponent(ProductViewComponents.Products);
        product = (Product) view.getComponent(ProductViewComponents.Product);

        // populate lists
        addAllToObservableList(products, productModel.findAll());

        // register listeners and observers
        createProductButton.addActionListener(new CreateButtonPressed());
        productModel.addObserver(this);
        nameField.addBindingListener(new FieldsListener());
        salePriceField.addBindingListener(new FieldsListener());
        productsTable.getColumnBinding(3).setValidator(new ChangedPriceValidator());
        productsTable.getColumnBinding(3).addBindingListener(new TableListener());
    }

    // Listeners
    // button listener
    private class CreateButtonPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            productModel.create(product);
            // reset product fields
            nameField.getSourceProperty().setValue(view, "");
            salePriceField.getSourceProperty().setValue(view, BigDecimal.ZERO);
        }
    }

    // product fields' listener
    private class FieldsListener extends AbstractBindingListener {
        @Override
        public void synced(Binding binding) {
            checkButtonState(createProductButton, product);
        }
    }

    // product table's listener
    private class TableListener extends AbstractBindingListener {
        // table modified
        @Override
        public void targetChanged(Binding binding, PropertyStateEvent event) {
            // get edited product
            editedProduct = (Product) binding.getSourceObject();
        }

        // validation failed
        @Override
        public void syncFailed(Binding binding, final Binding.SyncFailure failure) {
            if (failure.getType() == Binding.SyncFailureType.VALIDATION_FAILED) {
                // display message to the user
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(view, failure.getValidationResult().
                                getDescription());
                    }
                });

            }
        }

        // validation passed and changes were written to the source
        @Override
        public void synced(Binding binding) {
            // write changes to database
            productModel.update((Product) binding.getSourceObject());
        }
    }

    // validate product's changed price
    private class ChangedPriceValidator extends Validator {
        @Override
        public Result validate(Object value) {
            // if new sale price is negative
            BigDecimal newSalePrice = (BigDecimal) value;
            if (newSalePrice.compareTo(BigDecimal.ZERO) < 0) {
                return new Result(null, "A product's sale price must be positive");
            }

            // if product's price has been already changed today
            Date todaysDate = new Date(new java.util.Date().getTime());
            if (editedProduct.getPriceChangedDate().toString().equals(todaysDate.toString())) {
                return new Result(null,
                        "A product’s sale price can be modified only once in any one day");
            }

            return null;
        }
    }

    // Override methods
    @Override
    public void update(Observable o, Object arg) {
        // update lists
        addAllToObservableList(products, productModel.findAll());
    }
}
