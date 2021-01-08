package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @GetMapping("/recipe/{id}/showimage")
    public void showImage(final @PathVariable Long id, final HttpServletResponse servletResponse) throws IOException {
        RecipeCommand recipeCommand = recipeService.findById(id);
        servletResponse.setContentType("image/jpeg");

        byte[] imageBytes = new byte[recipeCommand.getImage().length];
        int i = 0;
        for (Byte b : recipeCommand.getImage()) {
            imageBytes[i++] = b;
        }

        InputStream is = new ByteArrayInputStream(imageBytes);
        IOUtils.copy(is, servletResponse.getOutputStream());
    }

    @GetMapping("/recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/new";
    }

    @PostMapping("/recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, final BindingResult bindingResult, final Model model) {
        if (bindingResult.hasErrors()) {
            return "recipe/new";
        }
        RecipeCommand savedRecipeCommand = recipeService.save(recipeCommand);
        return "redirect:/recipe/" + savedRecipeCommand.getId() + "/show";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Exception exception, Model model) {
        model.addAttribute("exception", exception);
        return "errors/404.html";
    }
}
