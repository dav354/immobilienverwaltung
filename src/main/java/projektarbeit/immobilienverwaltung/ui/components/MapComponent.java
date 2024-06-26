package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Span;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.GeocodingService;

import java.io.IOException;

/**
 * Eine Komponente, die eine Karte anzeigt, basierend auf der Adresse einer Wohnung.
 * Wenn keine Koordinaten gefunden werden, wird eine Standardkarte angezeigt.
 */
public class MapComponent extends Composite<Div> {

    private static final String OSM_BASE_URL = "https://www.openstreetmap.org/export/embed.html?bbox=";
    private static final String OSM_MARKER_URL = "&marker=";
    private static final String DEFAULT_MAP_URL = "https://www.openstreetmap.org/export/embed.html?bbox=10.986022%2C47.421033%2C10.986022%2C47.421033&marker=47.421033%2C10.986022";

    private final Wohnung wohnung;
    private final GeocodingService geocodingService = new GeocodingService();

    /**
     * Konstruktor für die MapComponent.
     *
     * @param wohnung Die Wohnung, deren Adresse auf der Karte angezeigt werden soll.
     */
    public MapComponent(Wohnung wohnung) {
        this.wohnung = wohnung;
        setupMap();
    }

    /**
     * Initialisiert die Karte und zeigt sie an.
     * Wenn keine Koordinaten gefunden werden, wird eine Standardkarte angezeigt.
     */
    private void setupMap() {
        String mapUrl;
        try {
            mapUrl = buildMapUrl(wohnung);
        } catch (IOException e) {
            mapUrl = DEFAULT_MAP_URL;
            getContent().add(new Span("Keine Koordinaten für die angegebene Adresse gefunden. Zeige Standardkarte."));
        }
        IFrame mapFrame = new IFrame(mapUrl);
        mapFrame.setWidth("100%");
        mapFrame.setHeight("400px");
        mapFrame.getElement().getStyle().set("border", "0");
        getContent().add(mapFrame);
    }

    /**
     * Erzeugt die URL für die Karte basierend auf der Adresse der Wohnung.
     *
     * @param wohnung Die Wohnung, deren Adresse geocodiert werden soll.
     * @return Die URL für die Karte.
     * @throws IOException Wenn keine Koordinaten für die Adresse gefunden werden.
     */
    private String buildMapUrl(Wohnung wohnung) throws IOException {
        String address = buildAddress(wohnung);
        double[] coordinates = geocodingService.getCoordinates(address);
        double latitude = coordinates[0];
        double longitude = coordinates[1];
        double boundingBoxSize = 0.01; // Größe des Bounding-Box nach Bedarf anpassen

        String boundingBox = (longitude - boundingBoxSize) + "," + (latitude - boundingBoxSize) + "," + (longitude + boundingBoxSize) + "," + (latitude + boundingBoxSize);
        String marker = latitude + "," + longitude;

        return OSM_BASE_URL + boundingBox + OSM_MARKER_URL + marker;
    }

    /**
     * Baut die Adresse der Wohnung als String zusammen.
     *
     * @param wohnung Die Wohnung, deren Adresse zusammengebaut werden soll.
     * @return Die Adresse als String.
     */
    private String buildAddress(Wohnung wohnung) {
        return String.format("%s %s, %s, %s, %s", wohnung.getStrasse(), wohnung.getHausnummer(), wohnung.getPostleitzahl(), wohnung.getStadt(), wohnung.getLand().name());
    }
}