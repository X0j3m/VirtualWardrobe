package x0j3m.virtualwardrobe.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import x0j3m.virtualwardrobe.model.Color;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ColorRepositoryTests {
    @Autowired
    private ColorRepository colorRepository;

    @BeforeEach
    void setUp() {
        colorRepository.deleteAll();
    }

    @Test
    void save_whenColorDoesNotExistsInDatabase_shouldReturnSavedColor() {
        Color color = new Color("testColor");

        Color saved = colorRepository.save(color);

        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertTrue(saved.getId() > 0);
        Assertions.assertEquals(color.getName(), saved.getName());
    }

    @Test
    void save_whenColorExistsInDatabase_shouldThrowDataIntegrityViolationException() {
        Color color = new Color("testColor");
        colorRepository.save(color);

        Color updatedColor = new Color("testColor");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> colorRepository.save(updatedColor));
    }

    @Test
    void save_whenColorNameExistsInDatabase_shouldThrowDataIntegrityViolationException() {
        Color color = new Color("testColor");
        colorRepository.save(color);

        Color updatedColor = new Color("testColor");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> colorRepository.save(updatedColor));
    }

    @Test
    void save_whenColorNameIsUpdated_shouldReturnUpdatedColor() {
        Color color = new Color("testColor");
        Color savedColor = colorRepository.save(color);

        Color updatedColor = new Color(savedColor.getId(), "updatedTestColor");
        Color updatedSavedColor = colorRepository.save(updatedColor);

        Assertions.assertNotNull(updatedSavedColor);
        Assertions.assertEquals(updatedColor.getName(), updatedSavedColor.getName());
        Assertions.assertEquals(savedColor.getId(), updatedSavedColor.getId());
    }

    @Test
    void findAll_whenTableIsEmpty_shouldReturnEmpty() {
        Iterable<Color> allColors = colorRepository.findAll();
        Assertions.assertNotNull(allColors);
        Assertions.assertFalse(allColors.iterator().hasNext());
    }

    @Test
    void findAll_whenTableIsNotEmpty_shouldReturnAllSavedColors() {
        Color color1 = new Color("testColor1");
        Color color2 = new Color("testColor2");
        Color color3 = new Color("testColor3");
        colorRepository.save(color1);
        colorRepository.save(color2);
        colorRepository.save(color3);

        Iterable<Color> allColors = colorRepository.findAll();

        Assertions.assertNotNull(allColors);
        Assertions.assertEquals(3, allColors.spliterator().getExactSizeIfKnown());
    }

    @Test
    void findById_whenColorExists_shouldReturnColor() {
        Color color = new Color("testColor");
        Color saved = colorRepository.save(color);

        long id = saved.getId();

        Color found = colorRepository.findById(id).orElse(null);
        Assertions.assertNotNull(found);
        Assertions.assertEquals(saved.getName(), found.getName());
    }

    @Test
    void findById_whenColorDoesNotExist_shouldReturnNull() {
        Color found = colorRepository.findById(999L).orElse(null);
        Assertions.assertNull(found);
    }

    @Test
    void findByName_whenColorExists_shouldReturnColor() {
        Color color = new Color("testColor");
        Color saved = colorRepository.save(color);

        Assertions.assertNotNull(saved);
        Assertions.assertEquals(color.getName(), saved.getName());
        Assertions.assertNotNull(saved.getId());
        Assertions.assertTrue(saved.getId() > 0);
    }

    @Test
    void findByName_whenColorDoesNotExists_shouldReturnNull() {
        Color found = colorRepository.findByName("testName").orElse(null);
        Assertions.assertNull(found);
    }

    @Test
    void deleteAll_whenTableIsNotEmpty_shouldDeleteAllColors() {
        Color color1 = new Color("testColor1");
        Color color2 = new Color("testColor2");
        Color color3 = new Color("testColor3");

        colorRepository.save(color1);
        colorRepository.save(color2);
        colorRepository.save(color3);

        colorRepository.deleteAll();

        Iterable<Color> allColors = colorRepository.findAll();

        Assertions.assertNotNull(allColors);
        Assertions.assertFalse(allColors.iterator().hasNext());
    }

    @Test
    void deleteAll_whenTableIsEmpty_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> colorRepository.deleteAll());
    }

    @Test
    void deleteById_whenColorExists_shouldDeleteColor() {
        Color color = new Color("testColor");
        Color saved = colorRepository.save(color);

        long savedId = saved.getId();
        colorRepository.deleteById(savedId);

        Color found = colorRepository.findById(savedId).orElse(null);

        Assertions.assertNull(found);
    }

    @Test
    void deleteById_whenColorDoesNotExist_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> colorRepository.deleteById(999L));
    }

    @Test
    void deleteByName_whenColorExists_shouldDeleteColor() {
        Color color = new Color("testColor");
        Color saved = colorRepository.save(color);

        String savedName = saved.getName();
        colorRepository.deleteByName(savedName);

        Color found = colorRepository.findByName(savedName).orElse(null);

        Assertions.assertNull(found);
    }

    @Test
    void deleteByName_whenColorDoesNotExist_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> colorRepository.deleteByName("testName"));
    }
}