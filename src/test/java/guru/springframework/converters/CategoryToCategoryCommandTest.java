package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryToCategoryCommandTest {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "Mexican";
    private CategoryToCategoryCommand converter;

    @Before
    public void setUp() {
        converter = new CategoryToCategoryCommand();
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        CategoryCommand categoryCommand = converter.convert(null);

        //Assert
        assertNull(categoryCommand);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        Category category = new Category();
        category.setId(ID);
        category.setDescription(DESCRIPTION);

        //Act
        CategoryCommand categoryCommand = converter.convert(category);

        //Assert
        assertNotNull(categoryCommand);
        assertEquals(ID, categoryCommand.getId());
        assertEquals(DESCRIPTION, categoryCommand.getDescription());
    }
}