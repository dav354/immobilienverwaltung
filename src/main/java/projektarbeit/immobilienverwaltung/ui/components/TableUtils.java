package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.function.ValueProvider;

import java.util.Collection;
import java.util.List;

/**
 * Utility Klasse zur Konfiguration von Vaadin-Grids.
 */
public class TableUtils {

    /**
     * Konfiguriert ein Grid mit den angegebenen Elementen und passt die Höhe des Grids an die Anzahl der Zeilen an.
     *
     * @param <T>   Der Typ der Elemente im Grid.
     * @param grid  Das zu konfigurierende Grid.
     * @param items Die Elemente, die im Grid angezeigt werden sollen.
     */
    public static <T> void configureGrid(Grid<T> grid, List<T> items, int rowHeight) {
        configureCommonGridSettings(grid, items.size(), rowHeight);
        grid.setItems(items);
    }

    /**
     * Konfiguriert ein TreeGrid mit den angegebenen Elementen und passt die Höhe des Grids an die Anzahl der Zeilen an.
     *
     * @param <T>           Der Typ der Elemente im TreeGrid.
     * @param treeGrid      Das zu konfigurierende TreeGrid.
     * @param items         Die Elemente, die im TreeGrid angezeigt werden sollen.
     * @param childProvider Die Funktion, die die Kinder eines Elements bereitstellt.
     */
    public static <T> void configureTreeGrid(TreeGrid<T> treeGrid, List<T> items, int rowHeight, ValueProvider<T, Collection<T>> childProvider) {
        configureCommonGridSettings(treeGrid, items.size(), rowHeight);
        treeGrid.setItems(items, childProvider);
    }

    /**
     * Konfiguriert gemeinsame Einstellungen für Grids und TreeGrids.
     *
     * @param <T>       Der Typ der Elemente im Grid.
     * @param grid      Das zu konfigurierende Grid.
     * @param rowCount  Die Anzahl der Zeilen im Grid.
     * @param rowHeight Die Höhe jeder Zeile im Grid.
     */
    private static <T> void configureCommonGridSettings(Grid<T> grid, int rowCount, int rowHeight) {
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        int headerHeight = 58; // Höhe des Headers in Pixel
        int footerHeight = 0; // Höhe des Footers

        // Setzt die Höhe des Grids
        grid.setHeight((rowCount * rowHeight + headerHeight + footerHeight) + "px");
    }

    /**
     * Erstellt eine benutzerdefinierte Überschrift für die Tabelle.
     *
     * @param text der Text der Überschrift.
     * @return das konfigurierte Div-Element mit der benutzerdefinierten CSS-Klasse.
     */
    public static Html createCustomHeader(String text) {
        return new Html("<div class='custom-header'>" + text + "</div>");
    }

}