package x0j3m.virtualwardrobe.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import x0j3m.virtualwardrobe.model.ClothesLayer;
import x0j3m.virtualwardrobe.model.ClothesType;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClothesTypeRepositoryTests {
    @Autowired
    private ClothesTypeRepository clothesTypeRepository;

    @BeforeEach
    void setUp() {
        clothesTypeRepository.deleteAll();
    }

    @Test
    void save_whenClothesTypeDoesNotExistsInDatabase_shouldReturnSavedClothesType() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);

        ClothesType saved = clothesTypeRepository.save(clothesType);

        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(clothesType.getName(), saved.getName());
        Assertions.assertEquals(clothesType.getLayer(), saved.getLayer());
    }

    @Test
    void save_whenClothesTypeNameExistsInDatabase_shouldThrowDataIntegrityViolationException() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);
        clothesTypeRepository.save(clothesType);

        ClothesType updatedClothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);

        Assertions.assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> clothesTypeRepository.save(updatedClothesType));
    }

    @Test
    void save_whenClothesTypeNameIsUpdated_shouldReturnUpdatedClothesType() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);
        ClothesType savedClothesType = clothesTypeRepository.save(clothesType);

        ClothesType updatedClothesType = new ClothesType(savedClothesType.getId(), "updatedTestName", ClothesLayer.BASE_LAYER);
        ClothesType updatedSavedClothesType = clothesTypeRepository.save(updatedClothesType);

        Assertions.assertNotNull(updatedSavedClothesType);
        Assertions.assertEquals(updatedClothesType.getName(), updatedSavedClothesType.getName());
        Assertions.assertEquals(savedClothesType.getId(), updatedSavedClothesType.getId());
        Assertions.assertEquals(savedClothesType.getLayer(), updatedSavedClothesType.getLayer());
    }

    @Test
    void save_whenClothesTypeLayerIsUpdated_shouldReturnUpdatedClothesType() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);
        ClothesType savedClothesType = clothesTypeRepository.save(clothesType);

        ClothesType updatedClothesType = new ClothesType(savedClothesType.getId(), "updatedTestName", ClothesLayer.BOTTOMWEAR);
        ClothesType updatedSavedClothesType = clothesTypeRepository.save(updatedClothesType);

        Assertions.assertNotNull(updatedSavedClothesType);
        Assertions.assertEquals(savedClothesType.getName(), updatedSavedClothesType.getName());
        Assertions.assertEquals(updatedClothesType.getLayer(), updatedSavedClothesType.getLayer());
    }

    @Test
    void findAll_whenTableIsEmpty_shouldReturnEmpty() {
        Iterable<ClothesType> allClothesTypes = clothesTypeRepository.findAll();
        Assertions.assertNotNull(allClothesTypes);
        Assertions.assertFalse(allClothesTypes.iterator().hasNext());
    }

    @Test
    void findAll_whenTableIsNotEmpty_shouldReturnAllSavedClothesTypes() {
        ClothesType clothesType1 = new ClothesType("testName1", ClothesLayer.BASE_LAYER);
        ClothesType clothesType2 = new ClothesType("testName2", ClothesLayer.BOTTOMWEAR);
        ClothesType clothesType3 = new ClothesType("testName3", ClothesLayer.ACCESSORY);
        clothesTypeRepository.save(clothesType1);
        clothesTypeRepository.save(clothesType2);
        clothesTypeRepository.save(clothesType3);

        Iterable<ClothesType> allClothesTypes = clothesTypeRepository.findAll();

        Assertions.assertNotNull(allClothesTypes);
        Assertions.assertEquals(3, allClothesTypes.spliterator().getExactSizeIfKnown());
    }

    @Test
    void findById_whenClothesTypeExists_shouldReturnClothesType() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.MID_LAYER);
        ClothesType saved = clothesTypeRepository.save(clothesType);

        long id = saved.getId();

        ClothesType found = clothesTypeRepository.findById(id).orElse(null);
        Assertions.assertNotNull(found);
        Assertions.assertEquals(saved.getId(), found.getId());
        Assertions.assertEquals(saved.getName(), found.getName());
        Assertions.assertEquals(saved.getLayer(), found.getLayer());
    }

    @Test
    void findById_whenClothesTypeDoesNotExist_shouldReturnNull() {
        ClothesType found = clothesTypeRepository.findById(999L).orElse(null);
        Assertions.assertNull(found);
    }

    @Test
    void findByName_whenClothesTypeExists_shouldReturnClothesType() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.MID_LAYER);
        ClothesType saved = clothesTypeRepository.save(clothesType);

        ClothesType found = clothesTypeRepository.findByName(clothesType.getName()).orElse(null);
        Assertions.assertNotNull(found);
        Assertions.assertEquals(saved.getId(), found.getId());
        Assertions.assertEquals(clothesType.getName(), found.getName());
        Assertions.assertEquals(clothesType.getLayer(), found.getLayer());
    }

    @Test
    void findByName_whenClothesTypeDoesNotExist_shouldReturnNull() {
        ClothesType found = clothesTypeRepository.findByName("testName").orElse(null);
        Assertions.assertNull(found);
    }

    @Test
    void deleteAll_whenTableIsNotEmpty_shouldDeleteAllClothesType() {
        ClothesType clothesType1 = new ClothesType("testName1", ClothesLayer.BASE_LAYER);
        ClothesType clothesType2 = new ClothesType("testName2", ClothesLayer.BOTTOMWEAR);
        ClothesType clothesType3 = new ClothesType("testName3", ClothesLayer.ACCESSORY);

        clothesTypeRepository.save(clothesType1);
        clothesTypeRepository.save(clothesType2);
        clothesTypeRepository.save(clothesType3);

        clothesTypeRepository.deleteAll();

        Iterable<ClothesType> allClothesTypes = clothesTypeRepository.findAll();

        Assertions.assertNotNull(allClothesTypes);
        Assertions.assertFalse(allClothesTypes.iterator().hasNext());
    }

    @Test
    void deleteAll_whenTableIsEmpty_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> clothesTypeRepository.deleteAll());
    }

    @Test
    void deleteById_whenClothesTypeExists_shouldDeleteClothesType() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.MID_LAYER);
        ClothesType saved = clothesTypeRepository.save(clothesType);

        long savedId = saved.getId();
        clothesTypeRepository.deleteById(savedId);

        Assertions.assertNull(clothesTypeRepository.findById(savedId).orElse(null));
    }

    @Test
    void deleteById_whenClothesTypeDoesNotExist_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> clothesTypeRepository.deleteById(999L));
    }

    @Test
    void deleteByName_whenClothesTypeExists_shouldDeleteClothesType() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.MID_LAYER);
        ClothesType saved = clothesTypeRepository.save(clothesType);

        String savedName = saved.getName();
        clothesTypeRepository.deleteByName(savedName);

        Assertions.assertNull(clothesTypeRepository.findByName(savedName).orElse(null));
    }

    @Test
    void deleteByName_whenClothesTypeDoesNotExist_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> clothesTypeRepository.deleteByName("testName"));
    }
}
