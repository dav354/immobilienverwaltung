package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import java.util.List;

public class TableUtils {

    public static <T> void configureGrid(Grid<T> grid, List<T> items) {
        // Set the items to the grid
        grid.setItems(items);

        // Calculate the height based on the number of rows
        int rowCount = items.size();
        int rowHeight = 40; // Default row height in pixels (adjust as needed)
        int headerHeight = 56; // Header height in pixels (adjust as needed)
        int footerHeight = 0; // Footer height (if any)

        // Set the height of the grid
        grid.setHeight((rowCount * rowHeight + headerHeight + footerHeight) + "px");

        // Ensure the no-items message is added only once
        FlexComponent parentLayout = (FlexComponent) grid.getParent().orElse(null);
        if (parentLayout != null) {
            // Remove existing "no items" message if it exists
            parentLayout.getChildren()
                    .filter(component -> component.getId().isPresent() && component.getId().get().equals("no-items-div"))
                    .findFirst()
                    .ifPresent(parentLayout::remove);
        }

        // Show or hide the grid based on the presence of items
        grid.setVisible(!items.isEmpty());
    }
}