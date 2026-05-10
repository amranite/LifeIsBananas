package be.lifeisbananas;

/**
 * SINGLETON PATTERN: ProductionSettings
 * Doel: Een centraal punt voor alle fabriek-instellingen.
 * Er mag maximaal 1 exemplaar van deze klasse bestaan.
 */
public class ProductionSettings {

    // 1. De enige, unieke instantie van deze klasse (static)
    private static ProductionSettings instance;

    // --- De Fabrieksdata ---
    private double defaultFreezeDryingTemperature;
    private int defaultPackagingSizeGrams;
    private String companyName;

    // 2. Private Constructor
    // Blokkeert andere bestanden om 'new ProductionSettings()' te gebruiken!
    private ProductionSettings() {
        this.defaultFreezeDryingTemperature = -50.5;
        this.defaultPackagingSizeGrams = 250;
        this.companyName = "Life is Bananas Inc.";
        System.out.println("⚙️ SINGLETON START: Fabrieksinstellingen eenmalig geladen.");
    }

    // 3. Globaal Toegangspunt (Lazy Initialization)
    // Dit is de methode die je teammate gaat gebruiken!
    public static ProductionSettings getInstance() {
        if (instance == null) {
            instance = new ProductionSettings();
        }
        return instance;
    }

    // --- Getters ---
    public double getDefaultFreezeDryingTemperature() { return defaultFreezeDryingTemperature; }
    public int getDefaultPackagingSizeGrams() { return defaultPackagingSizeGrams; }
    public String getCompanyName() { return companyName; }
}