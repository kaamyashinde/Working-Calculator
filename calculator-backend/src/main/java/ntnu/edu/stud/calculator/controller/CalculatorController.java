package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.model.CalculationRequest;
import ntnu.edu.stud.calculator.model.CalculationResponse;
import ntnu.edu.stud.calculator.model.Calculation;
import ntnu.edu.stud.calculator.service.CalculatorService;
import ntnu.edu.stud.calculator.service.CalculationService;
import ntnu.edu.stud.calculator.service.UserService;
import ntnu.edu.stud.calculator.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CalculatorController {
  private final CalculatorService calculatorService;
  private final CalculationService calculationService;
  private final UserService userService;
  private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

  @Autowired
  public CalculatorController(CalculatorService calculatorService, 
                            CalculationService calculationService,
                            UserService userService) {
    this.calculatorService = calculatorService;
    this.calculationService = calculationService;
    this.userService = userService;
  }

  @PostMapping("/calculate")
  public CalculationResponse calculate(@RequestBody CalculationRequest request) {
    logger.info("Received calculation request: {}", request.getExpression());
    String result = calculatorService.performCalculation(request.getExpression());
    CalculationResponse response = new CalculationResponse(result, request.getExpression());
    return response;
  }

  // Endpoint to retrieve the latest calculations (paginated)
  @GetMapping("/history")
  public ResponseEntity<Page<Calculation>> getHistory(@RequestParam String username,
                                                      @RequestParam String password,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
      User user = userService.login(username, password)
              .orElse(null);
      if (user == null) {
          return ResponseEntity.status(401).build();
      }
      Page<Calculation> history = calculationService.getCalculationsForUser(user, page, size);
      return ResponseEntity.ok(history);
  }
}
