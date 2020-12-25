package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesToNotesCommandTest {

    private static final Long ID = 1L;
    private static final String RECIPE_NOTES = "BlaBlaBla";

    private NotesToNotesCommand converter;

    @Before
    public void setUp() throws Exception {
        converter = new NotesToNotesCommand();
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        NotesCommand notesCommand = converter.convert(null);

        //Assert
        assertNull(notesCommand);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        Notes notes = new Notes();
        notes.setId(ID);
        notes.setRecipeNotes(RECIPE_NOTES);

        //Act
        NotesCommand notesCommand = converter.convert(notes);

        //Assert
        assertNotNull(notesCommand);
        assertEquals(ID, notesCommand.getId());
        assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
    }
}