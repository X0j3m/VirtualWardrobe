package x0j3m.virtualwardrobe.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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
import x0j3m.virtualwardrobe.model.Color;
import x0j3m.virtualwardrobe.service.ColorService;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = ColorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ColorControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        ColorService colorService() {
            return Mockito.mock(ColorService.class);
        }
    }

    @Autowired
    private ColorService colorService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getColor_whenColorExists_shouldReturnFoundColorAndOkStatus() throws Exception {
        Mockito.when(colorService.getColor(Mockito.anyLong())).thenReturn(new Color(1L, "testColor"));

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.get("/colors/1")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testColor"));
    }

    @Test
    void getColor_whenColorDoesNotExist_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(colorService.getColor(Mockito.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.get("/colors/1")
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getAllColors_whenTableIsEmpty_shouldReturnEmpty() throws Exception {
        Mockito.when(colorService.getAllColors(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(List.of());

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.get("/colors")
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getAllColors_whenTableIsNotEmpty_shouldReturnAllSavedColors() throws Exception {
        Color color1 = new Color(1L, "testColor1");
        Color color2 = new Color(2L, "testColor2");
        Color color3 = new Color(3L, "testColor3");
        List<Color> colorsIterable = Arrays.asList(color1, color2, color3);
        Mockito.when(colorService.getAllColors(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(colorsIterable);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.get("/colors")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("testColor1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("testColor2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("testColor3"));
    }

    @Test
    void saveColor_whenColorDoesNotExist_shouldReturnSavedPathInHeader() throws Exception {
        Color color = new Color("testColor");
        Mockito.when(colorService.saveColor(Mockito.any(Color.class))).thenReturn(1L);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/colors")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(color))
                .contentType(MediaType.APPLICATION_JSON)

        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/colors/1"));
    }

    @Test
    void saveColor_whenColorNameExists_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(colorService.saveColor(Mockito.any(Color.class))).thenThrow(new IllegalArgumentException());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/colors")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Color("testColor")))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void saveColor_whenColorIsNull_shouldReturnBadRequestStatus() throws Exception {
        Mockito.when(colorService.saveColor(Mockito.any(Color.class))).thenThrow(new IllegalArgumentException());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/colors")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveColor_whenColorNameIsEmpty_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(colorService.saveColor(Mockito.any(Color.class))).thenThrow(new IllegalArgumentException());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/colors")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Color("")))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteColor_whenColorExists_shouldReturnNoContentStatus() throws Exception {
        Mockito.doNothing().when(colorService).deleteColor(Mockito.anyLong());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/colors/1")
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteColor_whenColorDoesNotExists_shouldReturnNotFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException()).when(colorService).deleteColor(Mockito.anyLong());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/colors/1")
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteColor_whenIdNotGiven_shouldReturnMethodNotAllowedStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/colors")
        );

        response.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    void deleteColor_whenPathVariableIsNotValid_shouldBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/colors/not_valid")
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateColor_whenColorIdExists_shouldReturnCreatedStatus() throws Exception {
        Color update = new Color(1L, "updatedColor");
        Color color = new Color(1L, "testColor");

        Mockito.when(colorService.updateColor(Mockito.any(Long.class), Mockito.any(Color.class))).thenReturn(update);
        Mockito.when(colorService.getColor(Mockito.anyLong())).thenReturn(color);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/colors/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Color(update.getName())))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/colors/1"));
    }

    @Test
    void updateColor_whenColorIdDoesNotExists_shouldReturnNotFoundStatus() throws Exception {
        Mockito.when(colorService.getColor(Mockito.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/colors/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Color("updatedColor")))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateColor_whenUpdateNameIsNull_shouldReturnNotFoundStatus() throws Exception {
        Color update = new Color(null);
        Mockito.when(colorService.getColor(Mockito.anyLong())).thenReturn(new Color(1L, "testColor"));
        Mockito.when(colorService.updateColor(1L,update)).thenThrow(new IllegalArgumentException());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/colors/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateColor_whenPathVariableIsNotValid_shouldReturnBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .patch("/colors/not_valid")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Color(null)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
