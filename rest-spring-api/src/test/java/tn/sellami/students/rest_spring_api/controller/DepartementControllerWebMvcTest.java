package tn.sellami.students.rest_spring_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.sellami.students.rest_spring_api.dto.DepartementDto;
import tn.sellami.students.rest_spring_api.exception.GlobalExceptionHandler;
import tn.sellami.students.rest_spring_api.exception.ResourceNotFoundException;
import tn.sellami.students.rest_spring_api.service.DepartementService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartementController.class)
@Import({GlobalExceptionHandler.class, DepartementControllerWebMvcTest.TestBeans.class})
class DepartementControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartementService departementService;

    @TestConfiguration
    static class TestBeans {
        @Bean
        DepartementService departementService() {
            return org.mockito.Mockito.mock(DepartementService.class);
        }
    }

    @Test
    void getDepartementByIdWhenMissingReturnsNotFound() throws Exception {
        when(departementService.findById(404L))
                .thenThrow(new ResourceNotFoundException("Departement with id 404 not found"));

        mockMvc.perform(get("/api/departements/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/departements/404"));
    }

    @Test
    void createDepartementWithInvalidBodyReturnsBadRequest() throws Exception {
        DepartementDto invalid = new DepartementDto(null, "");

        mockMvc.perform(post("/api/departements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/departements"));
    }
}
