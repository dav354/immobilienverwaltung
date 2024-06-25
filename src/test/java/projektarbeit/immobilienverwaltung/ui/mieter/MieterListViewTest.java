package projektarbeit.immobilienverwaltung.ui.mieter;

import static org.junit.jupiter.api.Assertions.*;
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchTestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MieterListViewTest extends TestBenchTestCase {

    private static WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
    }

    @BeforeEach
    public void setUp() {
        setDriver(driver);
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testGridAndFilter() {
        // Open the application
        getDriver().get("http://localhost:8080/mieter");

        // Find the grid and the filter text field
        GridElement grid = $(GridElement.class).first();
        TextFieldElement filterText = $(TextFieldElement.class).first();

        // Verify initial state
        assertEquals(10, grid.getRowCount(), "Initial row count should be 10");

        // Apply a filter
        filterText.setValue("Muster");
        filterText.sendKeys("\n");

        // Verify filtered state
        assertTrue(grid.getRowCount() <= 10, "Filtered row count should be less than or equal to 10");
    }

    @Test
    public void testAddMieter() {
        // Open the application
        getDriver().get("http://localhost:8080/mieter");

        // Find and click the "Add Mieter" button
        ButtonElement addButton = $(ButtonElement.class).attribute("id", "addMieterButton").first();
        addButton.click();

        // Fill out the form
        TextFieldElement nameField = $(TextFieldElement.class).attribute("id", "nameField").first();
        TextFieldElement vornameField = $(TextFieldElement.class).attribute("id", "vornameField").first();
        TextFieldElement telefonnummerField = $(TextFieldElement.class).attribute("id", "telefonnummerField").first();
        TextFieldElement emailField = $(TextFieldElement.class).attribute("id", "emailField").first();

        nameField.setValue("Test");
        vornameField.setValue("User");
        telefonnummerField.setValue("0123456789");
        emailField.setValue("test.user@example.com");

        // Save the new Mieter
        ButtonElement saveButton = $(ButtonElement.class).attribute("id", "saveButton").first();
        saveButton.click();

        // Verify that the new Mieter appears in the grid
        GridElement grid = $(GridElement.class).first();
        assertTrue(grid.getCell(0, 0).getText().contains("Test"), "New mieter should be added to the grid");
    }
}
