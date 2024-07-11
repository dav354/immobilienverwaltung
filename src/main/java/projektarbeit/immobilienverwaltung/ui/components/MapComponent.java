package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Span;
import projektarbeit.immobilienverwaltung.model.Wohnung;

/**
 * Eine Komponente, die eine Übersichtskarte mit Markierungen für alle angegebenen Wohnungen anzeigt.
 * Wenn keine Koordinaten gefunden werden, wird ein Hinweistext angezeigt.
 */
public class MapComponent extends Div {

    private static final String OSM_BASE_URL = "https://www.openstreetmap.org/export/embed.html?bbox=";
    private static final String OSM_MARKER_URL = "&marker=";

    public MapComponent(Wohnung wohnung) {
        setupMap(wohnung);
    }

    /**
     * Initialisiert die Karte für eine einzelne Wohnung und zeigt sie an.
     * Wenn keine Koordinaten gefunden werden, wird ein Hinweistext angezeigt.
     *
     * @param wohnung Die Wohnung, für die die Karte angezeigt werden soll.
     */
    private void setupMap(Wohnung wohnung) {
        if (wohnung.getLatitude() == null || wohnung.getLongitude() == null) {
            Div container = new Div();
            container.getStyle().set("display", "flex");
            container.getStyle().set("justify-content", "center");
            container.getStyle().set("align-items", "center");
            container.getStyle().set("height", "400px"); // Höhe der Karte
            container.getStyle().set("width", "100%"); // Breite der Karte
            container.getStyle().set("border", "2px solid #ccc");
            container.getStyle().set("box-sizing", "border-box");

            Span noCoordinatesMessage = new Span("Keine Koordinaten für die angegebenen Adressen gefunden.");
            container.add(noCoordinatesMessage);
            add(container);
        } else {
            String mapUrl = buildMapUrlForWohnung(wohnung);
            IFrame mapFrame = new IFrame(mapUrl);
            mapFrame.setWidth("100%");
            mapFrame.setHeight("400px");
            mapFrame.getElement().getStyle().set("border", "0");
            add(mapFrame);
        }
    }

    /**
     * Erzeugt die URL für die Karte basierend auf den Koordinaten einer einzelnen Wohnung.
     *
     * @param wohnung Die Wohnung, für die die Koordinaten abgerufen werden sollen.
     * @return Die URL für die Karte.
     */
    private String buildMapUrlForWohnung(Wohnung wohnung) {
        double latitude = wohnung.getLatitude();
        double longitude = wohnung.getLongitude();
        double boundingBoxSize = 0.01; // Größe des Bounding Box

        String boundingBox = (longitude - boundingBoxSize) + "," + (latitude - boundingBoxSize) + "," + (longitude + boundingBoxSize) + "," + (latitude + boundingBoxSize);
        String marker = latitude + "," + longitude;

        return OSM_BASE_URL + boundingBox + OSM_MARKER_URL + marker;
    }
}