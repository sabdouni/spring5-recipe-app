package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Difficulty;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {
    public static final Long RECIPE_ID = 1L;
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
    public static final String RECIPE_NOTES = "BlaBlaBla";

    @Mock
    private RecipeService recipeService;

    private RecipeController recipeController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void testShowRecipeView() throws Exception {
        //Arrange
        RecipeCommand recipe = new RecipeCommand();
        when(recipeService.findById(anyLong())).thenReturn(recipe);

        //Act
        ResultActions resultActions = mockMvc.perform(get("/recipe/1/show"));

        //Assert
        verify(recipeService, times(1)).findById(eq(1L));
        resultActions.andExpect(model().attributeExists("recipe"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"));
    }

    @Test
    public void testNewRecipeView() throws Exception {
        //Arrange

        //Act
        ResultActions resultActions = mockMvc.perform(get("/recipe/new"));

        //Assert
        resultActions.andExpect(model().attributeExists("recipe"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/new"));
    }

    @Test
    public void testUpdateRecipeView() throws Exception {
        //Arrange
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);

        when(recipeService.findById(eq(RECIPE_ID))).thenReturn(recipeCommand);

        //Act
        ResultActions resultActions = mockMvc.perform(get("/recipe/" + RECIPE_ID + "/update"));

        //Assert
        verify(recipeService, times(1)).findById(eq(RECIPE_ID));

        resultActions.andExpect(model().attributeExists("recipe"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/new"));
    }

    @Test
    public void testSaveOrUpdateRecipe() throws Exception {
        //Arrange
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);

        when(recipeService.save(any(RecipeCommand.class))).thenReturn(recipeCommand);
        ArgumentCaptor<RecipeCommand> requestCaptor = ArgumentCaptor.forClass(RecipeCommand.class);

        //Act
        ResultActions resultActions = mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", RECIPE_DESCRIPTION)
                .param("prepTime", RECIPE_PREP_TIME.toString())
                .param("cookTime", RECIPE_COOK_TIME.toString())
                .param("servings", RECIPE_SERVINGS.toString())
                .param("source", RECIPE_SOURCE)
                .param("url", RECIPE_URL)
                .param("directions", RECIPE_DESCRIPTION)
                .param("notes.recipeNotes", RECIPE_NOTES));

        //Assert
        verify(recipeService, times(1)).save(requestCaptor.capture());

        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipe/" + RECIPE_ID + "/show"));

        assertEquals(RECIPE_DESCRIPTION, requestCaptor.getValue().getDescription());
        assertEquals(RECIPE_PREP_TIME, requestCaptor.getValue().getPrepTime());
        assertEquals(RECIPE_COOK_TIME, requestCaptor.getValue().getCookTime());
        assertEquals(RECIPE_SERVINGS, requestCaptor.getValue().getServings());
        assertEquals(RECIPE_SOURCE, requestCaptor.getValue().getSource());
        assertEquals(RECIPE_URL, requestCaptor.getValue().getUrl());
        assertEquals(RECIPE_DESCRIPTION, requestCaptor.getValue().getDirections());
        assertEquals(RECIPE_NOTES, requestCaptor.getValue().getNotes().getRecipeNotes());
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        //Arrange

        //Act
        ResultActions resultActions = mockMvc.perform(get("/recipe/" + RECIPE_ID + "/delete"));

        //Assert
        verify(recipeService, times(1)).deleteById(eq(RECIPE_ID));
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}