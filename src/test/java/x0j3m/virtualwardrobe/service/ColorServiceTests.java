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
import x0j3m.virtualwardrobe.data.ColorRepository;
import x0j3m.virtualwardrobe.model.Color;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ColorServiceTests {
    @Mock
    private ColorRepository colorRepository;

    @InjectMocks
    private ColorService colorService;

    @Test
    void saveColor_whenColorDoesNotExist_shouldReturnSavedColorId() {
        Color color = new Color("testColor");

        Mockito.when(colorRepository.findByName(color.getName())).thenReturn(Optional.empty());
        Mockito.when(colorRepository.save(color)).thenReturn(new Color(1L, color.getName()));

        Long savedColorId = colorService.saveColor(color);

        Assertions.assertNotNull(savedColorId);
    }

    @Test
    void saveColor_whenColorExists_shouldThrowIllegalArgumentException() {
        Color color = new Color("testColor");

        Mockito.when(colorRepository.findByName(color.getName())).thenReturn(Optional.of(color));

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.saveColor(color));
    }

    @Test
    void saveColor_whenColorIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.saveColor((Color) null));
    }

    @Test
    void saveColor_whenColorNameIsEmpty_shouldThrowIllegalArgumentException() {
        Color color = new Color("");

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.saveColor(color));
    }

    @Test
    void saveColor_whenColorNameIsNull_shouldThrowIllegalArgumentException() {
        Color color = new Color(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.saveColor(color));
    }

    @Test
    void saveColor_String_whenColorDoesNotExists_shouldReturnSavedColorId() {
        String colorName = "testColor";

        Mockito.when(colorRepository.findByName(colorName)).thenReturn(Optional.empty());
        Mockito.when(colorRepository.save(Mockito.any(Color.class))).thenReturn(new Color(1L, colorName));

        Long savedColorId = colorService.saveColor(colorName);

        Assertions.assertNotNull(savedColorId);
        Assertions.assertEquals(1L, savedColorId);
    }

    @Test
    void saveColor_String_whenColorExists_shouldThrowIllegalArgumentException() {
        String colorName = "testColor";

        Mockito.when(colorRepository.findByName(colorName)).thenReturn(Optional.of(new Color(1L, colorName)));

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.saveColor(colorName));
    }

    @Test
    void saveColor_String_whenColorNameIsEmpty_shouldThrowIllegalArgumentException() {
        String colorName = "";

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.saveColor(colorName));
    }

    @Test
    void saveColor_String_whenColorNameIsNull_shouldThrowIllegalArgumentException() {
        String colorName = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.saveColor(colorName));
    }

    @Test
    void getColor_whenColorIdExists_shouldReturnColor() {
        Color color = new Color(1L, "testColor");

        Mockito.when(colorRepository.findById(Mockito.any())).thenReturn(Optional.of(color));

        Color foundColor = colorService.getColor(color.getId());

        Assertions.assertNotNull(foundColor);
        Assertions.assertEquals(color.getName(), foundColor.getName());
    }

    @Test
    void getColor_whenColorIdDoesNotExist_shouldReturnNull() {
        Mockito.when(colorRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Color foundColor = colorService.getColor(999L);
        Assertions.assertNull(foundColor);
    }

    @Test
    void getColor_whenColorIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.getColor((Long) null));
    }

    @Test
    void getColor_whenColorIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.getColor(-1L));
    }

    @Test
    void getColor_whenColorIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.getColor(0L));
    }

    @Test
    void getColor_whenColorNameExists_shouldReturnColor() {
        Color color = new Color(1L, "testColor");

        Mockito.when(colorRepository.findByName(color.getName())).thenReturn(Optional.of(color));

        Color found = colorService.getColor(color.getName());

        Assertions.assertNotNull(found);
        Assertions.assertEquals(color.getId(), found.getId());
        Assertions.assertEquals(color.getName(), found.getName());
    }

    @Test
    void getColor_whenColorNameDoesNotExist_shouldReturnNull() {
        Mockito.when(colorRepository.findByName(Mockito.any())).thenReturn(Optional.empty());
        Color found = colorService.getColor("testColor");
        Assertions.assertNull(found);
    }

    @Test
    void getColor_whenColorNameIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.getColor((String) null));
    }

    @Test
    void getColor_whenColorNameIsEmpty_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.getColor(""));
    }

    @Test
    void getAllColors_whenTableIsEmpty_shouldReturnEmpty() {
        Mockito.when(colorRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        Iterable<Color> allColors = colorService.getAllColors(0, 999, Sort.unsorted());

        Assertions.assertNotNull(allColors);
        Assertions.assertFalse(allColors.iterator().hasNext());
    }

    @Test
    void getAllColors_whenTableIsNotEmpty_shouldReturnAllSavedColors() {
        Color color1 = new Color(1L, "testColor1");
        Color color2 = new Color(2L, "testColor2");
        Color color3 = new Color(3L, "testColor3");
        List<Color> colorsIterable = Arrays.asList(color1, color2, color3);
        Page<Color> page = new PageImpl<>(colorsIterable, PageRequest.of(0, 3), 3);

        Mockito.when(colorRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(page);

        Iterable<Color> allColors = colorService.getAllColors(0, 999, Sort.unsorted());

        Assertions.assertNotNull(allColors);
        Assertions.assertTrue(allColors.iterator().hasNext());
        Assertions.assertEquals(3, allColors.spliterator().getExactSizeIfKnown());
    }

    @Test
    void deleteColor_whenColorIdExists_shouldDeleteColor() {
        Color color = new Color(1L, "testColor");

        Mockito.when(colorRepository.findById(Mockito.any())).thenReturn(Optional.of(color));

        colorService.deleteColor(color.getId());

        Mockito.verify(colorRepository, Mockito.times(1)).deleteById(color.getId());
    }

    @Test
    void deleteColor_whenColorIdDoesNotExist_shouldThrowIllegalArgumentException() {
        Mockito.when(colorRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.deleteColor(999L));
    }

    @Test
    void deleteColor_whenColorIdIsNull_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.deleteColor(null));
    }

    @Test
    void deleteColor_whenColorIdIsNegative_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.deleteColor(-1L));
    }

    @Test
    void deleteColor_whenColorIdIsZero_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.deleteColor(0L));
    }

    @Test
    void updateColor_whenColorIdDoesNotExists_shouldThrowIllegalArgumentException() {
        Color update = new Color("testColor");
        Mockito.when(colorRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.updateColor(999L, update));
    }

    @Test
    void updateColor_whenColorIdExistsAndUpdateNameIsUnique_shouldReturnUpdatedColor() {
        Color color = new Color(1L, "testColor");
        Color update = new Color("newColor");

        Mockito.when(colorRepository.findById(Mockito.any())).thenReturn(Optional.of(color));
        Mockito.when(colorRepository.save(Mockito.any(Color.class))).thenReturn(new Color(color.getId(), update.getName()));

        Color updatedColor = colorService.updateColor(color.getId(), update);

        Assertions.assertNotNull(updatedColor);
        Assertions.assertEquals(color.getId(), updatedColor.getId());
        Assertions.assertEquals(update.getName(), updatedColor.getName());
    }

    @Test
    void updateColor_whenColorIdExistsAndUpdateNameIsNotUnique_shouldThrowIllegalArgumentException() {
        Color color = new Color(1L, "testColor");
        Color update = new Color("newColor");

        Mockito.when(colorRepository.findById(color.getId())).thenReturn(Optional.of(color));
        Mockito.when(colorRepository.save(new Color(color.getId(), update.getName()))).thenThrow(new IllegalArgumentException("Color name is not unique."));

        Assertions.assertThrows(IllegalArgumentException.class, () -> colorService.updateColor(color.getId(), update));
    }
}
