package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import javax.sound.midi.Receiver;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    private IndexController indexController;

    @Mock
    private RecipeService recipeService;

    @Mock
    private Model model;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    public void testMockMVC() throws Exception {
        //Arrange
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        //Act

        ResultActions resultActions =  mockMvc.perform(MockMvcRequestBuilders.get("/"));

        //Assert
        resultActions.andExpect(status().isOk()).andExpect(view().name("index"));
    }

    @Test
    public void index() {
        //Arrange
        Recipe recipe = new Recipe();
        Set<Recipe> recipes = new HashSet<Recipe>();
        recipes.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipes);

        ArgumentCaptor<Set<Receiver>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //Act
        String templateName = indexController.index(model);

        //Assert
        assertEquals("index", templateName);
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue().size());
    }
}