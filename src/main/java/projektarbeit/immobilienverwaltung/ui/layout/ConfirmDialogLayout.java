package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.theme.lumo.Lumo;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;

/**
 * Basisklasse für ConfirmDialoge, die den Dark Mode basierend auf der Konfiguration anwenden.
 */
public class ConfirmDialogLayout extends ConfirmDialog {

    private final ConfigurationService configurationService;

    /**
     * Konstruktor für ConfirmDialogLayout.
     *
     * @param configurationService der ConfigurationService zur Verwaltung der Konfigurationseinstellungen.
     */
    public ConfirmDialogLayout(ConfigurationService configurationService) {
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