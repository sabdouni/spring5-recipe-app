package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository  unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;

    public RecipeBootstrap(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //Units of measure
        UnitOfMeasure teaspoon = unitOfMeasureRepository.findByDescription("Teaspoon").get();
        UnitOfMeasure tablespoon = unitOfMeasureRepository.findByDescription("Tablespoon").get();
        UnitOfMeasure dash = unitOfMeasureRepository.findByDescription("Dash").get();

        //Categories
        Category american = categoryRepository.findByDescription("American").get();
        Category mexican =  categoryRepository.findByDescription("Mexican").get();

        //Guacamole
        Recipe guacamole = new Recipe();
        guacamole.setDescription("How to Make Perfect Guacamole");
        guacamole.setCookTime(0);
        guacamole.setPrepTime(10);
        guacamole.setServings(4);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.setDirections(
                "1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                        "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                        "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                        "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                        "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                        "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                        "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve."
        );
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.setSource("simplyrecipes");
        guacamole.setNotes(new Notes("BlaBlaBla"));

        guacamole.getCategories().add(american);
        guacamole.getCategories().add(mexican);

        Ingredient avocados = new Ingredient();
        avocados.setDescription("ripe avocados");
        avocados.setAmount(new BigDecimal(2));


        Ingredient salt = new Ingredient();
        salt.setDescription("of salt, more to taste");
        salt.setAmount(new BigDecimal(1/4));
        salt.setUnitOfMeasure(teaspoon);

        guacamole.addIngredient(avocados);
        guacamole.addIngredient(salt);

        recipeRepository.save(guacamole);
    }
}
