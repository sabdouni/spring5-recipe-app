package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryCommandToCategoryTest {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "Mexican";
    private CategoryCommandToCategory converter;

    @Before
    public void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        Category category = converter.convert(null);

        //Assert
        assertNull(category);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID);
        categoryCommand.setDescription(DESCRIPTION);

        //Act
        Category category = converter.convert(categoryCommand);

        //Assert
        assertNotNull(category);
        assertEquals(ID, category.getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }
}