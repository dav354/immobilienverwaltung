package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ConfirmationDialog extends Dialog {

    public interface ConfirmationListener {
        void onConfirm();
    }

    public ConfirmationDialog(String message, ConfirmationListener confirmationListener) {
        add(new Text(message));

        Button confirmButton = new Button("Delete", event -> {
            confirmationListener.onConfirm();
            close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancel", event -> close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
        add(buttons);
    }
}