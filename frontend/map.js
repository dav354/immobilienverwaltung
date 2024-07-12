if (!window.leafletLoaded) {
    // Fügt das Leaflet CSS hinzu
    let link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = 'https://unpkg.com/leaflet@1.7.1/dist/leaflet.css';
    document.head.appendChild(link);

    // Fügt das Leaflet JS hinzu
    let script = document.createElement('script');
    script.src = 'https://unpkg.com/leaflet@1.7.1/dist/leaflet.js';
    script.onload = () => {
        window.leafletLoaded = true;

        // Initialisiert die Karte und fügt Marker hinzu
        window.initializeMap = function(containerId, markers) {
            // Setzt die Anfangsposition der Karte auf Frankfurt, Deutschland
            var map = L.map(containerId).setView([50.1109, 8.6821], 6);

            // Fügt die OpenStreetMap-Kacheln hinzu
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);

            // Fügt alle Marker zur Karte hinzu
            markers.forEach(function(marker) {
                L.marker([marker.lat, marker.lng])
                    .addTo(map)
                    .bindPopup(marker.popup);
            });

            // Erzwingt, dass die Karte nach einer kurzen Verzögerung korrekt skaliert wird
            setTimeout(function() {
                map.invalidateSize();
            }, 100);
        };

        // Falls es eine ausstehende Karteninitialisierung gibt, wird diese jetzt ausgeführt
        if (window.pendingMapInit) {
            window.pendingMapInit();
            window.pendingMapInit = null; // Löscht die ausstehende Funktion
        }
    };
    document.head.appendChild(script);
} else {
    // Initialisiert die Karte und fügt Marker hinzu, wenn Leaflet bereits geladen ist
    window.initializeMap = function(containerId, markers) {
        var map = L.map(containerId).setView([50.1109, 8.6821], 6);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(map);

        markers.forEach(function(marker) {
            L.marker([marker.lat, marker.lng])
                .addTo(map)
                .bindPopup(marker.popup);
        });

        setTimeout(function() {
            map.invalidateSize();
        }, 100);
    };
}