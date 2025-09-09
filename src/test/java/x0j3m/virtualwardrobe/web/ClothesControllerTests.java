package x0j3m.virtualwardrobe.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import x0j3m.virtualwardrobe.config.JwtFilter;
import x0j3m.virtualwardrobe.web.dto.ClothesRequestDTO;
import x0j3m.virtualwardrobe.model.Clothes;
import x0j3m.virtualwardrobe.model.ClothesLayer;
import x0j3m.virtualwardrobe.model.ClothesType;
import x0j3m.virtualwardrobe.model.Color;
import x0j3m.virtualwardrobe.service.ClothesService;

import java.net.URI;
import java.util.List;

@WebMvcTest(controllers = ClothesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ClothesControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        ClothesService clothesService() {
            return Mockito.mock(ClothesService.class);
        }

        @Bean
        ClothesTypeController clothesTypeController() {
            return Mockito.mock(ClothesTypeController.class);
        }

        @Bean
        ColorController colorController() {
            return Mockito.mock(ColorController.class);
        }

        @Bean
        JwtFilter jwtFilter() {return Mockito.mock(JwtFilter.class);}
    }

    @Autowired
    private ClothesService clothesService;
    @Autowired
    private ClothesTypeController clothesTypeController;
    @Autowired
    private ColorController colorController;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getClothes_whenClothesExists_shouldReturnClothes() throws Exception {
        Color color = new Color(1L, "testColor");
        ClothesType clothesType = new ClothesType(1L, "testClothesType", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color, clothesType);
        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(clothes);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/clothes/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1)).andExpect(MockMvcResultMatchers.jsonPath("$.color.id").value(1)).andExpect(MockMvcResultMatchers.jsonPath("$.color.name").value("testColor")).andExpect(MockMvcResultMatchers.jsonPath("$.type.id").value(1)).andExpect(MockMvcResultMatchers.jsonPath("$.type.name").value("testClothesType")).andExpect(MockMvcResultMatchers.jsonPath("$.type.layer").value(ClothesLayer.BASE_LAYER.toString()));
    }

    @Test
    void getClothes_whenClothesDoesNotExist_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/clothes/1"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getClothes_whenPathVariableIsNotValid_shouldReturnBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/clothes/not_valid"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getClothes_whenIdIsZeroOrNegative_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/clothes/-1"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());

        response = mockMvc.perform(MockMvcRequestBuilders.get("/clothes/0"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getAllClothes_whenTableIsNotEmpty_shouldReturnAllSavedClothes() throws Exception {
        Color color1 = new Color(1L, "testColor1");
        Color color2 = new Color(2L, "testColor2");
        Color color3 = new Color(3L, "testColor3");
        ClothesType clothesType1 = new ClothesType(1L, "testClothesType1", ClothesLayer.BASE_LAYER);
        ClothesType clothesType2 = new ClothesType(2L, "testClothesType2", ClothesLayer.MID_LAYER);
        ClothesType clothesType3 = new ClothesType(3L, "testClothesType3", ClothesLayer.BOTTOMWEAR);
        Clothes clothes1 = new Clothes(1L, color1, clothesType1);
        Clothes clothes2 = new Clothes(2L, color2, clothesType2);
        Clothes clothes3 = new Clothes(3L, color3, clothesType3);
        Mockito.when(clothesService.getAllClothes(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(List.of(clothes1, clothes2, clothes3));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/clothes"));

        response.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1)).andExpect(MockMvcResultMatchers.jsonPath("$[0].color.id").value(1)).andExpect(MockMvcResultMatchers.jsonPath("$[0].color.name").value("testColor1")).andExpect(MockMvcResultMatchers.jsonPath("$[0].type.id").value(1)).andExpect(MockMvcResultMatchers.jsonPath("$[0].type.name").value("testClothesType1")).andExpect(MockMvcResultMatchers.jsonPath("$[0].type.layer").value(ClothesLayer.BASE_LAYER.toString()))

                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2)).andExpect(MockMvcResultMatchers.jsonPath("$[1].color.id").value(2)).andExpect(MockMvcResultMatchers.jsonPath("$[1].color.name").value("testColor2")).andExpect(MockMvcResultMatchers.jsonPath("$[1].type.id").value(2)).andExpect(MockMvcResultMatchers.jsonPath("$[1].type.name").value("testClothesType2")).andExpect(MockMvcResultMatchers.jsonPath("$[1].type.layer").value(ClothesLayer.MID_LAYER.toString()))

                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3)).andExpect(MockMvcResultMatchers.jsonPath("$[2].color.id").value(3)).andExpect(MockMvcResultMatchers.jsonPath("$[2].color.name").value("testColor3")).andExpect(MockMvcResultMatchers.jsonPath("$[2].type.id").value(3)).andExpect(MockMvcResultMatchers.jsonPath("$[2].type.name").value("testClothesType3")).andExpect(MockMvcResultMatchers.jsonPath("$[2].type.layer").value(ClothesLayer.BOTTOMWEAR.toString()));
    }

    @Test
    void getAllClothes_whenTableIsEmpty_shouldReturnEmptyList() throws Exception {
        Mockito.when(clothesService.getAllClothes(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(List.of());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/clothes"));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DirtiesContext
    void saveClothes_whenParametersAreValid_shouldReturnCreatedStatus() throws Exception {
        Color color = new Color(1L, "testColor");
        ClothesType clothesType = new ClothesType(1L, "testClothesType", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color, clothesType);
        ClothesRequestDTO requestDto = new ClothesRequestDTO(color.getId(), clothesType.getId());

        Mockito.when(clothesService.saveClothes(Mockito.anyLong(), Mockito.anyLong())).thenReturn(clothes.getId());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/clothes").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDto)).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.header().string("Location", "/clothes/1"));
    }

    @Test
    @DirtiesContext
    void saveClothes_whenGivenColorDoesNotExists_shouldReturnBadRequestStatus() throws Exception {
        ClothesRequestDTO requestDto = new ClothesRequestDTO(999L, 1L);

        Mockito.when(clothesService.saveClothes(Mockito.anyLong(), Mockito.anyLong())).thenThrow(new IllegalArgumentException());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/clothes").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDto)).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void saveClothes_whenGivenClothesTypeDoesNotExists_shouldReturnBadRequestStatus() throws Exception {
        ClothesRequestDTO requestDto = new ClothesRequestDTO(1L, 999L);

        Mockito.when(clothesService.saveClothes(Mockito.anyLong(), Mockito.anyLong())).thenThrow(new IllegalArgumentException());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/clothes").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDto)).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void saveClothes_whenGivenIdsAreZeroNegativeOrNull_shouldReturnBadRequestStatus() throws Exception {
        Mockito.when(clothesService.saveClothes(Mockito.anyLong(), Mockito.anyLong())).thenThrow(new IllegalArgumentException());

        ClothesRequestDTO requestDto;
        ResultActions response;

        requestDto = new ClothesRequestDTO(-1L, 1L);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/clothes").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDto)).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());


        requestDto = new ClothesRequestDTO(0L, 1L);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/clothes").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDto)).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());


        requestDto = new ClothesRequestDTO(1L, -1L);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/clothes").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDto)).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());


        requestDto = new ClothesRequestDTO(1L, 0L);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/clothes").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDto)).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteClothes_whenClothesExists_shouldReturnNoContentStatus() throws Exception {
        Mockito.doNothing().when(clothesService).deleteClothes(Mockito.anyLong());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/clothes/1"));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteClothes_whenClothesDoesNotExists_shouldReturnNoContentStatus() throws Exception {
        Mockito.doThrow(new IllegalArgumentException()).when(clothesService).deleteClothes(Mockito.anyLong());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/clothes/999"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteClothes_whenPathVariableIsNotValid_shouldReturnBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/clothes/not_valid"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteClothes_whenIdNotGiven_shouldReturnMethodNotAllowedStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/clothes"));

        response.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    void updateClothes_whenClothesExistsAndRequestParametersAreValid_shouldReturnCreatedStatus() throws Exception {
        Color color1 = new Color(1L, "testColorName1");
        Color color2 = new Color(2L, "testColorName2");
        ClothesType clothesType1 = new ClothesType(1L, "testClothesType1", ClothesLayer.HEADWEAR);
        ClothesType clothesType2 = new ClothesType(2L, "testClothesType2", ClothesLayer.FOOTWEAR);
        Clothes clothes = new Clothes(1L, color1, clothesType1);
        Clothes updatedClothes = new Clothes(1L, color2, clothesType2);
        ClothesRequestDTO update = new ClothesRequestDTO(color2.getId(), clothesType2.getId());

        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(clothes);
        Mockito.when(colorController.getColor(Mockito.anyLong())).thenReturn(new ResponseEntity<>(color1, HttpStatus.OK));
        Mockito.when(clothesTypeController.getClothesType(Mockito.anyLong())).thenReturn(new ResponseEntity<>(clothesType1, HttpStatus.OK));
        Mockito.when(clothesService.updateClothes(Mockito.anyLong(), Mockito.any())).thenReturn(updatedClothes);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/clothes/1").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(update)).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.header().string("Location", "/clothes/1"));
    }

    @Test
    void updateClothes_whenClothesDoesNotExists_shouldReturnNotFoundStatus() throws Exception {
        ClothesRequestDTO update = new ClothesRequestDTO(1L, 1L);

        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/clothes/999")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateClothes_whenUpdateColorIsNull_shouldReturnIsCreatedStatus() throws Exception {
        Color color = new Color(1L, "testColorName");
        ClothesType clothesType1 = new ClothesType(1L, "testClothesType1", ClothesLayer.HEADWEAR);
        ClothesType clothesType2 = new ClothesType(2L, "testClothesType2", ClothesLayer.FOOTWEAR);
        Clothes clothes = new Clothes(1L, color, clothesType1);
        Clothes updatedClothes = new Clothes(1L, color, clothesType2);
        ClothesRequestDTO update = new ClothesRequestDTO(null, 1L);

        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(clothes);
        Mockito.when(colorController.getColor(Mockito.anyLong())).thenReturn(new ResponseEntity<>(color, HttpStatus.OK));
        Mockito.when(clothesTypeController.getClothesType(Mockito.anyLong())).thenReturn(new ResponseEntity<>(clothesType1, HttpStatus.OK));
        Mockito.when(clothesService.updateClothes(Mockito.anyLong(), Mockito.any())).thenReturn(updatedClothes);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/clothes/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/clothes/1"));
    }

    @Test
    void updateClothes_whenUpdateClothesTypeIsNull_shouldReturnIsCreatedStatus() throws Exception {
        Color color1 = new Color(1L, "testColorName1");
        Color color2 = new Color(2L, "testColorName2");
        ClothesType clothesType = new ClothesType(1L, "testClothesType", ClothesLayer.BASE_LAYER);
        Clothes clothes = new Clothes(1L, color1, clothesType);
        Clothes updatedClothes = new Clothes(1L, color2, clothesType);
        ClothesRequestDTO update = new ClothesRequestDTO(1L, null);

        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(clothes);
        Mockito.when(colorController.getColor(Mockito.anyLong())).thenReturn(new ResponseEntity<>(color1, HttpStatus.OK));
        Mockito.when(clothesTypeController.getClothesType(Mockito.anyLong())).thenReturn(new ResponseEntity<>(clothesType, HttpStatus.OK));
        Mockito.when(clothesService.updateClothes(Mockito.anyLong(), Mockito.any())).thenReturn(updatedClothes);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/clothes/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/clothes/1"));
    }

    @Test
    void updateClothes_whenUpdateColorIdDoesNotExist_shouldReturnNotFoundStatus() throws Exception {
        Color color = new Color(1L, "testColorName");
        ClothesType clothesType = new ClothesType(1L, "testClothesType", ClothesLayer.HEADWEAR);
        Clothes clothes = new Clothes(1L, color, clothesType);
        ClothesRequestDTO update = new ClothesRequestDTO(999L, null);

        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(clothes);
        Mockito.when(colorController.getColor(Mockito.anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/clothes/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/clothes/1"));
    }

    @Test
    void updateClothes_whenUpdateClothesTypeIdDoesNotExist_shouldReturnNotFoundStatus() throws Exception {
        Color color = new Color(1L, "testColorName");
        ClothesType clothesType = new ClothesType(1L, "testClothesType", ClothesLayer.HEADWEAR);
        Clothes clothes = new Clothes(1L, color, clothesType);
        ClothesRequestDTO update = new ClothesRequestDTO(null, 999L);

        Mockito.when(clothesService.getClothes(Mockito.anyLong())).thenReturn(clothes);
        Mockito.when(clothesTypeController.getClothesType(Mockito.anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/clothes/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/clothes/1"));
    }

    @Test
    void updateClothes_whenPathVariableIsNotValid_shouldReturnBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/clothes/not_valid")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ClothesType(null, null)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
