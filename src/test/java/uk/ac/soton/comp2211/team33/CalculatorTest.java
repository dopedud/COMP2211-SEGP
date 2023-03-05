package uk.ac.soton.comp2211.team33;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uk.ac.soton.comp2211.team33.entities.Aircraft;
import uk.ac.soton.comp2211.team33.entities.Obstacle;
import uk.ac.soton.comp2211.team33.utilities.Calculator;
import uk.ac.soton.comp2211.team33.entities.Runway;

public class CalculatorTest {
  @DisplayName("Testing calculation of runway from TORA towards...")
  @Test
  public void toraTowards() {
    Runway runway = new Runway("09L", 3902, 3902, 3902, 3595,
        240, 306);
    runway.setCurrentObs(new Obstacle("Obstacle", 25,2500));

    Calculator.toraTowardsObs(runway);

    assertEquals(1496, runway.getCtora(), "TORA of runway is incorrect " +
        "after calculation.");
  }
}
