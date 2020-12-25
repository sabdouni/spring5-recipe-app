package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class RecipeToRecipeCommandTest {
    public static final Long CATEGORY_ID = 1L;
    public static final String CATEGORY_DESCRIPTION = "American";
    public static final Long NOTES_ID = 2L;
    public static final String RECIPE_NOTES = "BlaBlaBla";
    public static final Long UOM_ID = 3L;
    public static final String UOM_DESCRIPTION = "Each";
    public static final Long INGREDIENT_ID = 4L;
    public static final String INGREDIENT_DESCRIPTION = "ripe avocados";
    public static final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(2);
    public static final Long RECIPE_ID = 5L;
    public static final String RECIPE_DESCRIPTION = "How to Make Perfect Guacamole";
    public static final Integer RECIPE_COOK_TIME = 0;
    public static final Integer RECIPE_PREP_TIME = 10;
    public static final Integer RECIPE_SERVINGS = 4;
    public static final Difficulty RECIPE_DIFFICULTY = Difficulty.EASY;
    public static final String RECIPE_DIRECTIONS = "1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
            "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
            "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
            "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
            "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
            "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
            "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.";
    public static final String RECIPE_URL = "https://www.simplyrecipes.com/recipes/perfect_guacamole/";
    public static final String RECIPE_SOURCE = "simplyrecipes";

    private RecipeToRecipeCommand conveter;

    @Before
    public void setUp() throws Exception {
        NotesToNotesCommand notesToNotesCommand = new NotesToNotesCommand();
        CategoryToCategoryCommand categoryToCategoryCommand = new CategoryToCategoryCommand();
        UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
        IngredientToIngredientCommand ingredientToIngredientCommand = new IngredientToIngredientCommand(unitOfMeasureToUnitOfMeasureCommand);

        conveter = new RecipeToRecipeCommand(notesToNotesCommand, categoryToCategoryCommand, ingredientToIngredientCommand);
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        RecipeCommand recipeCommand = conveter.convert(null);

        //Assert
        assertNull(recipeCommand);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setDescription(CATEGORY_DESCRIPTION);

        Notes notes = new Notes();
        notes.setId(NOTES_ID);
        notes.setRecipeNotes(RECIPE_NOTES);

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(UOM_ID);
        unitOfMeasure.setDescription(UOM_DESCRIPTION);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setDescription(INGREDIENT_DESCRIPTION);
        ingredient.setAmount(INGREDIENT_AMOUNT);
        ingredient.setUnitOfMeasure(unitOfMeasure);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setDescription(RECIPE_DESCRIPTION);
        recipe.setCookTime(RECIPE_COOK_TIME);
        recipe.setPrepTime(RECIPE_PREP_TIME);
        recipe.setServings(RECIPE_SERVINGS);
        recipe.setDifficulty(RECIPE_DIFFICULTY);
        recipe.setDirections(RECIPE_DIRECTIONS);
        recipe.setUrl(RECIPE_URL);
        recipe.setSource(RECIPE_SOURCE);

        recipe.setNotes(notes);

        recipe.getCategories().add(category);

        recipe.getIngredients().add(ingredient);

        //Act
        RecipeCommand recipeCommand = conveter.convert(recipe);

        //Assert
        assertNotNull(recipeCommand);
        assertEquals(RECIPE_ID, recipeCommand.getId());
        assertEquals(RECIPE_DESCRIPTION, recipeCommand.getDescription());
        assertEquals(RECIPE_COOK_TIME, recipeCommand.getCookTime());
        assertEquals(RECIPE_PREP_TIME, recipeCommand.getPrepTime());
        assertEquals(RECIPE_SERVINGS, recipeCommand.getServings());
        assertEquals(RECIPE_DIFFICULTY, recipeCommand.getDifficulty());
        assertEquals(RECIPE_DIRECTIONS, recipeCommand.getDirections());
        assertEquals(RECIPE_URL, recipeCommand.getUrl());
        assertEquals(RECIPE_SOURCE, recipeCommand.getSource());

        assertNotNull(recipeCommand.getNotes());
        assertEquals(NOTES_ID, recipeCommand.getNotes().getId());
        assertEquals(RECIPE_NOTES, recipeCommand.getNotes().getRecipeNotes());

        assertNotNull(recipeCommand.getCategories());
        assertEquals(1, recipeCommand.getCategories().size());
        assertEquals(CATEGORY_ID, recipeCommand.getCategories().stream().findFirst().get().getId());
        assertEquals(CATEGORY_DESCRIPTION, recipeCommand.getCategories().stream().findFirst().get().getDescription());

        assertNotNull(recipeCommand.getIngredients());
        assertEquals(1, recipeCommand.getIngredients().size());
        assertEquals(INGREDIENT_ID, recipeCommand.getIngredients().stream().findFirst().get().getId());
        assertEquals(INGREDIENT_AMOUNT, recipeCommand.getIngredients().stream().findFirst().get().getAmount());
        assertEquals(INGREDIENT_DESCRIPTION, recipeCommand.getIngredients().stream().findFirst().get().getDescription());

        assertNotNull(recipeCommand.getIngredients().stream().findFirst().get().getUnitOfMeasure());
        assertEquals(UOM_ID, recipeCommand.getIngredients().stream().findFirst().get().getUnitOfMeasure().getId());
        assertEquals(UOM_DESCRIPTION, recipeCommand.getIngredients().stream().findFirst().get().getUnitOfMeasure().getDescription());
    }
}