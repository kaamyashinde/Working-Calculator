package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.model.Calculation;
import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.service.CalculationService;
import ntnu.edu.stud.calculator.service.CalculatorService;
import ntnu.edu.stud.calculator.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CalculatorController {
  @Autowired
  private CalculatorService calculatorService;
  @SuppressWarnings("unused")
  @Autowired
  private UserService userService;
  @Autowired
  private CalculationService calculationService;
  private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);


  @PostMapping("/calculate")
  public ResponseEntity<Calculation> calculate(@RequestBody Map<String, String> request) {
    try {
      // Get the authenticated user from the security context
      User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (user == null) {
        logger.error("Unauthorized request");
        return ResponseEntity.status(401).build();
      }

      String expression = request.get("expression");
      logger.info("{} requested calculation: {}", user.getUsername(), expression);
      String result = calculatorService.performCalculation(expression);
      Calculation cal = calculationService.saveCalculation(expression, Double.parseDouble(result), user);
      return ResponseEntity.ok(cal);
    } catch (Exception e) {
      logger.error("Calculation error: {}", e.getMessage());
      return ResponseEntity.internalServerError().build();
    }
  }

  /*
  public CalculationResponse calculate(@RequestBody CalculationRequest request) {
    logger.info("Received calculation request: {}", request.getExpression());
    String result = calculatorService.performCalculation(request.getExpression());
    CalculationResponse response = new CalculationResponse(result, request.getExpression());
    return response;
  } */


  //Endpoing for getting all calculations for an user
  @GetMapping("/history")
  public ResponseEntity<?> getHistory(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
      try {
          // Get the authenticated user from the security context
          User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          if (user == null) {
              return ResponseEntity.status(401).body("Unauthorized");
          }

          logger.info("Fetching calculation history for user: {}", user.getUsername());
          
          // Get paginated calculations
          Page<Calculation> history = calculationService.getCalculationsForUser(user, page, size);
          
          // Create a response map with metadata
          Map<String, Object> response = new HashMap<>();
          response.put("content", history.getContent());
          response.put("currentPage", history.getNumber());
          response.put("totalItems", history.getTotalElements());
          response.put("totalPages", history.getTotalPages());
          
          return ResponseEntity.ok(response);
      } catch (Exception e) {
          logger.error("Error fetching history: {}", e.getMessage());
          return ResponseEntity.internalServerError().body("Error fetching calculation history");
      }
  }
}
