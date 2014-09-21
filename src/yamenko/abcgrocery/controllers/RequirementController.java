package yamenko.abcgrocery.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import yamenko.abcgrocery.entities.Requirement;
import yamenko.abcgrocery.models.Model;
import yamenko.abcgrocery.models.ProductModel;
import yamenko.abcgrocery.models.PurchaseOrderModel;
import yamenko.abcgrocery.models.QuotationModel;
import yamenko.abcgrocery.models.SupplierModel;
import yamenko.abcgrocery.views.QuotationView;
import yamenko.abcgrocery.views.RequirementView;
import yamenko.abcgrocery.views.components.RequirementViewComponents;

/**
 * Requirement controller
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class RequirementController extends Controller<RequirementView> {
    // Fields
    private Model<Requirement> requirementModel;
    private Model<PurchaseOrder> purchaseOrderModel;
    private Model<Product> productModel;
    private ObservableList<Requirement> requirements;
    private ObservableList<Product> products;
    private ObservableList<PurchaseOrder> purchaseOrders;
    private Requirement requirement;
    private JButton newRequirementButton;
    private JComboBox productsComboBox;
    private JButton quotationsButton;
    private JTable requirementsTable;
    private Binding quantityField;
    private Requirement selectedRequirement;

    // Constructors
    public RequirementController(RequirementView view, Model<Requirement> requirementModel,
            Model<PurchaseOrder> purchaseOrderModel, Model<Product> productModel) {
        super(view);
        // initialaize fields
        this.requirementModel = requirementModel;
        this.purchaseOrderModel = purchaseOrderModel;
        this.productModel = productModel;
        requirements = (ObservableList<Requirement>) view.getComponent(
                RequirementViewComponents.Requirements);
        products = (ObservableList<Product>) view.getComponent(RequirementViewComponents.Products);
        purchaseOrders = (ObservableList<PurchaseOrder>) view.getComponent(
                RequirementViewComponents.PurchaseOrders);
        requirement = (Requirement) view.getComponent(RequirementViewComponents.Requirement);
        newRequirementButton = (JButton) view.getComponent(
                RequirementViewComponents.NewRequirementButton);
        productsComboBox = (JComboBox) view.getComponent(RequirementViewComponents.ProductsComboBox);
        quotationsButton = (JButton) view.getComponent(RequirementViewComponents.QuotationsButton);
        requirementsTable = (JTable) view.getComponent(RequirementViewComponents.RequirementsTable);
        quantityField = (Binding) view.getComponent(RequirementViewComponents.QuantityField);

        // populate lists
        addAllToObservableList(requirements, requirementModel.findAll());
        addAllToObservableList(purchaseOrders, purchaseOrderModel.findAll());
        addAllToObservableList(products, productModel.findAll());

        // register listeners and observers
        requirementModel.addObserver(this);
        productsComboBox.addActionListener(new ProductSelected());
        quantityField.addBindingListener(new FieldListener());
        newRequirementButton.addActionListener(new NewRequirementPressed());
        requirementsTable.getSelectionModel().addListSelectionListener(new RequirementSelected());
        quotationsButton.addActionListener(new QuotationsPressed());
    }

    // Listeners
    // combo box listener
    private class ProductSelected implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedItem = productsComboBox.getSelectedIndex();

            if (selectedItem >= 0) {
                // set selected product to requirement
                requirement.setProduct(products.get(selectedItem));
                checkButtonState(newRequirementButton, requirement);
            }
        }
    }

    // button listener
    private class NewRequirementPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            requirementModel.create(requirement);
            // reset quantity field
            quantityField.getSourceProperty().setValue(view, 0);
        }
    }

    // button listener
    private class QuotationsPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // start quotation controller
            QuotationView quotationView = new QuotationView();
            new QuotationController(quotationView, selectedRequirement, new QuotationModel(),
                    new SupplierModel(), new PurchaseOrderModel(), new ProductModel());

            // make view visible to the user
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            makeViewVisible(frame, quotationView);
        }
    }

    // field listener
    private class FieldListener extends AbstractBindingListener {
        @Override
        public void synced(Binding binding) {
            checkButtonState(newRequirementButton, requirement);
        }
    }

    // table selection listener
    private class RequirementSelected implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = requirementsTable.getSelectedRow();

            // if row is selected
            if (e.getValueIsAdjusting() && selectedRow >= 0) {
                // get selected requirement
                selectedRequirement = requirements.get(selectedRow);
                // activate button
                quotationsButton.setEnabled(true);
            }
        }
    }

    // Override methods
    @Override
    public void update(Observable o, Object arg) {
        // update lists
        addAllToObservableList(requirements, requirementModel.findAll());

        // reset components
        quotationsButton.setEnabled(false);
    }
}
