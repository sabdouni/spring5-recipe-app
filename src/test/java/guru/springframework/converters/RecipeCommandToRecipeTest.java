package guru.springframework.converters;

import guru.springframework.commands.*;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Recipe;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class RecipeCommandToRecipeTest {

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

    private RecipeCommandToRecipe conveter;

    @Before
    public void setUp() throws Exception {
        NotesCommandToNotes notesCommandToNotes = new NotesCommandToNotes();
        CategoryCommandToCategory categoryCommandToCategory = new CategoryCommandToCategory();
        UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure = new UnitOfMeasureCommandToUnitOfMeasure();
        IngredientCommandToIngredient ingredientCommandToIngredient = new IngredientCommandToIngredient(unitOfMeasureCommandToUnitOfMeasure);

        conveter = new RecipeCommandToRecipe(notesCommandToNotes, categoryCommandToCategory, ingredientCommandToIngredient);
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        Recipe recipe = conveter.convert(null);

        //Assert
        assertNull(recipe);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(CATEGORY_ID);
        categoryCommand.setDescription(CATEGORY_DESCRIPTION);

        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(NOTES_ID);
        notesCommand.setRecipeNotes(RECIPE_NOTES);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UOM_ID);
        unitOfMeasureCommand.setDescription(UOM_DESCRIPTION);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setDescription(INGREDIENT_DESCRIPTION);
        ingredientCommand.setAmount(INGREDIENT_AMOUNT);
        ingredientCommand.setUnitOfMeasure(unitOfMeasureCommand);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setDescription(RECIPE_DESCRIPTION);
        recipeCommand.setCookTime(RECIPE_COOK_TIME);
        recipeCommand.setPrepTime(RECIPE_PREP_TIME);
        recipeCommand.setServings(RECIPE_SERVINGS);
        recipeCommand.setDifficulty(RECIPE_DIFFICULTY);
        recipeCommand.setDirections(RECIPE_DIRECTIONS);
        recipeCommand.setUrl(RECIPE_URL);
        recipeCommand.setSource(RECIPE_SOURCE);

        recipeCommand.setNotes(notesCommand);

        recipeCommand.getCategories().add(categoryCommand);

        recipeCommand.getIngredients().add(ingredientCommand);

        //Act
        Recipe recipe = conveter.convert(recipeCommand);

        //Assert
        assertNotNull(recipe);
        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(RECIPE_DESCRIPTION, recipe.getDescription());
        assertEquals(RECIPE_COOK_TIME, recipe.getCookTime());
        assertEquals(RECIPE_PREP_TIME, recipe.getPrepTime());
        assertEquals(RECIPE_SERVINGS, recipe.getServings());
        assertEquals(RECIPE_DIFFICULTY, recipe.getDifficulty());
        assertEquals(RECIPE_DIRECTIONS, recipe.getDirections());
        assertEquals(RECIPE_URL, recipe.getUrl());
        assertEquals(RECIPE_SOURCE, recipe.getSource());

        assertNotNull(recipe.getNotes());
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(RECIPE_NOTES, recipe.getNotes().getRecipeNotes());

        assertNotNull(recipe.getCategories());
        assertEquals(1, recipe.getCategories().size());
        assertEquals(CATEGORY_ID, recipe.getCategories().stream().findFirst().get().getId());
        assertEquals(CATEGORY_DESCRIPTION, recipe.getCategories().stream().findFirst().get().getDescription());

        assertNotNull(recipe.getIngredients());
        assertEquals(1, recipe.getIngredients().size());
        assertEquals(INGREDIENT_ID, recipe.getIngredients().stream().findFirst().get().getId());
        assertEquals(INGREDIENT_AMOUNT, recipe.getIngredients().stream().findFirst().get().getAmount());
        assertEquals(INGREDIENT_DESCRIPTION, recipe.getIngredients().stream().findFirst().get().getDescription());

        assertNotNull(recipe.getIngredients().stream().findFirst().get().getUnitOfMeasure());
        assertEquals(UOM_ID, recipe.getIngredients().stream().findFirst().get().getUnitOfMeasure().getId());
        assertEquals(UOM_DESCRIPTION, recipe.getIngredients().stream().findFirst().get().getUnitOfMeasure().getDescription());
    }
}