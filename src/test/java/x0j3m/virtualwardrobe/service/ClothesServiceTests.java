package x0j3m.virtualwardrobe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import x0j3m.virtualwardrobe.data.ClothesRepository;
import x0j3m.virtualwardrobe.model.Clothes;
import x0j3m.virtualwardrobe.model.ClothesLayer;
import x0j3m.virtualwardrobe.model.ClothesType;
import x0j3m.virtualwardrobe.model.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClothesServiceTests {
    @Mock
    private ClothesRepository clothesRepository;
    @Mock
    private ColorService colorService;
    @Mock
    private ClothesTypeService clothesTypeService;

    @InjectMocks
    private ClothesService clothesService;

    @Test
    void saveClothes_whenClothesDoesNotExistAndParametersAreValid_shouldReturnSavedClothesId() {
        Color color = new Color(1L, "testColor");
        ClothesType type = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color, type);
        Mockito.when(colorService.getColor(Mockito.any(Long.class))).thenReturn(color);
        Mockito.when(clothesTypeService.getClothesType(Mockito.any(Long.class))).thenReturn(type);
        Mockito.when(clothesRepository.save(Mockito.any())).thenReturn(clothes);

        Long savedClothesId = clothesService.saveClothes(color.getId(), type.getId());

        Assertions.assertNotNull(savedClothesId);
        Assertions.assertEquals(clothes.getId(), savedClothesId);
    }

    @Test
    void saveClothes_whenColorIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.saveClothes(null, 1L));
    }

    @Test
    void saveClothes_whenClothesTypeIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.saveClothes(1L, null));
    }

    @Test
    void saveClothes_whenColorIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.saveClothes(-1L, 1L));
    }

    @Test
    void saveClothes_whenClothesTypeIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.saveClothes(1L, -1L));
    }

    @Test
    void saveClothes_whenColorIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.saveClothes(0L, 1L));
    }

    @Test
    void saveClothes_whenClothesTypeIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.saveClothes(1L, 0L));
    }

    @Test
    void saveClothes_whenColorIdDoesNotExist_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.saveClothes(999L, 1L));
    }

    @Test
    void saveClothes_whenLongIdDoesNotExist_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.saveClothes(1L, 999L));
    }

    @Test
    void getClothes_whenClothesIdExists_shouldReturnClothes() {
        Color color = new Color(1L, "testColor");
        ClothesType type = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color, type);
        Mockito.when(clothesRepository.findById(Mockito.any())).thenReturn(Optional.of(clothes));

        Clothes found = clothesService.getClothes(clothes.getId());

        Assertions.assertNotNull(found);
        Assertions.assertEquals(clothes.getId(), found.getId());
        Assertions.assertEquals(clothes.getColor(), found.getColor());
        Assertions.assertEquals(clothes.getType(), found.getType());
    }

    @Test
    void getClothes_whenIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.getClothes((Long) null));
    }

    @Test
    void getClothes_whenIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.getClothes(-1L));
    }

    @Test
    void getClothes_whenIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.getClothes(0L));
    }

    @Test
    void getClothes_whenClothesIdDoesNotExist_shouldReturnNull() {
        Mockito.when(clothesRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Clothes found = clothesService.getClothes(999L);
        Assertions.assertNull(found);
    }

    @Test
    void getAllClothes_whenTableIsEmpty_shouldReturnEmpty() {
        Mockito.when(clothesRepository.findAll()).thenReturn(java.util.Collections.emptyList());
        Iterable<Clothes> allClothes = clothesService.getAllClothes();
        Assertions.assertNotNull(allClothes);
        Assertions.assertFalse(allClothes.iterator().hasNext());
    }

    @Test
    void getAllClothes_whenTableIsNotEmpty_shouldReturnAllSavedClothes() {
        Color color1 = new Color(1L, "testColor1");
        Color color2 = new Color(2L, "testColor2");
        Color color3 = new Color(3L, "testColor3");
        ClothesType type1 = new ClothesType(1L, "testName1", ClothesLayer.BASE_LAYER);
        ClothesType type2 = new ClothesType(2L, "testName2", ClothesLayer.BASE_LAYER);
        ClothesType type3 = new ClothesType(3L, "testName3", ClothesLayer.BASE_LAYER);

        Clothes clothes1 = new Clothes(1L, color1, type1);
        Clothes clothes2 = new Clothes(2L, color2, type2);
        Clothes clothes3 = new Clothes(3L, color3, type3);

        Iterable<Clothes> clothesIterable = Arrays.asList(clothes1, clothes2, clothes3);

        Mockito.when(clothesRepository.findAll()).thenReturn(clothesIterable);

        Iterable<Clothes> allClothes = clothesService.getAllClothes();

        Assertions.assertNotNull(allClothes);
        Assertions.assertTrue(allClothes.iterator().hasNext());
        Assertions.assertEquals(3, allClothes.spliterator().getExactSizeIfKnown());
    }

    @Test
    void getClothesByColor_whenClothesWithGivenColorExists_shouldReturnAllClothesInGivenColor() {
        Color color = new Color(1L, "testColor1");
        ClothesType type1 = new ClothesType(1L, "testName1", ClothesLayer.BASE_LAYER);
        ClothesType type2 = new ClothesType(2L, "testName2", ClothesLayer.BASE_LAYER);
        ClothesType type3 = new ClothesType(3L, "testName3", ClothesLayer.BASE_LAYER);

        Clothes clothes1 = new Clothes(1L, color, type1);
        Clothes clothes2 = new Clothes(2L, color, type2);
        Clothes clothes3 = new Clothes(3L, color, type3);
        List<Clothes> clothesList = Arrays.asList(clothes1, clothes2, clothes3);

        Mockito.when(clothesRepository.findByColor_Name(Mockito.any())).thenReturn(clothesList);

        List<Clothes> found = (List<Clothes>) clothesService.getClothesByColor("testColor1");

        Assertions.assertNotNull(found);
        Assertions.assertEquals(3, found.size());
        Assertions.assertEquals(clothes1, found.getFirst());
        Assertions.assertEquals(clothes2, found.get(1));
        Assertions.assertEquals(clothes3, found.getLast());
        Assertions.assertEquals(color, found.getFirst().getColor());
        Assertions.assertEquals(color, found.get(1).getColor());
        Assertions.assertEquals(color, found.getLast().getColor());
    }

    @Test
    void getClothesByColor_whenClothesWithGivenColorNotExists_shouldReturnEmptyList() {
        Mockito.when(clothesRepository.findByColor_Name(Mockito.any())).thenReturn(Collections.emptyList());
        List<Clothes> found = (List<Clothes>) clothesService.getClothesByColor("testColor1");
        Assertions.assertNotNull(found);
        Assertions.assertTrue(found.isEmpty());
    }

    @Test
    void getClothesByColor_whenColorNameIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.getClothesByColor(null));
    }

    @Test
    void getClothesByColor_whenColorNameIsEmpty_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.getClothesByColor(""));
    }

    @Test
    void getClothesByType_whenClothesWithGivenTypeExists_shouldReturnAllClothesInGivenType() {
        Color color1 = new Color(1L, "testColor1");
        Color color2 = new Color(2L, "testColor2");
        Color color3 = new Color(3L, "testColor3");
        ClothesType type = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Clothes clothes1 = new Clothes(1L, color1, type);
        Clothes clothes2 = new Clothes(2L, color2, type);
        Clothes clothes3 = new Clothes(3L, color3, type);
        List<Clothes> clothesList = Arrays.asList(clothes1, clothes2, clothes3);

        Mockito.when(clothesRepository.findByType_Name(Mockito.any())).thenReturn(clothesList);

        List<Clothes> found = (List<Clothes>) clothesService.getClothesByType("testName");

        Assertions.assertNotNull(found);
        Assertions.assertEquals(3, found.size());
        Assertions.assertEquals(clothes1, found.getFirst());
        Assertions.assertEquals(clothes2, found.get(1));
        Assertions.assertEquals(clothes3, found.getLast());
    }

    @Test
    void getClothesByType_whenClothesWithGivenTypeNotExists_shouldReturnEmptyList() {
        Mockito.when(clothesRepository.findByType_Name(Mockito.any())).thenReturn(Collections.emptyList());
        List<Clothes> found = (List<Clothes>) clothesService.getClothesByType("testName");
        Assertions.assertNotNull(found);
        Assertions.assertTrue(found.isEmpty());
    }

    @Test
    void getClothesByType_whenClothesTypeIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.getClothesByType(null));
    }

    @Test
    void getClothesByType_whenClothesTypeIsEmpty_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.getClothesByType(""));
    }

    @Test
    void deleteClothes_whenClothesIdExists_shouldDeleteClothes() {
        Color color = new Color(1L, "testColor");
        ClothesType type = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color, type);
        Mockito.when(clothesRepository.findById(Mockito.any())).thenReturn(Optional.of(clothes));

        clothesService.deleteClothes(clothes.getId());
        Mockito.verify(clothesRepository, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    void deleteClothes_whenClothesIdDoesNotExist_shouldThrowIllegalArgumentException() {
        Mockito.when(clothesRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.deleteClothes(999L));
    }

    @Test
    void deleteClothes_whenClothesIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.deleteClothes(null));
    }

    @Test
    void deleteClothes_whenClothesIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.deleteClothes(-1L));
    }

    @Test
    void deleteClothes_whenClothesIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesService.deleteClothes(0L));
    }

    @Test
    void updateClothes_whenClothesIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->clothesService.updateClothes(null, new Clothes()));
    }

    @Test
    void updateClothes_whenClothesIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->clothesService.updateClothes(-1L, new Clothes()));
    }

    @Test
    void updateClothes_whenClothesIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->clothesService.updateClothes(0L, new Clothes()));
    }

    @Test
    void updateClothes_whenUpdateIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->clothesService.updateClothes(1L, null));
    }

    @Test
    void updateClothes_whenClothesWithProvidedIdDoesNotExist_shouldThrowIllegalArgumentException() {
        Mockito.when(clothesRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, ()->clothesService.updateClothes(999L, new Clothes()));
    }

    @Test
    void updateClothes_whenUpdateContainsValidData_shouldReturnUpdatedClothes() {
        Color color = new Color(1L, "testColor");
        ClothesType type = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color, type);
        Color updateColor = new Color(2L, "newColor");
        ClothesType updateType = new ClothesType(2L, "newName", ClothesLayer.BASE_LAYER);
        Clothes update = new Clothes(clothes.getId(), updateColor, updateType);
        Clothes updated = new Clothes(clothes.getId(), update.getColor(), update.getType());
        Mockito.when(clothesRepository.findById(Mockito.any())).thenReturn(Optional.of(clothes));
        Mockito.when(clothesRepository.save(Mockito.any())).thenReturn(updated);

        Clothes updatedClothes = clothesService.updateClothes(clothes.getId(), update);

        Assertions.assertNotNull(updatedClothes);
        Assertions.assertEquals(clothes.getId(), updatedClothes.getId());
        Assertions.assertEquals(update.getColor(), updatedClothes.getColor());
        Assertions.assertEquals(update.getType(), updatedClothes.getType());
    }

    @Test
    void updateClothes_whenUpdateColorIsNull_shouldReturnUpdatedClothes() {
        Color color = new Color(1L, "testColor");
        ClothesType type = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color, type);
        Clothes update = new Clothes(clothes.getId(), null, type);
        Clothes updated = new Clothes(clothes.getId(), clothes.getColor(), update.getType());
        Mockito.when(clothesRepository.findById(Mockito.any())).thenReturn(Optional.of(clothes));
        Mockito.when(clothesRepository.save(Mockito.any())).thenReturn(updated);

        Clothes updatedClothes = clothesService.updateClothes(clothes.getId(), update);
        Assertions.assertNotNull(updatedClothes);
        Assertions.assertEquals(clothes.getId(), updatedClothes.getId());
        Assertions.assertEquals(clothes.getColor(), updatedClothes.getColor());
        Assertions.assertEquals(update.getType(), updatedClothes.getType());
    }

    @Test
    void updateClothes_whenUpdateTypeIsNull_shouldReturnUpdatedClothes() {
        Color color = new Color(1L, "testColor");
        ClothesType type = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color, type);
        Clothes update = new Clothes(clothes.getId(), color, null);
        Clothes updated = new Clothes(clothes.getId(), update.getColor(), clothes.getType());
        Mockito.when(clothesRepository.findById(Mockito.any())).thenReturn(Optional.of(clothes));
        Mockito.when(clothesRepository.save(Mockito.any())).thenReturn(updated);

        Clothes updatedClothes = clothesService.updateClothes(clothes.getId(), update);
        Assertions.assertNotNull(updatedClothes);
        Assertions.assertEquals(clothes.getId(), updatedClothes.getId());
        Assertions.assertEquals(update.getColor(), updatedClothes.getColor());
        Assertions.assertEquals(clothes.getType(), updatedClothes.getType());
    }
}
