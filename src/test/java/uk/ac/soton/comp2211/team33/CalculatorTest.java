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
    Runway runway = new Runway("09R", 3660, 3660, 3660, 3353, 240, 307);
    Obstacle obs = new Obstacle("BrokenAirplane", 25, 2853);

    Calculator.toraTowardsObs(runway, obs);

    assertEquals(1850, runway.getCtora(),
        "Calculated TORA of runway is incorrect after calculation.");
    assertEquals(1850, runway.getCtoda(),
        "Calculated TODA of runway is incorrect after calculation.");
    assertEquals(1850, runway.getCasda(),
        "Calculated ASDA of runway is incorrect after calculation.");
  }

  @Test
  @DisplayName("Test for calculated TORA away from obstacle.")
  public void toraAway() {
    Runway runway = new Runway("27L", 3660, 3660, 3660, 3660, 240, 0);
    Aircraft aircraft = new Aircraft("A380", 300);
    Obstacle obs = new Obstacle("BrokenAirplane", 25,500);

    Calculator.toraAwayObs(runway, obs, aircraft);

    assertEquals(2860, runway.getCtora(),
        "Calculated TORA of runway is incorrect after calculation.");
    assertEquals(2860, runway.getCtoda(),
        "Calculated TODA of runway is incorrect after calculation.");
    assertEquals(2860, runway.getCasda(),
        "Calculated ASDA of runway is incorrect after calculation.");
  }

  @Test
  @DisplayName("Test for calculated LDA towards obstacle.")
  public void ldaTowards() {
    Runway runway = new Runway("09R", 3660, 3660, 3660, 3353, 240, 307);
    Obstacle obs = new Obstacle("BrokenAirplane", 25, 2853);

    Calculator.ldaTowardsObs(runway, obs);

    assertEquals(2553, runway.getClda(),
        "Calculated LDA of runway is incorrect after calculation.");
  }

  @Test
  @DisplayName("Test for calculated LDA over obstacle.")
  public void ldaOver() {
    Runway runway = new Runway("27L", 3660, 3660, 3660, 3660, 240, 0);
    Aircraft aircraft = new Aircraft("A380", 300);
    Obstacle obs = new Obstacle("BrokenAirplane", 25,500);

    Calculator.ldaOverObs(runway, obs, aircraft);

    assertEquals(1850, runway.getClda(),
        "Calculated LDA of runway is incorrect after calculation.");
  }
}
