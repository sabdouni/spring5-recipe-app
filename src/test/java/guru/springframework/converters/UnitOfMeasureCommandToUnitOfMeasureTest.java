package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureCommandToUnitOfMeasureTest {
    public static final Long ID = 1L;
    public static final String DESCRIPTION = "Teaspoon";

    private UnitOfMeasureCommandToUnitOfMeasure converter;

    @Before
    public void setUp() {
        converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        UnitOfMeasure unitOfMeasure = converter.convert(null);

        //Assert
        assertNull(unitOfMeasure);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(ID);
        unitOfMeasureCommand.setDescription(DESCRIPTION);

        //Act
        UnitOfMeasure unitOfMeasure = converter.convert(unitOfMeasureCommand);

        //Assert
        assertNotNull(unitOfMeasure);
        assertEquals(ID, unitOfMeasure.getId());
        assertEquals(DESCRIPTION, unitOfMeasure.getDescription());
    }
}