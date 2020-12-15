package guru.springframework.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryTest {
    private Category category;

    @Before
    public void setUp(){
        category = new Category();

    }

    @Test
    public void getId() {
        //Arrange
        Long idValue = 4L;

        //Act
        category.setId(idValue);

        //Assert
        assertEquals(idValue, category.getId());
    }
}


