package x0j3m.virtualwardrobe.data;

import org.junit.jupiter.api.Assertions;
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

    @Test
    void findAll_shouldReturnEmptyWhenNoClothesTypes() {
        Iterable<ClothesType> allClothesTypes = clothesTypeRepository.findAll();
        Assertions.assertNotNull(allClothesTypes);
        Assertions.assertFalse(allClothesTypes.iterator().hasNext());
    }

    @Test
    void save_shouldReturnSavedClothesType() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);

        ClothesType saved = clothesTypeRepository.save(clothesType);

        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(clothesType.getName(), saved.getName());
        Assertions.assertEquals(clothesType.getLayer(), saved.getLayer());
    }

    @Test
    void findAll_shouldReturnAllSavedClothesTypes() {
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
    void findById_shouldReturnClothesTypeWhenExists() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.MID_LAYER);
        ClothesType saved = clothesTypeRepository.save(clothesType);

        long id = saved.getId();

        ClothesType found = clothesTypeRepository.findById(id).orElse(null);
        Assertions.assertNotNull(found);
        Assertions.assertEquals(saved.getId(), found.getId());
        Assertions.assertEquals(saved.getName(), found.getName());
        Assertions.assertEquals(saved.getLayer(), found.getLayer());
    }
}
