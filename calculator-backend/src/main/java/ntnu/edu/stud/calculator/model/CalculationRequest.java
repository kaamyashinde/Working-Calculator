package ntnu.edu.stud.calculator.model;

public class CalculationRequest {
  private final String expression;

  public CalculationRequest(String expression) {
    this.expression = expression;
  }

  public String getExpression() {
    return expression;
  }


}
