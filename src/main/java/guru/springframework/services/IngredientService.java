package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand save(Long recipeId, IngredientCommand ingredientCommand);

    void deleteById(Long recipeId, Long ingredientId);
}
