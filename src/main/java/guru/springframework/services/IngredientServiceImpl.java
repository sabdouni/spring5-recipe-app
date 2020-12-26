package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;

    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            throw new RuntimeException("Recipe not found:" + recipeId);
        }

        Optional<Ingredient> optionalIngredient = optionalRecipe.get().getIngredients()
                .stream()
                .filter((Ingredient ingredient) -> ObjectUtils.nullSafeEquals(ingredientId, ingredient.getId()))
                .findFirst();

        if (!optionalIngredient.isPresent()) {
            throw new RuntimeException("Ingredient not found:" + ingredientId);
        }

        return ingredientToIngredientCommand.convert(optionalIngredient.get());
    }
}
