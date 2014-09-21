package yamenko.abcgrocery.controllers;

import java.awt.Container;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.jdesktop.observablecollections.ObservableList;
import yamenko.abcgrocery.entities.Entity;
import yamenko.abcgrocery.views.View;

/**
 * Base class of all controllers
 *
 * @param <T1> view
 * @author Eugene Yamenko <yamenko@me.com>
 */
public abstract class Controller<T1 extends View<? extends Enum>> implements Observer {
    // Fields
    protected T1 view;

    // Constructors
    public Controller(T1 view) {
        this.view = view;
    }

    // Helper methods
    protected <T2> void addAllToObservableList(ObservableList<T2> oList, List<T2> list) {
        oList.clear();
        oList.addAll(list);
    }

    protected void checkButtonState(JButton button, Entity entity) {
        // if entity is valid
        if (entity.isValid()) {
            // enable the button
            button.setEnabled(true);
        } else {
            // disable the button
            button.setEnabled(false);
        }
    }

    protected void makeViewVisible(JFrame frame, Container container) {
        frame.setContentPane(container);
        frame.validate();
        frame.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
