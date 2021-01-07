package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    private static final Long RECIPE_ID = 1L;

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

    @Test(expected = NotFoundException.class)
    public void findByIdNotFound() {
        //Arrange
        Recipe recipe = new Recipe();
        RecipeCommand recipeCommand = new RecipeCommand();

        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        when(recipeToRecipeCommand.convert(any(Recipe.class))).thenReturn(recipeCommand);

        //Act
        recipeService.findById(1L);

        //Assert
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

    @Test
    public void testDeleteById() {
        //Arrange
        Recipe recipe = new Recipe();
        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));

        //Act
        recipeService.deleteById(1L);

        //Assert
        verify(recipeRepository, times(1)).deleteById(eq(1L));
    }

    @Test
    public void testUploadImageById() throws IOException {
        //Arrange
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));

        MockMultipartFile file = new MockMultipartFile("imagefine", "testing.txt", "text/plain", "Some Image Mock".getBytes());

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        //Act
        recipeService.uploadImageById(RECIPE_ID, file);

        //Assert
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(file.getBytes().length, argumentCaptor.getValue().getImage().length);
    }
}