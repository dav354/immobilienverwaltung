package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import java.util.List;

/**
 * Utility Klasse zur Konfiguration von Vaadin-Grids.
 */
public class TableUtils {

    /**
     * Konfiguriert ein Grid mit den angegebenen Elementen und passt die Höhe des Grids an die Anzahl der Zeilen an.
     *
     * @param <T>    Der Typ der Elemente im Grid.
     * @param grid   Das zu konfigurierende Grid.
     * @param items  Die Elemente, die im Grid angezeigt werden sollen.
     */
    public static <T> void configureGrid(Grid<T> grid, List<T> items) {
        // Setzt die Elemente im Grid
        grid.setItems(items);

        // Berechnet die Höhe basierend auf der Anzahl der Zeilen
        int rowCount = items.size();
        int rowHeight = 40; // Standardhöhe einer Zeile in Pixel
        int headerHeight = 56; // Höhe des Headers in Pixel
        int footerHeight = 0; // Höhe des Footers

        // Setzt die Höhe des Grids
        grid.setHeight((rowCount * rowHeight + headerHeight + footerHeight) + "px");

        // Stellt sicher, dass die "keine Elemente"-Nachricht nur einmal hinzugefügt wird
        FlexComponent parentLayout = (FlexComponent) grid.getParent().orElse(null);
        if (parentLayout != null) {
            // Entfernt vorhandene "keine Elemente"-Nachricht, falls vorhanden
            parentLayout.getChildren()
                    .filter(component -> component.getId().isPresent() && component.getId().get().equals("no-items-div"))
                    .findFirst()
                    .ifPresent(parentLayout::remove);
        }

        // Zeigt oder verbirgt das Grid basierend auf dem Vorhandensein von Elementen
        grid.setVisible(!items.isEmpty());
    }
}