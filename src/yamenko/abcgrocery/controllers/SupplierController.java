package yamenko.abcgrocery.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.observablecollections.ObservableList;
import yamenko.abcgrocery.entities.Product;
import yamenko.abcgrocery.entities.Supplier;
import yamenko.abcgrocery.models.Model;
import yamenko.abcgrocery.models.ProductModel;
import yamenko.abcgrocery.views.SupplierView;
import yamenko.abcgrocery.views.components.SupplierViewComponents;

/**
 * Supplier controller
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class SupplierController extends Controller<SupplierView> {
    // Fields
    private Model<Supplier> supplierModel;
    private Model<Product> productModel;
    private JButton addProductButton;
    private JButton createSupplierButton;
    private Binding addressFieldBinding;
    private Binding contactPersonFieldBinding;
    private Binding contactPhoneFieldBinding;
    private Binding nameFieldBinding;
    private ObservableList<Supplier> suppliers;
    private ObservableList<Product> suppliedProducts;
    private ObservableList<Product> notSuppliedProducts;
    private Supplier supplier;
    private JTable supplierTable;
    private JComboBox notSuppliedProductsComboBox;
    private Supplier selectedSupplier;
    private Product selectedProduct;

    // Constructors
    public SupplierController(SupplierView view, Model<Product> productModel,
            Model<Supplier> supplierModel) {
        super(view);
        // initialize fields
        this.supplierModel = supplierModel;
        this.productModel = productModel;
        addProductButton = (JButton) view.getComponent(SupplierViewComponents.AddProductButton);
        createSupplierButton = (JButton) view.getComponent(
                SupplierViewComponents.CreateSupplierButton);
        addressFieldBinding = (Binding) view.
                getComponent(SupplierViewComponents.AddressFieldBinding);
        contactPersonFieldBinding = (Binding) view.getComponent(
                SupplierViewComponents.ContactPersonFieldBinding);
        contactPhoneFieldBinding = (Binding) view.getComponent(
                SupplierViewComponents.ContactPhoneFieldBinding);
        nameFieldBinding = (Binding) view.getComponent(SupplierViewComponents.NameFieldBinding);
        suppliers = (ObservableList<Supplier>) view.getComponent(SupplierViewComponents.Suppliers);
        suppliedProducts = (ObservableList<Product>) view.getComponent(
                SupplierViewComponents.SuppliedProducts);
        notSuppliedProducts = (ObservableList<Product>) view.getComponent(
                SupplierViewComponents.NotSuppliedProducts);
        supplier = (Supplier) view.getComponent(SupplierViewComponents.Supplier);
        supplierTable = (JTable) view.getComponent(SupplierViewComponents.SupplierTable);
        notSuppliedProductsComboBox = (JComboBox) view.getComponent(
                SupplierViewComponents.NotSuppliedProductsComboBox);

        // populate lists
        addAllToObservableList(suppliers, supplierModel.findAll());

        // register listeners and observers
        supplierModel.addObserver(this);
        productModel.addObserver(this);
        createSupplierButton.addActionListener(new CreateButtonPressed());
        addProductButton.addActionListener(new AddButtonPressed());
        nameFieldBinding.addBindingListener(new FieldsListener());
        addressFieldBinding.addBindingListener(new FieldsListener());
        contactPersonFieldBinding.addBindingListener(new FieldsListener());
        contactPhoneFieldBinding.addBindingListener(new FieldsListener());
        supplierTable.getSelectionModel().addListSelectionListener(new TableRowSelected());
        notSuppliedProductsComboBox.addActionListener(new ComboBoxItemSelected());
    }

    // Listeners
    // button listener
    private class CreateButtonPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            supplierModel.create(supplier);
            // reset supplier fields
            nameFieldBinding.getSourceProperty().setValue(view, "");
            addressFieldBinding.getSourceProperty().setValue(view, "");
            contactPersonFieldBinding.getSourceProperty().setValue(view, "");
            contactPhoneFieldBinding.getSourceProperty().setValue(view, "");
        }
    }

    // button listener
    private class AddButtonPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((ProductModel) productModel).createSuppliedProduct(selectedSupplier, selectedProduct);
        }
    }

    // supplier fields' listener
    private class FieldsListener extends AbstractBindingListener {
        @Override
        public void synced(Binding binding) {
            checkButtonState(createSupplierButton, supplier);
        }
    }

    // table selection listener
    private class TableRowSelected implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = supplierTable.getSelectedRow();

            // if row is selected
            if (e.getValueIsAdjusting() && selectedRow >= 0) {
                // get selected supplier
                selectedSupplier = suppliers.get(selectedRow);
                // update products lists
                addAllToObservableList(suppliedProducts,
                        ((ProductModel) productModel).findSuppliedProducts(selectedSupplier));
                addAllToObservableList(notSuppliedProducts,
                        ((ProductModel) productModel).findNotSuppliedProducts(selectedSupplier));
            }
        }
    }

    // combo box listener
    private class ComboBoxItemSelected implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedItem = notSuppliedProductsComboBox.getSelectedIndex();

            // if table row is selected
            if (supplierTable.getSelectedRow() >= 0 && selectedItem >= 0) {
                // get selected product
                selectedProduct = notSuppliedProducts.get(selectedItem);
                // activate button
                addProductButton.setEnabled(true);
            }
        }
    }

    // Override methods
    @Override
    public void update(Observable o, Object arg) {
        // update lists
        // if supplier model was changed
        if (arg instanceof Supplier) {
            addAllToObservableList(suppliers, supplierModel.findAll());
        }

        // if product model was changed
        if (arg instanceof Product) {
            addAllToObservableList(suppliedProducts,
                    ((ProductModel) productModel).findSuppliedProducts(selectedSupplier));
            addAllToObservableList(notSuppliedProducts,
                    ((ProductModel) productModel).findNotSuppliedProducts(selectedSupplier));
        }

        // reset components
        addProductButton.setEnabled(false);
    }
}
