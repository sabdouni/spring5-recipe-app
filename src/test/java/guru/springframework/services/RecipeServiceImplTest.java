package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository);
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

        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));

        //Act
        Recipe result = recipeService.findById(1L);

        //Assert
        assertEquals(recipe, result);
        verify(recipeRepository, never()).findAll();
        verify(recipeRepository, times(1)).findById(anyLong());

    }
}