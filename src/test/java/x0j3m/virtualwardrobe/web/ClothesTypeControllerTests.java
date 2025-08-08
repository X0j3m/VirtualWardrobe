package x0j3m.virtualwardrobe.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import x0j3m.virtualwardrobe.model.ClothesLayer;
import x0j3m.virtualwardrobe.model.ClothesType;
import x0j3m.virtualwardrobe.service.ClothesTypeService;

import java.util.List;

@WebMvcTest(controllers = ClothesTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ClothesTypeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        ClothesTypeService clothesTypeService() {
            return Mockito.mock(ClothesTypeService.class);
        }
    }

    @Autowired
    private ClothesTypeService clothesTypeService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getClothesType_whenClothesTypeExists_shouldReturnClothesType() throws Exception {
        ClothesType clothesType = new ClothesType(1L, "testName", ClothesLayer.BASE_LAYER);
        Mockito.when(clothesTypeService.getClothesType(Mockito.anyLong())).thenReturn(clothesType);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.get("/clothes-types/1")
        );

        Assertions.assertNotNull(clothesType.getLayer());
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(clothesType.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(clothesType.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.layer").value(clothesType.getLayer().toString()));
    }

    @Test
    void getClothesType_whenClothesTypeDoesNotExist_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(clothesTypeService.getClothesType(Mockito.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.get("/clothes-types/1")
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getClothesType_whenPathVariableIsNotValid_shouldReturnBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/clothes-types/not_valid")
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getClothesType_whenIdIsZeroOrNegative_shouldReturnNotFoundStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/clothes-types/0")
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());

        response = mockMvc.perform(MockMvcRequestBuilders
                .get("/clothes-types/-1")
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getAllClothesTypes_whenTableIsNotEmpty_shouldReturnAllSavedClothestypes() throws Exception {
        ClothesType clothesType1 = new ClothesType(1L, "testName1", ClothesLayer.BASE_LAYER);
        ClothesType clothesType2 = new ClothesType(2L, "testName2", ClothesLayer.MID_LAYER);
        ClothesType clothesType3 = new ClothesType(3L, "testName3", ClothesLayer.OUTER_LAYER);
        List<ClothesType> clothestypes = List.of(clothesType1, clothesType2, clothesType3);
        Mockito.when(clothesTypeService.getAllClothesTypes(
                Mockito.anyInt(), Mockito.anyInt(), Mockito.any())
        ).thenReturn(clothestypes);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/clothes-types")
        );

        Assertions.assertNotNull(clothesType1.getLayer());
        Assertions.assertNotNull(clothesType2.getLayer());
        Assertions.assertNotNull(clothesType3.getLayer());
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(clothesType1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(clothesType1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].layer").value(clothesType1.getLayer().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(clothesType2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(clothesType2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].layer").value(clothesType2.getLayer().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(clothesType3.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(clothesType3.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].layer").value(clothesType3.getLayer().toString()));
    }

    @Test
    void getAllClothesTypes_whenTableIsEmpty_shouldReturnEmptyList() throws Exception {
        Mockito.when(clothesTypeService.getAllClothesTypes(
                Mockito.anyInt(), Mockito.anyInt(), Mockito.any())
        ).thenReturn(List.of());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/clothes-types")
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void saveClothesType_whenClothesTypeDoesNotExistsInDatabase_shouldReturnCreatedStatus() throws Exception {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);

        Mockito.when(clothesTypeService.saveClothesType(Mockito.any(ClothesType.class))).thenReturn(1L);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/clothes-types")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clothesType))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/clothes-types/1"));
    }

    @Test
    void saveClothesType_whenClothesTypeNameExistsInDatabase_shouldReturnBadRequestStatus() throws Exception {
        ClothesType clothesType = new ClothesType("testName", ClothesLayer.BASE_LAYER);
        Mockito.when(clothesTypeService.saveClothesType(Mockito.any(ClothesType.class))).thenThrow(new IllegalArgumentException());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/clothes-types")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clothesType))
                .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveClothesType_whenClothesTypeIsNull_shouldReturnBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/clothes-types")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null))
                .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveClothesType_whenClothesTypeLayerIsNull_shouldReturnBadRequestStatus() throws Exception {
        ClothesType clothesType = new ClothesType("testName", null);
        Mockito.when(clothesTypeService.saveClothesType(Mockito.any(ClothesType.class))).thenThrow(new IllegalArgumentException());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/clothes-types")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clothesType))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveClothesType_whenClothesTypeNameIsNull_shouldReturnBadRequestStatus() throws Exception {
        ClothesType clothesType = new ClothesType(null, ClothesLayer.BASE_LAYER);
        Mockito.when(clothesTypeService.saveClothesType(Mockito.any(ClothesType.class))).thenThrow(new IllegalArgumentException());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/clothes-types")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clothesType))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveClothesType_whenClothesTypeLayerIsInvalid_shouldReturnBadRequestStatus() throws Exception {
        Mockito.when(clothesTypeService.saveClothesType(Mockito.any(ClothesType.class))).thenThrow(new IllegalArgumentException());
        String content =
                """
                {
                    "name": "testName",
                    "layer": "invalidLayer"
                }
                """;
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/clothes-types")
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteClothesType_whenClothesTypeExists_shouldReturnNoContentStatus() throws Exception {
        Mockito.doNothing().when(clothesTypeService).deleteClothesType(Mockito.anyLong());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/clothes-types/1")
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteClothesType_whenClothesTypeDoesNotExist_shouldReturnNotFoundStatus() throws Exception {
        Mockito.doThrow(new IllegalArgumentException()).when(clothesTypeService).deleteClothesType(Mockito.anyLong());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/clothes-types/1")
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteClothesType_whenPathVariableIsNotValid_shouldReturnBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/clothes-types/not_valid")
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteClothesType_whenIdNotGiven_shouldReturnMethodNotAllowedStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/clothes-types")
        );

        response.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }
}
