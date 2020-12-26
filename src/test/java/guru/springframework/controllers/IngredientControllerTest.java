package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    private static final Long RECIPE_ID = 1L;
    private static final Long INGREDIENT_ID = 2L;

    private IngredientController controller;

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new IngredientController(recipeService, ingredientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testShowListOfRecipeIngredients() throws Exception {
        //Arrange
        RecipeCommand recipeCommand = new RecipeCommand();

        when(recipeService.findById(anyLong())).thenReturn(recipeCommand);

        //Act
        ResultActions resultActions = mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients"));

        //Assert
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findById(anyLong());
    }

    @Test
    public void testShowIngredientDetails() throws Exception {
        //Arrange
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        //Act
        ResultActions resultActions = mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/show"));

        //Assert
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyLong(), anyLong());
    }
}