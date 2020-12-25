package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesCommandToNotesTest {

    private static final Long ID = 1L;
    private static final String RECIPE_NOTES = "BlaBlaBla";

    private NotesCommandToNotes converter;

    @Before
    public void setUp() throws Exception {
        converter = new NotesCommandToNotes();
    }

    @Test
    public void shouldConvertNullObject() {
        //Arrange

        //Act
        Notes notes = converter.convert(null);

        //Assert
        assertNull(notes);
    }

    @Test
    public void shouldConvertNoneNullObject() {
        //Arrange
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(ID);
        notesCommand.setRecipeNotes(RECIPE_NOTES);

        //Act
        Notes notes = converter.convert(notesCommand);

        //Assert
        assertNotNull(notes);
        assertEquals(ID, notes.getId());
        assertEquals(RECIPE_NOTES, notes.getRecipeNotes());
    }
}