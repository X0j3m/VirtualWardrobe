package x0j3m.virtualwardrobe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import x0j3m.virtualwardrobe.data.ClothesTypeRepository;
import x0j3m.virtualwardrobe.model.ClothesType;
import x0j3m.virtualwardrobe.model.ClothesLayer;

import java.util.*;
import java.util.stream.StreamSupport;

@ExtendWith(MockitoExtension.class)
public class ClothesTypeServiceTests {
    @Mock
    private ClothesTypeRepository clothesTypeRepository;
    @InjectMocks
    private ClothesTypeService clothesTypeService;

    @Test
    void saveClothesType_whenClothesTypeDoesNotExist_shouldReturnSavedClothesTypeId() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);
        Mockito.when(clothesTypeRepository.save(Mockito.any())).thenReturn(
                new ClothesType(
                        1L,
                        clothesType.getName(),
                        clothesType.getLayer()
                )
        );

        Long savedClothesType = clothesTypeService.saveClothesType(clothesType);

        Assertions.assertNotNull(savedClothesType);
    }

    @Test
    void saveClothesType_whenClothesTypeExists_shouldThrowIllegalArgumentException() {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);
        Mockito.when(clothesTypeRepository.findByName(clothesType.getName())).thenReturn(
                Optional.of(
                        new ClothesType(
                                1L,
                                clothesType.getName(),
                                clothesType.getLayer()
                        )
                )
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.saveClothesType(clothesType));
    }

    @Test
    void saveClothesType_whenClothesTypeIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.saveClothesType((ClothesType) null));
    }

    @Test
    void saveClothesType_whenClothesTypeNameIsEmpty_shouldThrowIllegalArgumentException() {
        ClothesType clothesType = new ClothesType("", ClothesLayer.BASE_LAYER);
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.saveClothesType(clothesType));
    }

    @Test
    void saveClothesType_whenClothesTypeNameIsNull_shouldThrowIllegalArgumentException() {
        ClothesType clothesType = new ClothesType(null, ClothesLayer.BASE_LAYER);
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.saveClothesType(clothesType));
    }

    @Test
    void saveClothesType_whenClothesTypeLayerIsNull_shouldThrowIllegalArgumentException() {
        ClothesType clothesType = new ClothesType("testName", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.saveClothesType(clothesType));
    }

    @Test
    void saveClothesType_String_ClothesLayer_whenClothesTypeNameIsEmpty_shouldThrowIllegalArgumentException() {
        String clothesTypeName = "";
        ClothesLayer clothesLayer = ClothesLayer.BASE_LAYER;
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.saveClothesType(clothesTypeName, clothesLayer));
    }

    @Test
    void saveClothesType_String_ClothesLayer_whenAnyParameterIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> clothesTypeService.saveClothesType(null, null)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> clothesTypeService.saveClothesType("notNull", null)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> clothesTypeService.saveClothesType(null, ClothesLayer.BASE_LAYER)
        );
    }

    @Test
    void getClothesType_whenClothesTypeIdExists_shouldReturnClothesType() {
        ClothesType clothesType = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Mockito.when(clothesTypeRepository.findById(Mockito.any())).thenReturn(Optional.of(clothesType));

        ClothesType found = clothesTypeService.getClothesType(1L);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(clothesType.getId(), found.getId());
        Assertions.assertEquals(clothesType.getName(), found.getName());
        Assertions.assertEquals(clothesType.getLayer(), found.getLayer());
    }

    @Test
    void getClothesType_whenClothesTypeIdDoesNotExist_shouldReturnNull() {
        Mockito.when(clothesTypeRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        ClothesType found = clothesTypeService.getClothesType(999L);

        Assertions.assertNull(found);
    }

    @Test
    void getClothesType_whenClothesTypeIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.getClothesType((Long) null));
    }

    @Test
    void getClothesType_whenClothesTypeIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.getClothesType(-1L));
    }

    @Test
    void getClothesType_whenClothesTypeIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.getClothesType(0L));
    }

    @Test
    void getClothesType_whenClothesTypeNameExists_shouldReturnClothesType() {
        ClothesType clothesType = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Mockito.when(clothesTypeRepository.findByName(clothesType.getName())).thenReturn(Optional.of(clothesType));

        ClothesType found = clothesTypeService.getClothesType(clothesType.getName());

        Assertions.assertNotNull(found);
        Assertions.assertEquals(clothesType.getId(), found.getId());
        Assertions.assertEquals(clothesType.getName(), found.getName());
        Assertions.assertEquals(clothesType.getLayer(), found.getLayer());
    }

    @Test
    void getClothesType_whenClothesTypeNameDoesNotExist_shouldReturnNull() {
        Mockito.when(clothesTypeRepository.findByName(Mockito.any())).thenReturn(Optional.empty());

        ClothesType found = clothesTypeService.getClothesType("testName");

        Assertions.assertNull(found);
    }

    @Test
    void getClothesType_whenClothesTypeNameIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.getClothesType((String) null));
    }

    @Test
    void getClothesType_whenClothesTypeNameIsEmpty_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.getClothesType(""));
    }

    @Test
    void getAllClothesTypes_whenTableIsEmpty_shouldReturnEmpty() {
        Mockito.when(clothesTypeRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        Iterable<ClothesType> allClothesTypes = clothesTypeService.getAllClothesTypes(0, 999, Sort.unsorted());

        Assertions.assertNotNull(allClothesTypes);
        Assertions.assertFalse(allClothesTypes.iterator().hasNext());
    }

    @Test
    void getAllClothesTypes_whenTableIsNotEmpty_shouldReturnAllClothesTypes() {
        ClothesType clothesType1 = new ClothesType(1L, "testName1", ClothesLayer.BASE_LAYER);
        ClothesType clothesType2 = new ClothesType(2L, "testName2", ClothesLayer.BOTTOMWEAR);
        ClothesType clothesType3 = new ClothesType(3L, "testName3", ClothesLayer.ACCESSORY);
        List<ClothesType> clothesTypeIterable = Arrays.asList(clothesType1, clothesType2, clothesType3);
        Page<ClothesType> page = new PageImpl<>(clothesTypeIterable, PageRequest.of(0, 3), 3);

        Mockito.when(clothesTypeRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(page);

        Iterable<ClothesType> allClothesTypes = clothesTypeService.getAllClothesTypes(0, 999, Sort.unsorted());

        Assertions.assertNotNull(allClothesTypes);
        Assertions.assertTrue(allClothesTypes.iterator().hasNext());
        Assertions.assertEquals(3, StreamSupport.stream(allClothesTypes.spliterator(), false).count());
    }

    @Test
    void deleteClothesType_whenClothesTypeExists_shouldDeleteClothesType() {
        ClothesType clothesType = new ClothesType(1L, "testName", ClothesLayer.MID_LAYER);
        Mockito.when(clothesTypeRepository.findById(Mockito.any())).thenReturn(Optional.of(clothesType));

        clothesTypeService.deleteClothesType(clothesType.getId());

        Mockito.verify(clothesTypeRepository, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    void deleteClothesType_whenClothesTypeDoesNotExist_shouldThrowIllegalArgumentException() {
        Mockito.when(clothesTypeRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.deleteClothesType(999L));
    }

    @Test
    void deleteClothesType_whenClothesTypeIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.deleteClothesType((Long) null));
    }

    @Test
    void deleteClothesType_whenClothesTypeIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.deleteClothesType(-1L));
    }

    @Test
    void deleteClothesType_whenClothesTypeIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.deleteClothesType(0L));
    }

    @Test
    void updateClothesType_whenIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.updateClothesType(null, new ClothesType()));
    }

    @Test
    void updateClothesType_whenIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.updateClothesType(-1L, new ClothesType()));
    }

    @Test
    void updateClothesType_whenIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.updateClothesType(0L, new ClothesType()));
    }

    @Test
    void updateClothesType_whenUpdateNameExists_shouldThrowIllegalArgumentException() {
        ClothesType clothesType = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Mockito.when(clothesTypeRepository.findByName(Mockito.any())).thenReturn(Optional.of(clothesType));

        ClothesType update = new ClothesType("updatedTestName", clothesType.getLayer());
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.updateClothesType(clothesType.getId(), update));
    }

    @Test
    void updateClothesType_whenClothesTypeWithProvidedIdDoesNotExist_shouldThrowIllegalArgumentException() {
        Mockito.when(clothesTypeRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.updateClothesType(999L, new ClothesType()));
    }

    @Test
    void updateClothesType_whenUpdateIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> clothesTypeService.updateClothesType(1L, null));
    }

    @Test
    void updateClothesType_whenUpdateContainsValidData_shouldReturnUpdatedClothesType() {
        ClothesType clothesType = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        ClothesType update = new ClothesType("updatedTestName", ClothesLayer.BOTTOMWEAR);
        ClothesType updated = new ClothesType(clothesType.getId(), update.getName(), update.getLayer());

        Mockito.when(clothesTypeRepository.findById(Mockito.any())).thenReturn(Optional.of(clothesType));
        Mockito.when(clothesTypeRepository.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(clothesTypeRepository.save(Mockito.any())).thenReturn(updated);

        ClothesType updatedClothesType = clothesTypeService.updateClothesType(clothesType.getId(), update);

        Assertions.assertNotNull(updatedClothesType);
        Assertions.assertEquals(clothesType.getId(), updatedClothesType.getId());
        Assertions.assertEquals(update.getName(), updatedClothesType.getName());
        Assertions.assertEquals(update.getLayer(), updatedClothesType.getLayer());
    }

    @Test
    void updateClothesType_whenUpdateNameIsNull_shouldReturnUpdatedClothesType() {
        ClothesType clothesType = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        ClothesType update = new ClothesType(null, ClothesLayer.BOTTOMWEAR);
        ClothesType updated = new ClothesType(clothesType.getId(), clothesType.getName(), update.getLayer());

        Mockito.when(clothesTypeRepository.findById(Mockito.any())).thenReturn(Optional.of(clothesType));
        Mockito.when(clothesTypeRepository.save(Mockito.any())).thenReturn(updated);

        ClothesType updatedClothesType = clothesTypeService.updateClothesType(clothesType.getId(), update);

        Assertions.assertNotNull(updatedClothesType);
        Assertions.assertEquals(clothesType.getId(), updatedClothesType.getId());
        Assertions.assertEquals(clothesType.getName(), updatedClothesType.getName());
        Assertions.assertEquals(update.getLayer(), updatedClothesType.getLayer());
    }

    @Test
    void updateClothesType_whenUpdateLayerIsNull_shouldReturnUpdatedClothesType() {
        ClothesType clothesType = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        ClothesType update = new ClothesType("updatedName", null);
        ClothesType updated = new ClothesType(clothesType.getId(), update.getName(), clothesType.getLayer());

        Mockito.when(clothesTypeRepository.findById(Mockito.any())).thenReturn(Optional.of(clothesType));
        Mockito.when(clothesTypeRepository.save(Mockito.any())).thenReturn(updated);

        ClothesType updatedClothesType = clothesTypeService.updateClothesType(clothesType.getId(), update);

        Assertions.assertNotNull(updatedClothesType);
        Assertions.assertEquals(clothesType.getId(), updatedClothesType.getId());
        Assertions.assertEquals(update.getName(), updatedClothesType.getName());
        Assertions.assertEquals(clothesType.getLayer(), updatedClothesType.getLayer());
    }
}