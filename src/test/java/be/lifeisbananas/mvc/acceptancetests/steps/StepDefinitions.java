package be.lifeisbananas.mvc.acceptancetests.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StepDefinitions {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String TEST_CODE_PREFIX = "AT-RM-";

    private WebDriver driver;
    private WebDriverWait wait;

    @Given("^I am on the page where I can register a new raw material$")
    public void i_am_on_the_page_where_i_can_register_a_new_raw_material() {
        openBrowser();
        clearRawMaterialsThroughUi();
        driver.navigate().to(BASE_URL + "/raw-materials/new");
    }

    @Given("^I have registered a raw material with code \"([^\"]*)\"$")
    public void i_have_registered_a_raw_material_with_code(String code) {
        openBrowser();
        clearRawMaterialsThroughUi();
        registerRawMaterial(code, "Banana Powder", "2.50", "12.50", "KG", true);
    }

    @When("^I enter \"([^\"]*)\" in the ([^\"]*) field$")
    public void i_enter_in_the_field(String enteredText, String fieldName) {
        WebElement field = driver.findElement(By.id(fieldName));
        field.clear();
        field.sendKeys(enteredText);
    }

    @When("^I choose \"([^\"]*)\" in the ([^\"]*) field$")
    public void i_choose_in_the_field(String value, String fieldName) {
        new Select(driver.findElement(By.id(fieldName))).selectByValue(value);
    }

    @When("^I mark the raw material as organic certified$")
    public void i_mark_the_raw_material_as_organic_certified() {
        WebElement checkbox = driver.findElement(By.id("organicCertified"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    @When("^I press on the Submit button$")
    public void i_press_on_the_submit_button() {
        driver.findElement(By.name("submit")).click();
    }

    @When("^I click the Edit raw material link$")
    public void i_click_the_edit_raw_material_link() {
        driver.findElement(By.linkText("Edit raw material")).click();
    }

    @When("^I click the Raw materials list link$")
    public void i_click_the_raw_materials_list_link() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Back to list"))).click();
        } catch (WebDriverException ignored) {
            driver.navigate().to(BASE_URL + "/raw-materials");
        }
    }

    @When("^I press on the Delete raw material button$")
    public void i_press_on_the_delete_raw_material_button() {
        driver.findElement(By.xpath("//button[contains(.,'Delete raw material')]")).click();
    }

    @Then("^I should see the following raw material details$")
    public void i_should_see_the_following_raw_material_details(DataTable checklistDataTable) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Code:"));
        String bodyText = driver.findElement(By.tagName("body")).getText();

        List<Map<String, String>> dataTable = checklistDataTable.asMaps();
        for (Map<String, String> rowFromTable : dataTable) {
            String textToBeFound = rowFromTable.get("label") + " " + rowFromTable.get("data");
            Assert.assertTrue("Did not find this text: " + textToBeFound + "\n", bodyText.contains(textToBeFound));
        }
    }

    @Then("^I should see a raw materials list containing \"([^\"]*)\"$")
    public void i_should_see_a_raw_materials_list_containing(String textToBeFound) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Raw materials"));
        String bodyText = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue("Did not find this text: " + textToBeFound + "\n", bodyText.contains(textToBeFound));
    }

    @Then("^I should not see a raw materials list containing \"([^\"]*)\"$")
    public void i_should_not_see_a_raw_materials_list_containing(String textToBeFound) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Raw materials"));
        String bodyText = driver.findElement(By.tagName("body")).getText();
        Assert.assertFalse("Found this text: " + textToBeFound + "\n", bodyText.contains(textToBeFound));
    }

    @Then("^I close the browser$")
    public void i_close_the_browser() {
        closeBrowser();
    }

    @After
    public void tearDown() {
        closeBrowser();
    }

    private void openBrowser() {
        if (driver == null) {
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }
    }

    private void registerRawMaterial(String code, String name, String amount, String price, String unit, boolean organic) {
        driver.navigate().to(BASE_URL + "/raw-materials/new");
        i_enter_in_the_field(code, "code");
        i_enter_in_the_field(name, "name");
        i_enter_in_the_field(amount, "amount");
        i_enter_in_the_field(price, "price");
        i_choose_in_the_field(unit, "unit");
        if (organic) {
            i_mark_the_raw_material_as_organic_certified();
        }
        i_press_on_the_submit_button();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Code:"));
    }

    private void clearRawMaterialsThroughUi() {
        driver.navigate().to(BASE_URL + "/raw-materials");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Raw materials"));

        int attempts = 0;
        while (true) {
            List<WebElement> deleteButtons = driver.findElements(
                    By.xpath("//table//tr[td[contains(.,'" + TEST_CODE_PREFIX + "')]]//button[contains(.,'Delete')]"));
            if (deleteButtons.isEmpty()) {
                return;
            }
            if (attempts++ > 20) {
                Assert.fail("Could not remove existing raw material acceptance test records");
            }

            try {
                deleteButtons.get(0).click();
            } catch (WebDriverException ignored) {
                // The list may refresh while Chrome is resolving the clicked node. Reload and rescan.
            }
            driver.navigate().to(BASE_URL + "/raw-materials");
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Raw materials"));
        }
    }

    private void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
        }
    }
}
