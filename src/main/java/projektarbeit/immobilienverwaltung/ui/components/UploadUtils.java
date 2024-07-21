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

public class UploadUtils {

    public static HorizontalLayout createUploadButton(String buttonText, DokumentService dokumentService, Object entity, Runnable refreshView) {
        HorizontalLayout uploadLayout = new HorizontalLayout();

        // Create a memory buffer for handling file uploads
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("application/pdf", "image/*");

        // Create and style the upload button
        Button uploadButton = new Button(buttonText, new Icon(VaadinIcon.UPLOAD));
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(uploadButton);

        // Style the upload component
        upload.setDropAllowed(false); // Disable drop area
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