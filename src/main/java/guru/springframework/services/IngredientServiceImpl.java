package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    private final RecipeRepository recipeRepository;

    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
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

    @Transactional
    public IngredientCommand save(final Long recipeId, final IngredientCommand ingredientCommand) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            throw new RuntimeException("Recipe not found:" + recipeId);
        }

        Recipe detachedRecipe = optionalRecipe.get();

        Optional<Ingredient> optionalIngredient = detachedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ObjectUtils.nullSafeEquals(ingredient.getId(), ingredientCommand.getId()))
                .findFirst();

        Ingredient ingredientToUpdateOrCreate;

        if (optionalIngredient.isPresent()) {
            ingredientToUpdateOrCreate = optionalIngredient.get();
            ingredientToUpdateOrCreate.setDescription(ingredientCommand.getDescription());
            ingredientToUpdateOrCreate.setAmount(ingredientCommand.getAmount());
            ingredientToUpdateOrCreate.setUnitOfMeasure(unitOfMeasureRepository
                    .findById(ingredientCommand.getUnitOfMeasure().getId())
                    .orElseThrow(() -> new RuntimeException("UnitOfMeasur not found:" + ingredientCommand.getUnitOfMeasure().getId())));
        } else {
            ingredientToUpdateOrCreate = ingredientCommandToIngredient.convert(ingredientCommand);
            ingredientToUpdateOrCreate.setRecipe(detachedRecipe);
        }

        Ingredient ingredientSaved = ingredientRepository.save(ingredientToUpdateOrCreate);

        return ingredientToIngredientCommand.convert(ingredientSaved);
    }
}
