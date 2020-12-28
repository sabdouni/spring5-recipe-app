package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"", "/", "index", "/index.html"})
    public String index(Model model) {
        Iterable<Recipe> recipes = recipeService.getRecipes();
        model.addAttribute("recipes", recipes);
        return "index";
    }
}
