package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/show";
    }

    @GetMapping("/recipe/{id}/update")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/new";
    }

    @GetMapping("/recipe/{id}/delete")
    public String delete(@PathVariable Long id) {
        recipeService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/recipe/{id}/image")
    public String showUploadImageView(final @PathVariable Long id, final Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/uploadimage";
    }

    @PostMapping("/recipe/{id}/image")
    public String uploadImage(@PathVariable Long id, @RequestParam("imagefile") MultipartFile file) throws IOException {
        recipeService.uploadImageById(id, file);
        return "redirect:/recipe/" + id + "/show";
    }

    @GetMapping("/recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/new";
    }

    @PostMapping("/recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
        RecipeCommand savedRecipeCommand = recipeService.save(recipeCommand);
        return "redirect:/recipe/" + savedRecipeCommand.getId() + "/show";
    }
}
