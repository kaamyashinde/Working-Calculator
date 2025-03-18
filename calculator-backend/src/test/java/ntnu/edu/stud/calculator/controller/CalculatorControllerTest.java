package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;

    private MockMvc mockMvc;

    @Test
    void testCalculate() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(calculatorController).build();

        when(calculatorService.performCalculation("2+2")).thenReturn("4");

        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"expression\": \"2+2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("4"));
    }
}
