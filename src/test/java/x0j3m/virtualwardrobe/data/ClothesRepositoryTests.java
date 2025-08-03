package x0j3m.virtualwardrobe.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import x0j3m.virtualwardrobe.model.Clothes;
import x0j3m.virtualwardrobe.model.ClothesType;
import x0j3m.virtualwardrobe.model.ClothesLayer;
import x0j3m.virtualwardrobe.model.Color;

import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClothesRepositoryTests {
    @Autowired
    private ClothesRepository clothesRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private ClothesTypeRepository clothesTypeRepository;

    @BeforeEach
    void setUp() {
        clothesRepository.deleteAll();
        colorRepository.deleteAll();
        clothesTypeRepository.deleteAll();

        colorRepository.save(new Color("testColor1"));
        colorRepository.save(new Color("testColor2"));
        colorRepository.save(new Color("testColor3"));
        clothesTypeRepository.save(new ClothesType("testType1", ClothesLayer.ACCESSORY));
        clothesTypeRepository.save(new ClothesType("testType2", ClothesLayer.ACCESSORY));
        clothesTypeRepository.save(new ClothesType("testType3", ClothesLayer.ACCESSORY));
    }

    @Test
    void save_whenClothesDoesNotExists_shouldReturnSavedClothes() {
        Color color = colorRepository.findAll().iterator().next();
        ClothesType type = clothesTypeRepository.findAll().iterator().next();

        Clothes testClothes = new Clothes(color, type);

        Clothes saved = clothesRepository.save(testClothes);

        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(testClothes.getColor().getName(), saved.getColor().getName());
        Assertions.assertEquals(testClothes.getType(), saved.getType());
    }

    @Test
    void save_whenClothesExists_shouldReturnUpdatedClothes() {
        Color color = colorRepository.findAll().iterator().next();
        ClothesType type = clothesTypeRepository.findAll().iterator().next();
        Clothes testClothes = new Clothes(color, type);
        Clothes savedClothes = clothesRepository.save(testClothes);

        Color newColor = new Color("newColor");
        Color savedNewColor = colorRepository.save(newColor);
        Clothes updatedClothes = new Clothes(savedClothes.getId(), savedNewColor, type);

        Assertions.assertNotNull(updatedClothes);
        Assertions.assertEquals(savedClothes.getId(), updatedClothes.getId());
        Assertions.assertEquals(newColor.getName(), updatedClothes.getColor().getName());
        Assertions.assertEquals(savedClothes.getType(), updatedClothes.getType());
    }

    @Test
    void findAll_whenTableIsEmpty_shouldReturnEmpty() {
        Iterable<Clothes> allClothes = clothesRepository.findAll();
        Assertions.assertNotNull(allClothes);
        Assertions.assertFalse(allClothes.iterator().hasNext());
    }

    @Test
    void findAll_whenTableIsNotEmpty_shouldReturnAllSavedClothes() {
        Color color = colorRepository.findAll().iterator().next();
        ClothesType type = clothesTypeRepository.findAll().iterator().next();
        Clothes clothes1 = new Clothes(color, type);
        Clothes clothes2 = new Clothes(color, type);
        Clothes clothes3 = new Clothes(color, type);

        clothesRepository.save(clothes1);
        clothesRepository.save(clothes2);
        clothesRepository.save(clothes3);

        Iterable<Clothes> allClothes = clothesRepository.findAll();

        Assertions.assertNotNull(allClothes);
        Assertions.assertEquals(3, allClothes.spliterator().getExactSizeIfKnown());
    }

    @Test
    void findById_whenClothesExists_shouldReturnClothes() {
        Color color = colorRepository.findAll().iterator().next();
        ClothesType type = clothesTypeRepository.findAll().iterator().next();

        Clothes testClothes = new Clothes(color, type);
        Clothes saved = clothesRepository.save(testClothes);

        long id = saved.getId();

        Clothes found = clothesRepository.findById(id).orElse(null);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(saved.getId(), found.getId());
        Assertions.assertEquals(saved.getColor().getName(), found.getColor().getName());
        Assertions.assertEquals(saved.getType(), found.getType());
    }

    @Test
    void findById_whenClothesDoesNotExist_shouldReturnNull() {
        Clothes found = clothesRepository.findById(999L).orElse(null);
        Assertions.assertNull(found);
    }

    @Test
    void findByColor_Name_whenClothesExists_shouldReturnClothes() {
        List<Color> colors = (List<Color>) colorRepository.findAll();
        List<ClothesType> types = (List<ClothesType>) clothesTypeRepository.findAll();

        Clothes testClothes1 = new Clothes(colors.getFirst(), types.get(0));
        Clothes testClothes2 = new Clothes(colors.getFirst(), types.get(1));
        Clothes testClothes3 = new Clothes(colors.getLast(), types.get(2));

        clothesRepository.save(testClothes1);
        clothesRepository.save(testClothes2);
        clothesRepository.save(testClothes3);

        List<Clothes> found = (List<Clothes>) clothesRepository.findByColor_Name(colors.getFirst().getName());

        Assertions.assertNotNull(found);
        Assertions.assertEquals(2, found.spliterator().getExactSizeIfKnown());
        Assertions.assertTrue(found.iterator().hasNext());
        Assertions.assertEquals(testClothes1, found.getFirst());
        Assertions.assertEquals(testClothes2, found.getLast());
    }

    @Test
    void findByColor_Name_whenClothesDoesNotExist_shouldReturnEmpty() {
        List<Clothes> found = (List<Clothes>) clothesRepository.findByColor_Name("testColor");
        Assertions.assertNotNull(found);
        Assertions.assertEquals(0, found.spliterator().getExactSizeIfKnown());
        Assertions.assertFalse(found.iterator().hasNext());
    }

    @Test
    void findByType_Name_whenClothesExists_shouldReturnClothes() {
        List<Color> colors = (List<Color>) colorRepository.findAll();
        List<ClothesType> types = (List<ClothesType>) clothesTypeRepository.findAll();
        Clothes testClothes1 = new Clothes(colors.getFirst(), types.getFirst());
        Clothes testClothes2 = new Clothes(colors.get(1), types.getFirst());
        Clothes testClothes3 = new Clothes(colors.getLast(), types.getLast());

        clothesRepository.save(testClothes1);
        clothesRepository.save(testClothes2);
        clothesRepository.save(testClothes3);

        List<Clothes> found = (List<Clothes>) clothesRepository.findByType_Name(types.getFirst().getName());

        Assertions.assertNotNull(found);
        Assertions.assertEquals(2, found.spliterator().getExactSizeIfKnown());
        Assertions.assertTrue(found.iterator().hasNext());
        Assertions.assertEquals(testClothes1, found.getFirst());
        Assertions.assertEquals(testClothes1.getId(), found.getFirst().getId());
        Assertions.assertEquals(testClothes1.getColor(), found.getFirst().getColor());
        Assertions.assertEquals(testClothes1.getType(), found.getFirst().getType());
        Assertions.assertEquals(testClothes2, found.getLast());
        Assertions.assertEquals(testClothes2.getId(), found.getLast().getId());
        Assertions.assertEquals(testClothes2.getColor(), found.getLast().getColor());
        Assertions.assertEquals(testClothes2.getType(), found.getLast().getType());
    }

    @Test
    void findByType_Name_whenClothesDoesNotExist_shouldReturnEmpty() {
        List<Clothes> found = (List<Clothes>) clothesRepository.findByType_Name("testType");
        Assertions.assertNotNull(found);
        Assertions.assertEquals(0, found.spliterator().getExactSizeIfKnown());
        Assertions.assertFalse(found.iterator().hasNext());
    }

    @Test
    void deleteAll_whenTableIsNotEmpty_shouldDeleteAllClothes() {
        Color color = colorRepository.findAll().iterator().next();
        ClothesType type = clothesTypeRepository.findAll().iterator().next();

        Clothes clothes1 = new Clothes(color, type);
        Clothes clothes2 = new Clothes(color, type);
        Clothes clothes3 = new Clothes(color, type);

        clothesRepository.save(clothes1);
        clothesRepository.save(clothes2);
        clothesRepository.save(clothes3);

        clothesRepository.deleteAll();

        Iterable<Clothes> allClothes = clothesRepository.findAll();

        Assertions.assertNotNull(allClothes);
        Assertions.assertFalse(allClothes.iterator().hasNext());
    }

    @Test
    void deleteAll_whenTableIsEmpty_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> clothesRepository.deleteAll());
    }

    @Test
    void deleteById_whenClothesExists_shouldDeleteClothes() {
        Color color = colorRepository.findAll().iterator().next();
        ClothesType type = clothesTypeRepository.findAll().iterator().next();
        Clothes testClothes = new Clothes(color, type);
        Clothes saved = clothesRepository.save(testClothes);

        long savedId = saved.getId();
        clothesRepository.deleteById(savedId);

        Assertions.assertNull(clothesRepository.findById(savedId).orElse(null));
    }

    @Test
    void deleteById_whenClothesDoesNotExist_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> clothesRepository.deleteById(999L));
    }
}
