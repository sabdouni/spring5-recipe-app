package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientCommandToIngredientCommandTest {

    private static final Long INGREDIENT_ID = 1L;
    private static final String INGREDIENT_DESCRIPTION = "Salt";
    private static final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(2);
    private static final Long UNIT_OF_MEASURE_ID = 2L;
    private static final String UNIT_OF_MEASURE_DESCRIPTION = "Teaspoon";

    private IngredientCommandToIngredient converter;

    @Before
    public void setUp() {
        UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure = new UnitOfMeasureCommandToUnitOfMeasure();
        converter = new IngredientCommandToIngredient(unitOfMeasureCommandToUnitOfMeasure);
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        Ingredient ingredient = converter.convert(null);

        //Assert
        assertNull(ingredient);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UNIT_OF_MEASURE_ID);
        unitOfMeasureCommand.setDescription(UNIT_OF_MEASURE_DESCRIPTION);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setDescription(INGREDIENT_DESCRIPTION);
        ingredientCommand.setAmount(INGREDIENT_AMOUNT);
        ingredientCommand.setUnitOfMeasure(unitOfMeasureCommand);

        //Act
        Ingredient ingredient = converter.convert(ingredientCommand);

        //Assert
        assertNotNull(ingredient);
        assertEquals(INGREDIENT_ID, ingredient.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredient.getDescription());
        assertEquals(INGREDIENT_AMOUNT, ingredient.getAmount());

        assertNotNull(ingredient.getUnitOfMeasure());
        assertEquals(UNIT_OF_MEASURE_ID, ingredient.getUnitOfMeasure().getId());
        assertEquals(UNIT_OF_MEASURE_DESCRIPTION, ingredient.getUnitOfMeasure().getDescription());
    }
}