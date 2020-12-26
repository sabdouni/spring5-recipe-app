package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    private static final Long RECIPE_ID = 1L;
    private static final Long INGREDIENT_ID = 2L;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Mock
    private IngredientToIngredientCommand ingredientToIngredientCommand;

    @Mock
    private IngredientCommandToIngredient ingredientCommandToIngredient;

    private IngredientServiceImpl ingredientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand, ingredientCommandToIngredient, unitOfMeasureRepository);
    }

    @Test
    public void findByRecipeIdAndId() {
        //Arrange
        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.getIngredients().add(ingredient);


        IngredientCommand ingredientCommand = new IngredientCommand();

        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));
        when(ingredientToIngredientCommand.convert(any(Ingredient.class))).thenReturn(ingredientCommand);

        //Act
        IngredientCommand result = ingredientService.findByRecipeIdAndIngredientId(RECIPE_ID, INGREDIENT_ID);

        //Assert
        assertNotNull(result);
        verify(recipeRepository, times(1)).findById(eq(RECIPE_ID));
        verify(ingredientToIngredientCommand, times(1)).convert(eq(ingredient));
    }

    @Test
    public void testSave() {
        //Arrange
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        when(recipeRepository.findById(eq(RECIPE_ID))).thenReturn(java.util.Optional.of(recipe));

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        when(ingredientCommandToIngredient.convert(any(IngredientCommand.class))).thenReturn(ingredient);

        //Act
        IngredientCommand result = ingredientService.save(RECIPE_ID, ingredientCommand);


        //Assert
        verify(recipeRepository, times(1)).findById(eq(RECIPE_ID));
        verify(ingredientCommandToIngredient, times(1)).convert(any(IngredientCommand.class));
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}