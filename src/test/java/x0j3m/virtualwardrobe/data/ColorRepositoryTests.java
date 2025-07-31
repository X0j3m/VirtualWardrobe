package x0j3m.virtualwardrobe.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import x0j3m.virtualwardrobe.model.Color;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ColorRepositoryTests {
    @Autowired
    private ColorRepository colorRepository;

    @Test
    void findAll_shouldReturnEmptyWhenNoColors() {
        Iterable<Color> allColors = colorRepository.findAll();
        Assertions.assertNotNull(allColors);
        Assertions.assertFalse(allColors.iterator().hasNext());
    }

    @Test
    void save_shouldReturnSavedColor() {
        Color color = new Color("testColor");

        Color saved = colorRepository.save(color);

        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(color.getName(), saved.getName());
    }

    @Test
    void findAll_shouldReturnAllSavedColors() {
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
    void findById_shouldReturnColorWhenExists() {
        Color color = new Color("testColor");
        Color saved = colorRepository.save(color);

        long id = saved.getId();

        Color found = colorRepository.findById(id).orElse(null);
        Assertions.assertNotNull(found);
        Assertions.assertEquals(saved.getName(), found.getName());
    }
}
