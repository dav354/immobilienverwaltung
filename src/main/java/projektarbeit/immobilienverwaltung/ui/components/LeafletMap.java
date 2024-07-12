package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Die LeafletMap-Klasse integriert eine Leaflet-Karte in eine Vaadin-Anwendung.
 * Sie ermöglicht das Hinzufügen von Markern und das Anzeigen dieser Marker auf der Karte.
 */
@JsModule("./map.js")
public class LeafletMap extends VerticalLayout {

    private Div mapContainer;
    private List<Marker> markers;

    /**
     * Konstruktor für die LeafletMap-Klasse.
     * Initialisiert den Kartencontainer und die Liste der Marker.
     */
    public LeafletMap() {
        mapContainer = new Div();
        mapContainer.setId("map");
        mapContainer.setHeight("600px");
        mapContainer.setWidth("100%");
        add(mapContainer);

        markers = new ArrayList<>();
    }

    /**
     * Fügt einen Marker zur Karte hinzu.
     *
     * @param lat   Die Breite des Markers.
     * @param lng   Die Länge des Markers.
     * @param popup Der Popup-Text, der angezeigt wird, wenn der Marker angeklickt wird.
     */
    public void addMarker(double lat, double lng, String popup) {
        markers.add(new Marker(lat, lng, popup));
    }

    /**
     * Wird aufgerufen, wenn die Komponente an die Benutzeroberfläche angehängt wird.
     * Initialisiert die Karte und fügt alle Marker hinzu.
     *
     * @param attachEvent Das AttachEvent-Objekt.
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs(
                "if (window.leafletLoaded) {" +
                        "   window.initializeMap($0, JSON.parse($1));" +
                        "} else {" +
                        "   window.pendingMapInit = () => window.initializeMap($0, JSON.parse($1));" +
                        "}",
                mapContainer.getId().get(),
                markersToJson()
        );
    }

    /**
     * Konvertiert die Liste der Marker in ein JSON-Format, das von der Leaflet-Bibliothek verwendet werden kann.
     *
     * @return Eine Zeichenkette, die die Marker im JSON-Format enthält.
     */
    private String markersToJson() {
        StringBuilder json = new StringBuilder("[");
        for (Marker marker : markers) {
            if (json.length() > 1) {
                json.append(",");
            }
            json.append("{")
                    .append("\"lat\":").append(marker.lat).append(",")
                    .append("\"lng\":").append(marker.lng).append(",")
                    .append("\"popup\":\"").append(marker.popup).append("\"")
                    .append("}");
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Die Marker-Klasse repräsentiert einen einzelnen Marker auf der Karte.
     * Sie enthält die Breite, Länge und den Popup-Text des Markers.
     */
    private static class Marker {
        double lat;
        double lng;
        String popup;

        /**
         * Konstruktor für die Marker-Klasse.
         *
         * @param lat   Die Breite des Markers.
         * @param lng   Die Länge des Markers.
         * @param popup Der Popup-Text des Markers.
         */
        Marker(double lat, double lng, String popup) {
            this.lat = lat;
            this.lng = lng;
            this.popup = popup;
        }
    }
}