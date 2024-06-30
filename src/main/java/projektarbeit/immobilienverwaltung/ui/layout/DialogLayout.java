package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.theme.lumo.Lumo;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;

/**
 * Basisklasse für Dialoge, die den Dark Mode basierend auf der Konfiguration anwenden.
 */
public class DialogLayout extends Dialog {

    private final ConfigurationService configurationService;

    /**
     * Konstruktor für DialogLayout.
     *
     * @param configurationService der ConfigurationService zur Verwaltung der Konfigurationseinstellungen.
     */
    public DialogLayout(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        applyDarkMode();
    }

    /**
     * Wendet den aktuellen Dark Mode-Status an.
     */
    private void applyDarkMode() {
        if (configurationService.isDarkMode()) {
            getElement().getThemeList().add(Lumo.DARK);
        } else {
            getElement().getThemeList().remove(Lumo.DARK);
        }
    }
}