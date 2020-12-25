package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureToUnitOfMeasureCommandTest {
    public static final Long ID = 1L;
    public static final String DESCRIPTION = "Teaspoon";

    private UnitOfMeasureToUnitOfMeasureCommand converter;

    @Before
    public void setUp() {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        UnitOfMeasureCommand unitOfMeasureCommand = converter.convert(null);

        //Assert
        assertNull(unitOfMeasureCommand);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(ID);
        unitOfMeasure.setDescription(DESCRIPTION);

        //Act
        UnitOfMeasureCommand unitOfMeasureCommand = converter.convert(unitOfMeasure);

        //Assert
        assertNotNull(unitOfMeasureCommand);
        assertEquals(ID, unitOfMeasureCommand.getId());
        assertEquals(DESCRIPTION, unitOfMeasureCommand.getDescription());
    }
}