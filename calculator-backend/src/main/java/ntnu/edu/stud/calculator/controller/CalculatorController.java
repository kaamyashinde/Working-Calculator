package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.model.CalculationRequest;
import ntnu.edu.stud.calculator.model.CalculationResponse;
import ntnu.edu.stud.calculator.service.CalculatorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CalculatorController {
  private final CalculatorService calculatorService;
  private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

  public CalculatorController(CalculatorService calculatorService) {
    this.calculatorService = calculatorService;
  }

  @PostMapping("/calculate")
  public CalculationResponse calculate(@RequestBody CalculationRequest request) {
    logger.info("Received calculation request: {}", request.getExpression());
    String result = calculatorService.performCalculation(request.getExpression());
    CalculationResponse response = new CalculationResponse(result, request.getExpression());
    return response;
  }
}
