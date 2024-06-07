package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * A confirmation dialog component for Vaadin applications.
 * This dialog displays a message and provides "Delete" and "Cancel" buttons.
 * When the "Delete" button is pressed, the provided ConfirmationListener is triggered.
 */
public class ConfirmationDialog extends Dialog {

    /**
     * An interface for handling confirmation actions.
     */
    public interface ConfirmationListener {
        void onConfirm();
    }

    /**
     * Constructs a ConfirmationDialog with the specified message and confirmation listener.
     *
     * @param message                The message to display in the dialog.
     * @param confirmationListener   The listener to invoke when the "Delete" button is pressed.
     */
    public ConfirmationDialog(String message, ConfirmationListener confirmationListener) {
        // Add the message text to the dialog
        add(new Text(message));

        // Create the "Delete" button and add a click listener to trigger the confirmation listener
        Button confirmButton = new Button("Delete", event -> {
            confirmationListener.onConfirm();
            close(); // Close the dialog after confirmation
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR); // Add error theme to the "Delete" button

        // Create the "Cancel" button and add a click listener to close the dialog
        Button cancelButton = new Button("Cancel", event -> close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY); // Add tertiary theme to the "Cancel" button

        // Arrange the buttons in a horizontal layout
        HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
        add(buttons); // Add the button layout to the dialog
    }
}