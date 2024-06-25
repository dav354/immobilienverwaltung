package projektarbeit.immobilienverwaltung.ui.login;
/*

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.parallel.TestCategory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

@TestCategory("adminview")
public class AdminViewTest extends TestBenchTestCase {

    @Before
    public void setup() {
        WebDriver driver = getDriver();
        setDriver(driver);
        driver.get("http://localhost:8080/admin");
    }

    @Test
    public void testAddUser() {
        // Fill out the form fields
        $(TextFieldElement.class).id("username").setValue("newuser");
        $(PasswordFieldElement.class).id("password").setValue("password123");
        $(ComboBoxElement.class).id("role").selectByText("USER");

        // Click the "Add User" button
        $(ButtonElement.class).caption("Add User").first().click();

        // Check if the user is added successfully
        NotificationElement notification = $(NotificationElement.class).waitForFirst();
        Assert.assertEquals("User added successfully", notification.getText());

        // Verify the new user is in the grid
        GridElement grid = $(GridElement.class).first();
        boolean userFound = grid.getCell(0, 0).getText().equals("newuser");
        Assert.assertTrue("New user should be present in the grid", userFound);
    }

    @Test
    public void testDeleteUser() {
        // Add a user to delete
        testAddUser();

        // Find the user in the grid and delete
        GridElement grid = $(GridElement.class).first();
        int rowIndex = grid.getCellIndex("newuser", 0);

        ButtonElement deleteButton = grid.getRow(rowIndex).$(ButtonElement.class).caption("Delete").first();
        deleteButton.click();

        // Check if the user is deleted successfully
        NotificationElement notification = $(NotificationElement.class).waitForFirst();
        Assert.assertEquals("User deleted successfully", notification.getText());

        // Verify the user is no longer in the grid
        grid = $(GridElement.class).first();
        boolean userFound = grid.getCell(0, 0).getText().equals("newuser");
        Assert.assertFalse("User should be deleted from the grid", userFound);
    }

    @Test
    public void testChangePassword() {
        // Add a user to change password
        testAddUser();

        // Find the user in the grid and change password
        GridElement grid = $(GridElement.class).first();
        int rowIndex = grid.getCellIndex("newuser", 0);

        ButtonElement changePasswordButton = grid.getRow(rowIndex).$(ButtonElement.class).caption("Change Password").first();
        changePasswordButton.click();

        // Fill out the new password in the dialog
        DialogElement dialog = $(DialogElement.class).waitForFirst();
        dialog.$(PasswordFieldElement.class).first().setValue("newpassword123");
        dialog.$(ButtonElement.class).caption("Change").first().click();

        // Check if the password is changed successfully
        NotificationElement notification = $(NotificationElement.class).waitForFirst();
        Assert.assertEquals("Password changed successfully for user: newuser", notification.getText());
    }
}

 */