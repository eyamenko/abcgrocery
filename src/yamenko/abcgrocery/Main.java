package yamenko.abcgrocery;

import javax.swing.SwingUtilities;
import yamenko.abcgrocery.controllers.MainController;
import yamenko.abcgrocery.views.MainView;

/**
 * Application's starting point
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // start main controller
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainController(new MainView());
            }
        });
    }
}
