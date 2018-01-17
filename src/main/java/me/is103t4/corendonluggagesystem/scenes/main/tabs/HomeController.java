package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

/**
 * Controller for the home tab, not too spectacular
 *
 * @author Tim van Ekert
 */
public class HomeController extends Controller {

    @Override
    public boolean isOpen() {
        return Tabs.HOME.isOpen(0);
    }

}
