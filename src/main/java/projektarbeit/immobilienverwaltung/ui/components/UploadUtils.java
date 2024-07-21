package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import projektarbeit.immobilienverwaltung.service.DokumentService;

import java.io.InputStream;

/**
 * Dienstprogrammklasse für das Erstellen von Upload-Schaltflächen in Vaadin.
 */
public class UploadUtils {

    /**
     * Erstellt ein Layout mit einer Upload-Schaltfläche.
     *
     * @param buttonText      Der Text, der auf der Upload-Schaltfläche angezeigt wird.
     * @param dokumentService Der Dienst zum Verarbeiten des Datei-Uploads.
     * @param entity          Das Objekt (Wohnung oder Mieter), zu dem das hochgeladene Dokument gehört.
     * @param refreshView     Die Methode zum Aktualisieren der Ansicht nach dem Upload.
     * @return Ein HorizontalLayout, das die Upload-Schaltfläche enthält.
     */
    public static HorizontalLayout createUploadButton(String buttonText, DokumentService dokumentService, Object entity, Runnable refreshView) {
        HorizontalLayout uploadLayout = new HorizontalLayout();

        // Erstellen eines Speicherpuffers für die Handhabung von Datei-Uploads
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("application/pdf", "image/*");

        // Erstellen und Stylen der Upload-Schaltfläche
        Button uploadButton = new Button(buttonText, new Icon(VaadinIcon.UPLOAD));
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(uploadButton);

        // Stylen der Upload-Komponente
        upload.setDropAllowed(false); // Deaktivieren des Drop-Bereichs
        upload.getElement().getStyle().set("box-shadow", "none");
        upload.getElement().getStyle().set("border", "none");

        upload.addSucceededListener(event -> {
            InputStream inputStream = buffer.getInputStream();
            dokumentService.handleFileUpload(event.getFileName(), inputStream, event.getMIMEType(), entity, refreshView);
        });

        uploadLayout.add(upload);
        return uploadLayout;
    }
}