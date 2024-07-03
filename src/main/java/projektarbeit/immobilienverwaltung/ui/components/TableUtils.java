package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.function.ValueProvider;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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
    public static <T> void configureGrid(Grid<T> grid, List<T> items, int rowHeight) {
        // Setzt die Elemente im Grid
        grid.setItems(items);
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // Berechnet die Höhe basierend auf der Anzahl der Zeilen
        int rowCount = items.size();
        int headerHeight = 58; // Höhe des Headers in Pixel
        int footerHeight = 0; // Höhe des Footers

        // Setzt die Höhe des Grids
        grid.setHeight((rowCount * rowHeight + headerHeight + footerHeight) + "px");
    }

    /**
     * Konfiguriert ein TreeGrid mit den angegebenen Elementen und passt die Höhe des Grids an die Anzahl der Zeilen an.
     *
     * @param <T>    Der Typ der Elemente im TreeGrid.
     * @param treeGrid Das zu konfigurierende TreeGrid.
     * @param items    Die Elemente, die im TreeGrid angezeigt werden sollen.
     * @param childProvider Die Funktion, die die Kinder eines Elements bereitstellt.
     */
    public static <T> void configureTreeGrid(TreeGrid<T> treeGrid, List<T> items, int rowHeight, ValueProvider<T, Collection<T>> childProvider) {
        // Setzt die Elemente im TreeGrid
        treeGrid.setItems(items, childProvider);
        treeGrid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        treeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        treeGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // Berechnet die Höhe basierend auf der Anzahl der Zeilen
        int rowCount = items.size();
        int headerHeight = 58; // Höhe des Headers in Pixel
        int footerHeight = 0; // Höhe des Footers

        // Setzt die Höhe des TreeGrid
        treeGrid.setHeight((rowCount * rowHeight + headerHeight + footerHeight) + "px");
    }
}