package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;

import java.util.Set;


public interface RecipeService {
    Set<Recipe> getRecipes();

    RecipeCommand findById(Long id);

    RecipeCommand save(RecipeCommand recipeCommand);

    void deleteById(Long id);
}
