package ntnu.edu.stud.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalculatorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFullApplication() throws Exception {
        mockMvc.perform(post("/api/calculate")
                .contentType("application/json")
                .content("{\"expression\": \"10-5\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("5"));
    }
}
