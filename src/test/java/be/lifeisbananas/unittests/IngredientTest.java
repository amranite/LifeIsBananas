package be.lifeisbananas.unittests;

import be.lifeisbananas.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    private RawMaterial banana;

    @BeforeEach
    void setUp() {
        // We need a valid RawMaterial to test the Ingredient constructor
        banana = new RawMaterial("RM01", "Banana", true, 0.5, UnitType.PIECE);
    }

    @Test
    void testConstructorThrowsExceptionForZeroQuantity() {
        // The constructor should throw IllegalArgumentException if quantity is 0
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient(0, UnitType.PIECE, 1, banana);
        });

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

    @Test
    void testConstructorThrowsExceptionForNegativeQuantity() {
        // The constructor should throw IllegalArgumentException if quantity is -1.5
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient(-1.5, UnitType.PIECE, 1, banana);
        });

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

    @Test
    void testConstructorThrowsExceptionForNullRawMaterial() {
        // The constructor should throw IllegalArgumentException if RawMaterial is null
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient(10.0, UnitType.GRAM, 1, null);
        });

        assertEquals("RawMaterial cannot be null", exception.getMessage());
    }
}