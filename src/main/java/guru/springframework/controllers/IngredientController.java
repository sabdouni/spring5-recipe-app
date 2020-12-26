package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String showListofIngredientsView(final @PathVariable Long recipeId, final Model model) {
        model.addAttribute("recipe", recipeService.findById(recipeId));
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/show")
    public String showIngredientDetailView(final @PathVariable Long recipeId, final @PathVariable Long ingredientId, final Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/update")
    public String showIngredientUpdateView(final @PathVariable Long recipeId, final @PathVariable Long ingredientId, final Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        model.addAttribute("listOfUoms", unitOfMeasureService.findAll());
        model.addAttribute("recipeId", recipeId);

        return "recipe/ingredient/new";
    }

    @PostMapping("/recipe/{recipeId}/ingredients")
    public String saveOrUpdate(final @PathVariable Long recipeId, final @ModelAttribute IngredientCommand ingredientCommand) {
        IngredientCommand saveIngredientCommand = ingredientService.save(recipeId, ingredientCommand);
        return "redirect:/recipe/" + recipeId + "/ingredients/" + saveIngredientCommand.getId() + "/show";
    }


}
