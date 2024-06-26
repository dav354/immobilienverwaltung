package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Span;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Eine Komponente, die eine Übersichtskarte mit Markierungen für alle angegebenen Wohnungen anzeigt.
 * Wenn keine Koordinaten gefunden werden, wird ein Hinweistext angezeigt.
 */
public class MapComponent extends Div {

    private static final String OSM_BASE_URL = "https://www.openstreetmap.org/export/embed.html?bbox=";
    private static final String OSM_MARKER_URL = "&marker=";

    public MapComponent(Wohnung wohnung) {
        setupMapForSingleWohnung(wohnung);
    }

    public MapComponent(List<Wohnung> wohnungen) {
        setupMapForWohnungen(wohnungen);
    }

    /**
     * Initialisiert die Karte für eine einzelne Wohnung und zeigt sie an.
     * Wenn keine Koordinaten gefunden werden, wird ein Hinweistext angezeigt.
     *
     * @param wohnung Die Wohnung, für die die Karte angezeigt werden soll.
     */
    private void setupMapForSingleWohnung(Wohnung wohnung) {
        if (wohnung.getLatitude() == null || wohnung.getLongitude() == null) {
            add(new Span("Keine Koordinaten für die angegebene Adresse gefunden."));
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
     * Initialisiert die Karte für eine Liste von Wohnungen und zeigt sie an.
     * Wohnungen ohne Koordinaten werden übersprungen.
     *
     * @param wohnungen Eine Liste von Wohnungen, für die die Karte angezeigt werden soll.
     */
    private void setupMapForWohnungen(List<Wohnung> wohnungen) {
        List<Wohnung> validWohnungen = wohnungen.stream()
                .filter(wohnung -> wohnung.getLatitude() != null && wohnung.getLongitude() != null)
                .collect(Collectors.toList());

        if (validWohnungen.isEmpty()) {
            add(new Span("Keine Koordinaten für die angegebenen Adressen gefunden."));
        } else {
            String mapUrl = buildMapUrlForWohnungen(validWohnungen);
            IFrame mapFrame = new IFrame(mapUrl);
            mapFrame.setWidth("100%");
            mapFrame.setHeight("600px");
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

    /**
     * Erzeugt die URL für die Karte basierend auf den Koordinaten einer Liste von Wohnungen.
     *
     * @param wohnungen Eine Liste von Wohnungen, für die die Koordinaten abgerufen werden sollen.
     * @return Die URL für die Karte.
     */
    private String buildMapUrlForWohnungen(List<Wohnung> wohnungen) {
        double minLat = Double.MAX_VALUE, minLon = Double.MAX_VALUE, maxLat = Double.MIN_VALUE, maxLon = Double.MIN_VALUE;
        StringBuilder markers = new StringBuilder();

        for (Wohnung wohnung : wohnungen) {
            double latitude = wohnung.getLatitude();
            double longitude = wohnung.getLongitude();
            minLat = Math.min(minLat, latitude);
            minLon = Math.min(minLon, longitude);
            maxLat = Math.max(maxLat, latitude);
            maxLon = Math.max(maxLon, longitude);
            markers.append(OSM_MARKER_URL).append(latitude).append("%2C").append(longitude);
        }

        String boundingBox = minLon + "," + minLat + "," + maxLon + "," + maxLat;
        return OSM_BASE_URL + boundingBox + markers;
    }
}