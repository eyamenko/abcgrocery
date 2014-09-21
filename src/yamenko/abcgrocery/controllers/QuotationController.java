package yamenko.abcgrocery.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.observablecollections.ObservableList;
import yamenko.abcgrocery.entities.Product;
import yamenko.abcgrocery.entities.PurchaseOrder;
import yamenko.abcgrocery.entities.Quotation;
import yamenko.abcgrocery.entities.Requirement;
import yamenko.abcgrocery.entities.Supplier;
import yamenko.abcgrocery.models.Model;
import yamenko.abcgrocery.models.QuotationModel;
import yamenko.abcgrocery.models.SupplierModel;
import yamenko.abcgrocery.views.QuotationView;
import yamenko.abcgrocery.views.components.QuotationViewComponents;

/**
 * Quotation controller
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class QuotationController extends Controller<QuotationView> {
    // Fields
    private Model<Quotation> quotationModel;
    private Model<Supplier> supplierModel;
    private Model<PurchaseOrder> purchaseOrderModel;
    private Model<Product> productModel;
    private Requirement requirement;
    private Binding unitPriceField;
    private Binding quantityField;
    private JButton createQuotationButton;
    private JButton purchaseQuotationButton;
    private JTable quotationTable;
    private JComboBox suppliersComboBox;
    private ObservableList<Quotation> quotations;
    private ObservableList<Supplier> suppliers;
    private Quotation quotation;
    private Quotation selectedQuotation;

    // Constructors
    public QuotationController(QuotationView view, Requirement requirement,
            Model<Quotation> quotationModel, Model<Supplier> supplierModel,
            Model<PurchaseOrder> purchaseOrderModel, Model<Product> productModel) {
        super(view);
        // initialize fields
        this.quotationModel = quotationModel;
        this.supplierModel = supplierModel;
        this.requirement = requirement;
        this.purchaseOrderModel = purchaseOrderModel;
        this.productModel = productModel;
        unitPriceField = (Binding) view.getComponent(QuotationViewComponents.UnitPriceField);
        quantityField = (Binding) view.getComponent(QuotationViewComponents.QuantityField);
        createQuotationButton = (JButton) view.getComponent(
                QuotationViewComponents.CreateQuotationButton);
        purchaseQuotationButton = (JButton) view.getComponent(
                QuotationViewComponents.PurchaseQuotationButton);
        quotationTable = (JTable) view.getComponent(QuotationViewComponents.QuotationTable);
        suppliersComboBox = (JComboBox) view.getComponent(QuotationViewComponents.SuppliersComboBox);
        quotations = (ObservableList<Quotation>) view.getComponent(
                QuotationViewComponents.Quotations);
        suppliers = (ObservableList<Supplier>) view.getComponent(QuotationViewComponents.Suppliers);
        quotation = (Quotation) view.getComponent(QuotationViewComponents.Quotation);
        quotation.setRequirement(requirement); // assign supplied requirement to the quotation

        // populate lists
        addAllToObservableList(quotations, ((QuotationModel) quotationModel).findAll(requirement));
        addAllToObservableList(suppliers, ((SupplierModel) supplierModel).findAll(requirement));

        // register listeners and observers
        quotationModel.addObserver(this);
        supplierModel.addObserver(this);
        suppliersComboBox.addActionListener(new SuppliersComboBoxListener());
        unitPriceField.addBindingListener(new FieldsListener());
        quantityField.addBindingListener(new FieldsListener());
        createQuotationButton.addActionListener(new CreateQuotationPressed());
        quotationTable.getSelectionModel().addListSelectionListener(new QuotationsTableListener());
        purchaseQuotationButton.addActionListener(new PurchaseQuotationPressed());
    }

    // Listeners
    // combo box listener
    private class SuppliersComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedItem = suppliersComboBox.getSelectedIndex();

            if (selectedItem >= 0) {
                // set selected supplier to quotation
                quotation.setSupplier(suppliers.get(selectedItem));
                checkButtonState(createQuotationButton, quotation);
            }
        }
    }

    // button listener
    private class PurchaseQuotationPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // increase product's quantity
            Product product = selectedQuotation.getRequirement().getProduct();
            Integer newQuantity = product.getQuantity() + selectedQuotation.getQuantity();
            product.setQuantity(newQuantity);

            // calculate total and create purchase order
            BigDecimal total = selectedQuotation.getUnitPrice().multiply(new BigDecimal(
                    selectedQuotation.getQuantity()));
            // construct purchase order
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setTotal(total);
            purchaseOrder.setQuotation(selectedQuotation);

            // write changes to database
            purchaseOrderModel.create(purchaseOrder);
            productModel.update(product);

            // navigate user to requirement's controller
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            frame.getJMenuBar().getMenu(0).getItem(2).doClick();
        }
    }

    // table selection listener
    private class QuotationsTableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = quotationTable.getSelectedRow();

            if (e.getValueIsAdjusting() && selectedRow >= 0) {
                // get selected requirement
                selectedQuotation = quotations.get(selectedRow);
                // activate button
                purchaseQuotationButton.setEnabled(true);
            }
        }
    }

    // button listener
    private class CreateQuotationPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            quotationModel.create(quotation);
            // reset quotation fields
            quantityField.getSourceProperty().setValue(view, 0);
            unitPriceField.getSourceProperty().setValue(view, BigDecimal.ZERO);
        }
    }

    // fields listener
    private class FieldsListener extends AbstractBindingListener {
        @Override
        public void synced(Binding binding) {
            checkButtonState(createQuotationButton, quotation);
        }
    }

    // Override methods
    @Override
    public void update(Observable o, Object arg) {
        // update lists
        addAllToObservableList(quotations, ((QuotationModel) quotationModel).findAll(requirement));
        addAllToObservableList(suppliers, ((SupplierModel) supplierModel).findAll(requirement));

        // reset components
        createQuotationButton.setEnabled(false);
        purchaseQuotationButton.setEnabled(false);
    }
}
