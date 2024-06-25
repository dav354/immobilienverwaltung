package projektarbeit.immobilienverwaltung.ui.mieter;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.datepicker.testbench.DatePickerElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.parallel.TestCategory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

@TestCategory("mytests")
public class MieterFormTest extends TestBenchTestCase {

    @Before
    public void setup() {
        setDriver(getDriver());
        getDriver().get("http://localhost:8080");
    }

    @Test
    public void testFormSubmission() {
        // Navigiere zum Formular
        FormLayoutElement form = $(FormLayoutElement.class).first();

        // Fülle das Formular aus
        form.$(TextFieldElement.class).id("name").setValue("Mustermann");
        form.$(TextFieldElement.class).id("vorname").setValue("Max");
        form.$(TextFieldElement.class).id("telefonnummer").setValue("123456789");
        form.$(TextFieldElement.class).id("email").setValue("max.mustermann@example.com");

        // Setze das Datum
        DatePickerElement mietbeginn = form.$(DatePickerElement.class).id("mietbeginn");
        setDatePickerValue(mietbeginn, "2023-01-01");

        DatePickerElement mietende = form.$(DatePickerElement.class).id("mietende");
        setDatePickerValue(mietende, "2023-12-31");

        // Fülle restliche Felder aus
        form.$(TextFieldElement.class).id("einkommen").setValue("50000");
        form.$(TextFieldElement.class).id("miete").setValue("1000");
        form.$(TextFieldElement.class).id("kaution").setValue("2000");
        form.$(TextFieldElement.class).id("anzahlBewohner").setValue("2");

        // Speichern Button klicken
        form.$(ButtonElement.class).id("speichern").click();

        // Überprüfen ob eine Erfolgsmeldung
        NotificationElement notification = $(NotificationElement.class).waitForFirst();
        Assert.assertEquals("Mieter erfolgreich gespeichert.", notification.getText());
    }

    private void setDatePickerValue(DatePickerElement datePicker, String value) {
        WebElement input = datePicker.$("input").first();
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value = arguments[1];", input, value);
        input.sendKeys(" ");

    }
}
