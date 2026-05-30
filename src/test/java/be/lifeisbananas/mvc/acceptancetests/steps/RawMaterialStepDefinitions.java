package be.lifeisbananas.mvc.acceptancetests.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import be.lifeisbananas.mvc.domain.RawMaterial;
import be.lifeisbananas.mvc.domain.UnitType;
import be.lifeisbananas.mvc.repository.RawMaterialRepository;

public class RawMaterialStepDefinitions {

    private static final String BASE_URL = "http://localhost:8080";

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        rawMaterialRepository.deleteAll();
        driver = new SafariDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I am on the raw material registration page")
    public void iAmOnTheRawMaterialRegistrationPage() {
        driver.navigate().to(BASE_URL + "/raw-materials/new");
    }

    @Given("a raw material with code {string} already exists")
    public void aRawMaterialWithCodeAlreadyExists(String code) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode(code);
        rawMaterial.setName("Banana Powder");
        rawMaterial.setOrganicCertified(true);
        rawMaterial.setAmount(2.50);
        rawMaterial.setPrice(12.50);
        rawMaterial.setUnit(UnitType.KG);
        RawMaterial saved = rawMaterialRepository.save(rawMaterial);
        driver.navigate().to(BASE_URL + "/raw-materials/" + saved.getId());
    }

    @When("I enter code {string}")
    public void iEnterCode(String code) {
        WebElement field = driver.findElement(By.id("code"));
        field.clear();
        field.sendKeys(code);
    }

    @When("I enter name {string}")
    public void iEnterName(String name) {
        WebElement field = driver.findElement(By.id("name"));
        field.clear();
        field.sendKeys(name);
    }

    @When("I enter amount {string}")
    public void iEnterAmount(String amount) {
        WebElement field = driver.findElement(By.id("amount"));
        field.clear();
        field.sendKeys(amount);
    }

    @When("I enter price {string}")
    public void iEnterPrice(String price) {
        WebElement field = driver.findElement(By.id("price"));
        field.clear();
        field.sendKeys(price);
    }

    @When("I choose unit {string}")
    public void iChooseUnit(String unit) {
        driver.findElement(By.cssSelector("#unit option[value='" + unit + "']")).click();
    }

    @When("I mark the raw material as organic certified")
    public void iMarkTheRawMaterialAsOrganicCertified() {
        WebElement checkbox = driver.findElement(By.id("organicCertified"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    @When("I save the raw material")
    public void iSaveTheRawMaterial() {
        driver.findElement(By.id("submit")).click();
    }

    @When("I choose to edit the raw material")
    public void iChooseToEditTheRawMaterial() {
        driver.findElement(By.linkText("Edit raw material")).click();
    }

    @When("I delete the raw material")
    public void iDeleteTheRawMaterial() {
        driver.findElement(By.xpath("//button[contains(.,'Delete raw material')]")).click();
    }

    @Then("I should see the raw material details")
    public void iShouldSeeTheRawMaterialDetails() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Code:"));
        String bodyText = driver.findElement(By.tagName("body")).getText();
        Assertions.assertAll(
                () -> Assertions.assertTrue(bodyText.contains("RM-001")),
                () -> Assertions.assertTrue(bodyText.contains("Organic Banana Puree")),
                () -> Assertions.assertTrue(bodyText.contains("Yes")),
                () -> Assertions.assertTrue(bodyText.contains("6.00")),
                () -> Assertions.assertTrue(bodyText.contains("4.75")),
                () -> Assertions.assertTrue(bodyText.contains("LITER"))
        );
    }

    @Then("I should see the updated raw material details")
    public void iShouldSeeTheUpdatedRawMaterialDetails() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Code:"));
        String bodyText = driver.findElement(By.tagName("body")).getText();
        Assertions.assertAll(
                () -> Assertions.assertTrue(bodyText.contains("Banana Powder Premium")),
                () -> Assertions.assertTrue(bodyText.contains("4.50")),
                () -> Assertions.assertTrue(bodyText.contains("13.95")),
                () -> Assertions.assertTrue(bodyText.contains("KG"))
        );
    }

    @Then("the raw material should no longer appear in the list")
    public void theRawMaterialShouldNoLongerAppearInTheList() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Raw materials"));
        String bodyText = driver.findElement(By.tagName("body")).getText();
        Assertions.assertFalse(bodyText.contains("Banana Powder"));
    }
}
