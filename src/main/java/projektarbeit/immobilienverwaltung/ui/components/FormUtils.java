package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

/**
 * Utility-Klasse für das Hinzufügen von Details zu einem FormLayout in Vaadin.
 */
public class FormUtils {

    /**
     * Fügt ein Detail zum Formularlayout hinzu.
     *
     * @param formLayout das Formularlayout
     * @param label      das Label für das Detail
     * @param value      der Wert für das Detail
     */
    public static void addDetailToFormLayout(FormLayout formLayout, String label, String value) {
        if (value != null) {
            Div container = new Div();
            container.getStyle().set("display", "flex").set("justify-content", "space-between");
            Span labelSpan = new Span(label);
            Span valueSpan = new Span(value);
            labelSpan.getStyle().set("font-weight", "bold");
            container.add(labelSpan, valueSpan);
            formLayout.add(container);
        }
    }
}