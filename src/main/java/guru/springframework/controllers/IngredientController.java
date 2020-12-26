package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String showListofIngredients(final @PathVariable Long recipeId, final Model model) {
        RecipeCommand recipeCommand = recipeService.findById(recipeId);
        model.addAttribute("recipe", recipeCommand);
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients/{ingredientId}/show")
    public String showIngredientDetail(final @PathVariable Long recipeId, final @PathVariable Long ingredientId, final Model model) {
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId);
        model.addAttribute("ingredient", ingredientCommand);
        return "recipe/ingredient/show";
    }
}
