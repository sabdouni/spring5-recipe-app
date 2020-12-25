package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    private RecipeToRecipeCommand recipeToRecipeCommand;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void getRecipes() {
        //Arrange
        Recipe recipe = new Recipe();
        Set<Recipe> recipesData = new HashSet<Recipe>();
        recipesData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipesData);

        //Act
        Set<Recipe> recipes = recipeService.getRecipes();

        //Assert
        assertEquals(recipesData, recipes);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyLong());

    }


    @Test
    public void findById() {
        //Arrange
        Recipe recipe = new Recipe();
        RecipeCommand recipeCommand = new RecipeCommand();

        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));
        when(recipeToRecipeCommand.convert(any(Recipe.class))).thenReturn(recipeCommand);

        //Act
        RecipeCommand result = recipeService.findById(1L);

        //Assert
        assertEquals(recipeCommand, result);
        verify(recipeRepository, never()).findAll();
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void save() {
        //Arrange
        RecipeCommand recipeCommand = new RecipeCommand();

        Recipe recipe = new Recipe();

        when(recipeCommandToRecipe.convert(any(RecipeCommand.class))).thenReturn(recipe);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        //Act
        RecipeCommand result = recipeService.save(recipeCommand);

        //Assert
        verify(recipeCommandToRecipe, times(1)).convert(any(RecipeCommand.class));
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(recipeToRecipeCommand, times(1)).convert(any(Recipe.class));
    }
}