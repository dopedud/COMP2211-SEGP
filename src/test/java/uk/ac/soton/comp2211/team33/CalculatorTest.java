package uk.ac.soton.comp2211.team33;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uk.ac.soton.comp2211.team33.entities.Obstacle;
import uk.ac.soton.comp2211.team33.utilities.Calculator;
import uk.ac.soton.comp2211.team33.entities.Runway;

@DisplayName("Test for runway re-declaration calculation.")
public class CalculatorTest {

  @Test
  @DisplayName("Test for calculated TORA away from obstacle.")
  public void toraTowards() {
    Runway runway1 = new Runway("09L", 3902, 3902, 3902, 3595,
        240, 306);
    runway1.setCurrentObs(new Obstacle("Obstacle", 25,2500));

    Calculator.toraTowardsObs(runway1);

    assertEquals(1496, runway1.getCtora(), "Calculated TORA of runway1 is incorrect " +
        "after calculation.");
  }
}
