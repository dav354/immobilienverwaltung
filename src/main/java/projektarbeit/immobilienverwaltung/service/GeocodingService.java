package projektarbeit.immobilienverwaltung.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Ein Dienst zum Abrufen von Geokoordinaten für eine gegebene Adresse
 * unter Verwendung der Nominatim-API von OpenStreetMap.
 */
public class GeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1&limit=1";

    /**
     * Ruft die Geokoordinaten für eine gegebene Adresse ab.
     *
     * @param address Die Adresse, für die die Geokoordinaten abgerufen werden sollen.
     * @return Ein Array mit der Breiten- und Längengrad.
     * @throws IOException Wenn keine Koordinaten für die gegebene Adresse gefunden werden oder ein Fehler bei der Anfrage auftritt.
     */
    public double[] getCoordinates(String address) throws IOException {
        String url = String.format(NOMINATIM_URL, address.replace(" ", "+"));
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getEntity().getContent());
                if (root.isArray() && !root.isEmpty()) {
                    JsonNode location = root.get(0);
                    double latitude = location.get("lat").asDouble();
                    double longitude = location.get("lon").asDouble();
                    return new double[]{latitude, longitude};
                } else {
                    throw new IOException("Keine Koordinaten für die angegebene Adresse gefunden: " + address);
                }
            }
        }
    }
}