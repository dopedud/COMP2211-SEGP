package uk.ac.soton.comp2211.team33;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uk.ac.soton.comp2211.team33.entities.Aircraft;
import uk.ac.soton.comp2211.team33.entities.Obstacle;
import uk.ac.soton.comp2211.team33.utilities.Calculator;
import uk.ac.soton.comp2211.team33.entities.Runway;

@DisplayName("Test for runway re-declaration calculation.")
public class CalculatorTest {

  @Test
  @DisplayName("Test for calculated TORA towards from obstacle.")
  public void toraTowards() {
    Runway runway = new Runway("09L", 3902, 3902, 3902, 3595, 240, 307);
    runway.setCurrentObs(new Obstacle("Obstacle", 20, 3546));

    Calculator.toraTowardsObs(runway);

    assertEquals(2793, runway.getCtora(),
        "Calculated TORA of runway is incorrect after calculation.");
    assertEquals(2793, runway.getCtoda(),
        "Calculated TODA of runway is incorrect after calculation.");
    assertEquals(2793, runway.getCasda(),
        "Calculated ASDA of runway is incorrect after calculation.");

  }

  @Test
  @DisplayName("Test for calculated TORA away from obstacle.")
  public void toraAway() {
    Runway runway = new Runway("27R", 3884, 3962, 3884, 3884, 240, 0,
        new Aircraft("A380", 300));
    runway.setCurrentObs(new Obstacle("Obstacle", 20,50));

    Calculator.toraAwayObs(runway);

    assertEquals(3534, runway.getCtora(),
        "Calculated TORA of runway is incorrect after calculation.");
    assertEquals(3612, runway.getCtoda(),
        "Calculated TODA of runway is incorrect after calculation.");
    assertEquals(3534, runway.getCasda(),
        "Calculated ASDA of runway is incorrect after calculation.");

  }

  @Test
  @DisplayName("Test for calculated LDA towards obstacle.")
  public void ldaTowards() {
    Runway runway = new Runway("09L", 3902, 3902, 3902, 3595, 240, 307);
    runway.setCurrentObs(new Obstacle("Obstacle", 20, 3546));

    Calculator.ldaTowardsObs(runway);

    assertEquals(3246, runway.getLda(),
        "Calculated LDA of runway is incorrect after calculation.");
  }

  @Test
  @DisplayName("Test for calculated LDA over obstacle.")
  public void ldaOver() {
    Runway runway = new Runway("27R", 3884, 3962, 3884, 3884, 240, 0,
        new Aircraft("A380", 300));
    runway.setCurrentObs(new Obstacle("Obstacle", 20,50));

    Calculator.ldaOverObs(runway);

    assertEquals(2774, runway.getLda(),
        "Calculated LDA of runway is incorrect after calculation.");
  }
}
