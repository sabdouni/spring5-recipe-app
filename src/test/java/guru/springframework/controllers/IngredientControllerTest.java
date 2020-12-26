package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    private static final Long RECIPE_ID = 1L;
    private static final Long INGREDIENT_ID = 2L;
    private static final Long UOM_ID = 3L;
    private static final String INGREDIENT_DESCRIPTION = "Teaspoon";
    private static final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(2);

    private IngredientController controller;

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private UnitOfMeasureService unitOfMeasureService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testShowListOfRecipeIngredientsView() throws Exception {
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
    public void testShowIngredientDetailsView() throws Exception {
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

    @Test
    public void testShowIngredientUpdateDetailsView() throws Exception {
        //Arrange
        IngredientCommand ingredientCommand = new IngredientCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        Set<UnitOfMeasureCommand> unitOfMeasures = new HashSet<>();
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UOM_ID);
        unitOfMeasures.add(unitOfMeasureCommand);

        when(unitOfMeasureService.findAll()).thenReturn(unitOfMeasures);

        //Act
        ResultActions resultActions = mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/update"));

        //Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/new"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("listOfUoms"))
                .andExpect(model().attributeExists("recipeId"));

        verify(unitOfMeasureService, times(1)).findAll();
    }

    @Test
    public void testShowIngredientCreationView() throws Exception {
        //Arrange
        Set<UnitOfMeasureCommand> unitOfMeasures = new HashSet<>();
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UOM_ID);
        unitOfMeasures.add(unitOfMeasureCommand);

        when(unitOfMeasureService.findAll()).thenReturn(unitOfMeasures);

        //Act
        ResultActions resultActions = mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients/new"));

        //Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/new"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("listOfUoms"))
                .andExpect(model().attributeExists("recipeId"));

        verify(unitOfMeasureService, times(1)).findAll();
    }

    @Test
    public void testCreateOrUpdateIngredient() throws Exception {
        //Arrange
        IngredientCommand savedIngredient = new IngredientCommand();
        savedIngredient.setId(INGREDIENT_ID);

        when(ingredientService.save(anyLong(), any(IngredientCommand.class))).thenReturn(savedIngredient);

        //Act
        ResultActions resultActions = mockMvc.perform(post("/recipe/" + RECIPE_ID + "/ingredients")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", INGREDIENT_ID.toString())
                .param("description", INGREDIENT_DESCRIPTION)
                .param("amount", INGREDIENT_AMOUNT.toString())
                .param("unitOfMeasure.id", UOM_ID.toString()));

        //Assert
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/show"));

        verify(ingredientService, times(1)).save(anyLong(), any(IngredientCommand.class));
    }
}