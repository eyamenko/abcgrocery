package yamenko.abcgrocery.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.observablecollections.ObservableList;
import yamenko.abcgrocery.entities.Product;
import yamenko.abcgrocery.entities.SaleActivity;
import yamenko.abcgrocery.models.Model;
import yamenko.abcgrocery.models.ProductModel;
import yamenko.abcgrocery.views.SaleActivityView;
import yamenko.abcgrocery.views.components.SaleActivityComponents;

/**
 * Sale activity controller
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class SaleActivityController extends Controller<SaleActivityView> {
    // Fields
    private Model<SaleActivity> saleActivityModel;
    private Model<Product> productModel;
    private JButton addProductButton;
    private JButton newSaleActivityButton;
    private JComboBox productsComboBox;
    private JTable saleActivitiesTable;
    private ObservableList<SaleActivity> saleActivities;
    private ObservableList<Product> products;
    private ObservableList<Product> soldProducts;
    private SaleActivity selectedSaleActivity;
    private Product selectedProduct;

    public SaleActivityController(SaleActivityView view, Model<SaleActivity> saleActivityModel,
            Model<Product> productModel) {
        super(view);
        // initialize fields
        this.saleActivityModel = saleActivityModel;
        this.productModel = productModel;
        addProductButton = (JButton) view.getComponent(SaleActivityComponents.AddProductButton);
        newSaleActivityButton = (JButton) view.getComponent(
                SaleActivityComponents.NewSaleActivityButton);
        productsComboBox = (JComboBox) view.getComponent(SaleActivityComponents.ProductsComboBox);
        saleActivitiesTable = (JTable) view.getComponent(SaleActivityComponents.SaleActivitiesTable);
        saleActivities = (ObservableList<SaleActivity>) view.getComponent(
                SaleActivityComponents.SaleActivities);
        products = (ObservableList<Product>) view.getComponent(SaleActivityComponents.Products);
        soldProducts = (ObservableList<Product>) view.getComponent(
                SaleActivityComponents.SoldProducts);

        // populate lists
        addAllToObservableList(saleActivities, saleActivityModel.findAll());
        addAllToObservableList(products, ((ProductModel) productModel).findProductsInStock());

        // register listeners and observers
        saleActivityModel.addObserver(this);
        productModel.addObserver(this);
        newSaleActivityButton.addActionListener(new NewSaleActivityPressed());
        addProductButton.addActionListener(new AddProductPressed());
        saleActivitiesTable.getSelectionModel().addListSelectionListener(new TableRowSelected());
        productsComboBox.addActionListener(new ComboBoxItemSelected());
    }

    // Listeners
    // button listener
    private class NewSaleActivityPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saleActivityModel.create(new SaleActivity());
        }
    }

    // button listener
    private class AddProductPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // increase total
            BigDecimal newTotal = selectedSaleActivity.getTotal().
                    add(selectedProduct.getSalePrice());
            selectedSaleActivity.setTotal(newTotal);

            // update sale activity
            saleActivityModel.update(selectedSaleActivity);

            // create sold product
            ((ProductModel) productModel).createSoldProduct(selectedSaleActivity, selectedProduct);

            // decrease product's quantity by 1
            Integer newQuantity = selectedProduct.getQuantity() - 1;
            selectedProduct.setQuantity(newQuantity);
            productModel.update(selectedProduct);
        }
    }

    // table selection listener
    private class TableRowSelected implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = saleActivitiesTable.getSelectedRow();

            // if row is selected
            if (selectedRow >= 0 && e.getValueIsAdjusting()) {
                // get selected sale activity
                selectedSaleActivity = saleActivities.get(selectedRow);
                // update products lists
                addAllToObservableList(soldProducts, ((ProductModel) productModel).findSoldProducts(
                        selectedSaleActivity));
            }
        }
    }

    // combo box listener
    private class ComboBoxItemSelected implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedItem = productsComboBox.getSelectedIndex();

            // if table row is selected
            if (saleActivitiesTable.getSelectedRow() >= 0 && selectedItem >= 0) {
                // get selected product
                selectedProduct = products.get(selectedItem);
                // activate button
                addProductButton.setEnabled(true);
            }
        }
    }

    // Override methods
    @Override
    public void update(Observable o, Object arg) {
        // update lists
        // if sale activity model was changed
        if (arg instanceof SaleActivity) {
            addAllToObservableList(saleActivities, saleActivityModel.findAll());
        }

        // if product model was changed
        if (arg instanceof Product) {
            addAllToObservableList(soldProducts, ((ProductModel) productModel).findSoldProducts(
                    selectedSaleActivity));
            addAllToObservableList(products, ((ProductModel) productModel).findProductsInStock());
        }

        // reset components
        addProductButton.setEnabled(false);
    }
}
