package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientToIngredientCommandCommandTest {

    private static final Long INGREDIENT_ID = 1L;
    private static final String INGREDIENT_DESCRIPTION = "Salt";
    private static final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(2);
    private static final Long UNIT_OF_MEASURE_ID = 2L;
    private static final String UNIT_OF_MEASURE_DESCRIPTION = "Teaspoon";

    private IngredientToIngredientCommand converter;

    @Before
    public void setUp() throws Exception {
        UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
        converter = new IngredientToIngredientCommand(unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        IngredientCommand ingredientCommand = converter.convert(null);

        //Assert
        assertNull(ingredientCommand);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(UNIT_OF_MEASURE_ID);
        unitOfMeasure.setDescription(UNIT_OF_MEASURE_DESCRIPTION);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setDescription(INGREDIENT_DESCRIPTION);
        ingredient.setAmount(INGREDIENT_AMOUNT);
        ingredient.setUnitOfMeasure(unitOfMeasure);

        //Act
        IngredientCommand ingredientCommand = converter.convert(ingredient);

        //Assert
        assertNotNull(ingredientCommand);
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredientCommand.getDescription());
        assertEquals(INGREDIENT_AMOUNT, ingredientCommand.getAmount());

        assertNotNull(ingredientCommand.getUnitOfMeasure());
        assertEquals(UNIT_OF_MEASURE_ID, ingredientCommand.getUnitOfMeasure().getId());
        assertEquals(UNIT_OF_MEASURE_DESCRIPTION, ingredientCommand.getUnitOfMeasure().getDescription());
    }
}