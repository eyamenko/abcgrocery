package yamenko.abcgrocery.controllers;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import yamenko.abcgrocery.models.ProductModel;
import yamenko.abcgrocery.models.PurchaseOrderModel;
import yamenko.abcgrocery.models.RequirementModel;
import yamenko.abcgrocery.models.SaleActivityModel;
import yamenko.abcgrocery.models.SupplierModel;
import yamenko.abcgrocery.views.MainView;
import yamenko.abcgrocery.views.ProductView;
import yamenko.abcgrocery.views.RequirementView;
import yamenko.abcgrocery.views.SaleActivityView;
import yamenko.abcgrocery.views.SupplierView;
import yamenko.abcgrocery.views.components.MainViewComponents;

/**
 * Main controller
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class MainController extends Controller<MainView> {
    // Fields
    private JMenuItem productsItem;
    private JMenuItem requirementsItem;
    private JMenuItem saleActivitiesItem;
    private JMenuItem suppliersItem;

    // Constructors
    public MainController(MainView view) {
        super(view);
        // initialize fields
        productsItem = (JMenuItem) view.getComponent(MainViewComponents.ProductsItem);
        requirementsItem = (JMenuItem) view.getComponent(MainViewComponents.RequirementsItem);
        saleActivitiesItem = (JMenuItem) view.getComponent(MainViewComponents.SaleActivitiesItem);
        suppliersItem = (JMenuItem) view.getComponent(MainViewComponents.SuppliersItem);

        // set listeners for items
        productsItem.addActionListener(new ProductsItemPressed());
        suppliersItem.addActionListener(new SuppliersItemPressed());
        saleActivitiesItem.addActionListener(new SaleActivitiesItemPressed());
        requirementsItem.addActionListener(new RequirementsItemPressed());

        // set application's icon
        try {
            Image icon = ImageIO.read(getClass().getResource("/grocery_shop_icon.jpg"));
            view.setIconImage(icon);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // press products item
        productsItem.doClick();

        // make view visible to the user
        view.setVisible(true);
    }

    // Listeners
    private class ProductsItemPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // start product controller
            ProductView productView = new ProductView();
            new ProductController(productView, new ProductModel());

            // make view visible to the user
            makeViewVisible(view, productView);
        }
    }

    private class RequirementsItemPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // start requirement controller
            RequirementView requirementView = new RequirementView();
            new RequirementController(requirementView, new RequirementModel(),
                    new PurchaseOrderModel(), new ProductModel());

            // make view visible to the user
            makeViewVisible(view, requirementView);
        }
    }

    private class SaleActivitiesItemPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // start sale activity controller
            SaleActivityView saleActivityView = new SaleActivityView();
            new SaleActivityController(saleActivityView, new SaleActivityModel(), new ProductModel());

            // make view visible to the user
            makeViewVisible(view, saleActivityView);
        }
    }

    private class SuppliersItemPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // start supplier controller
            SupplierView supplierView = new SupplierView();
            new SupplierController(supplierView, new ProductModel(), new SupplierModel());

            // make view visible to the user
            makeViewVisible(view, supplierView);
        }
    }
}
