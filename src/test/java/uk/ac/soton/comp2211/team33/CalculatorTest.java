package uk.ac.soton.comp2211.team33;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.utilities.Calculator;
import uk.ac.soton.comp2211.team33.models.Runway;

/**
 * The CalculatorTest test class to test calculation for runway values.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
@DisplayName("Test for runway re-declaration calculation.")
public class CalculatorTest {

  private static Runway runway09L, runway27R, runway09R, runway27L;

  private static Obstacle obstacle12H0N, obstacle25H20S, obstacle15H60N, obstacle20H20N, obstacle20H100S;

  private static Aircraft aircraft;

  @BeforeAll
  @DisplayName("Setting up the objects required for runway re-declaration.")
  public static void setupObjects() {
    runway09L = new Runway("09L", 3902, 3902, 3902, 3595, 240, 306);
    runway27R = new Runway("27R", 3884, 3962, 3884, 3884, 240, 0);
    runway09R = new Runway("09R", 3660, 3660, 3660, 3353, 240, 307);
    runway27L = new Runway("27L", 3660, 3660, 3660, 3660, 240, 0);

    obstacle12H0N = new Obstacle("Obstacle 1", 12, 0, 0);
    obstacle25H20S = new Obstacle("Obstacle 2", 25, 0, -20);
    obstacle15H60N = new Obstacle("Obstacle 3", 15, 0, 60);
    obstacle20H20N = new Obstacle("Obstacle 4", 20, 0, 20);
    obstacle20H100S = new Obstacle("Obstacle 5", 20, 0, -100);

    try {
      aircraft = new Aircraft("A380", 300);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }

  @BeforeEach
  @DisplayName("Reset calculations for runway values.")
  public void resetCalculations() {
    Calculator.resetCalculationsPP(runway09L);
    Calculator.resetCalculationsPP(runway27R);
    Calculator.resetCalculationsPP(runway09R);
    Calculator.resetCalculationsPP(runway27L);
  }

  @Test
  @DisplayName("First test for calculated TORA, TODA, and ASDA towards obstacle.")
  public void takeOffTowardsObs1() {
    runway27R.setObsDistFromThresh(3646);
    Calculator.takeOffTowardsObsPP(runway27R, obstacle12H0N);

    assertEquals(2986, runway27R.getCtora(), "Calculated TORA is incorrect after calculation.");
    assertEquals(2986, runway27R.getCtoda(), "Calculated TODA is incorrect after calculation.");
    assertEquals(2986, runway27R.getCasda(), "Calculated ASDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("First test for calculated TORA, TODA, and ASDA away from obstacle.")
  public void takeOffAwayObs1() {
    runway09L.setObsDistFromThresh(-50);
    Calculator.takeOffAwayObsPP(runway09L, obstacle12H0N, aircraft);

    assertEquals(3346, runway09L.getCtora(), "Calculated TORA is incorrect after calculation.");
    assertEquals(3346, runway09L.getCtoda(), "Calculated TODA is incorrect after calculation.");
    assertEquals(3346, runway09L.getCasda(), "Calculated ASDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("First test for calculated LDA towards obstacle.")
  public void landingTowardsObs1() {
    runway27R.setObsDistFromThresh(3646);
    Calculator.landingTowardsObsPP(runway27R, obstacle12H0N);

    assertEquals(3346, runway27R.getClda(), "Calculated LDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("First test for calculated LDA over obstacle.")
  public void landingOverObs1() {
    runway09L.setObsDistFromThresh(-50);
    Calculator.landingOverObsPP(runway09L, obstacle12H0N, aircraft);

    assertEquals(2985, runway09L.getClda(), "Calculated LDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Second test for calculated TORA, TODA, and ASDA towards obstacle.")
  public void takeOffTowardsObs2() {
    runway09R.setObsDistFromThresh(2853);
    Calculator.takeOffTowardsObsPP(runway09R, obstacle25H20S);

    assertEquals(1850, runway09R.getCtora(), "Calculated TORA is incorrect after calculation.");
    assertEquals(1850, runway09R.getCtoda(), "Calculated TODA is incorrect after calculation.");
    assertEquals(1850, runway09R.getCasda(), "Calculated ASDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Second test for calculated TORA, TODA, and ASDA away from obstacle.")
  public void takeOffAwayObs2() {
    runway27L.setObsDistFromThresh(500);
    Calculator.takeOffAwayObsPP(runway27L, obstacle25H20S, aircraft);

    assertEquals(2860, runway27L.getCtora(), "Calculated TORA is incorrect after calculation.");
    assertEquals(2860, runway27L.getCtoda(), "Calculated TODA is incorrect after calculation.");
    assertEquals(2860, runway27L.getCasda(), "Calculated ASDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Second test for calculated LDA towards obstacle.")
  public void landingTowardsObs2() {
    runway09R.setObsDistFromThresh(2853);
    Calculator.landingTowardsObsPP(runway09R, obstacle25H20S);

    assertEquals(2553, runway09R.getClda(), "Calculated LDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Second test for calculated LDA over obstacle.")
  public void landingOverObs2() {
    runway27L.setObsDistFromThresh(500);
    Calculator.landingOverObsPP(runway27L, obstacle25H20S, aircraft);

    assertEquals(1850, runway27L.getClda(), "Calculated LDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Third test for calculated TORA, TODA, and ASDA towards obstacle.")
  public void takeOffTowardsObs3() {
    runway27L.setObsDistFromThresh(3203);
    Calculator.takeOffTowardsObsPP(runway27L, obstacle15H60N);

    assertEquals(2393, runway27L.getCtora(), "Calculated TORA is incorrect after calculation.");
    assertEquals(2393, runway27L.getCtoda(), "Calculated TODA is incorrect after calculation.");
    assertEquals(2393, runway27L.getCasda(), "Calculated ASDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Third test for calculated TORA, TODA, and ASDA away from obstacle.")
  public void takeOffAwayObs3() {
    runway09R.setObsDistFromThresh(150);
    Calculator.takeOffAwayObsPP(runway09R, obstacle15H60N, aircraft);

    assertEquals(2903, runway09R.getCtora(), "Calculated TORA is incorrect after calculation.");
    assertEquals(2903, runway09R.getCtoda(), "Calculated TODA is incorrect after calculation.");
    assertEquals(2903, runway09R.getCasda(), "Calculated ASDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Third test for calculated LDA towards obstacle.")
  public void landingTowardsObs3() {
    runway27L.setObsDistFromThresh(3203);
    Calculator.landingTowardsObsPP(runway27L, obstacle15H60N);

    assertEquals(2903, runway27L.getClda(), "Calculated LDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Third test for calculated LDA over obstacle.")
  public void landingOverObs3() {
    runway09R.setObsDistFromThresh(150);
    Calculator.landingOverObsPP(runway09R, obstacle15H60N, aircraft);

    assertEquals(2393, runway09R.getClda(), "Calculated LDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Fourth test for calculated TORA, TODA, and ASDA towards obstacle.")
  public void takeOffTowardsObs4() {
    runway09L.setObsDistFromThresh(3546);
    Calculator.takeOffTowardsObsPP(runway09L, obstacle20H20N);

    assertEquals(2792, runway09L.getCtora(), "Calculated TORA is incorrect after calculation.");
    assertEquals(2792, runway09L.getCtoda(), "Calculated TODA is incorrect after calculation.");
    assertEquals(2792, runway09L.getCasda(), "Calculated ASDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Fourth test for calculated TORA, TODA, and ASDA away from obstacle.")
  public void takeOffAwayObs4() {
    runway27R.setObsDistFromThresh(50);
    Calculator.takeOffAwayObsPP(runway27R, obstacle20H20N, aircraft);

    assertEquals(3534, runway27R.getCtora(), "Calculated TORA is incorrect after calculation.");
    assertEquals(3612, runway27R.getCtoda(), "Calculated TODA is incorrect after calculation.");
    assertEquals(3534, runway27R.getCasda(), "Calculated ASDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Fourth test for calculated LDA towards obstacle.")
  public void landingTowardsObs4() {
    runway09L.setObsDistFromThresh(3546);
    Calculator.landingTowardsObsPP(runway09L, obstacle20H20N);

    assertEquals(3246, runway09L.getClda(), "Calculated LDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Fourth test for calculated LDA over obstacle.")
  public void landingOverObs4() {
    runway27R.setObsDistFromThresh(50);
    Calculator.landingOverObsPP(runway27R, obstacle20H20N, aircraft);

    assertEquals(2774, runway27R.getClda(), "Calculated LDA is incorrect after calculation.");
  }

  @Test
  @DisplayName("Test case for when the obstacle exceeds the maximum amount of distance threshold.")
  public void exceedDistThresh() {
    runway27L.setObsDistFromThresh(-100);

    Calculator.takeOffTowardsObsPP(runway27L, obstacle12H0N);
    Calculator.landingTowardsObsPP(runway27L, obstacle12H0N);

    assertEquals(runway27L.getTora(), runway27L.getCtora(), "TORA should not be re-declared.");
    assertEquals(runway27L.getToda(), runway27L.getCtoda(), "TODA should not be re-declared.");
    assertEquals(runway27L.getAsda(), runway27L.getCasda(), "ASDA should not be re-declared.");
    assertEquals(runway27L.getLda(), runway27L.getClda(), "LDA should not be re-declared.");

    Calculator.takeOffAwayObsPP(runway27L, obstacle12H0N, aircraft);
    Calculator.landingOverObsPP(runway27L, obstacle12H0N, aircraft);

    assertEquals(runway27L.getTora(), runway27L.getCtora(), "TORA should not be re-declared.");
    assertEquals(runway27L.getToda(), runway27L.getCtoda(), "TODA should not be re-declared.");
    assertEquals(runway27L.getAsda(), runway27L.getCasda(), "ASDA should not be re-declared.");
    assertEquals(runway27L.getLda(), runway27L.getClda(), "LDA should not be re-declared.");
  }

  @Test
  @DisplayName("Test case for when the obstacle exceeds the maximum amount of center-line.")
  public void exceedCenterline() {
    runway27L.setObsDistFromThresh(0);

    Calculator.takeOffTowardsObsPP(runway27L, obstacle20H100S);
    Calculator.landingTowardsObsPP(runway27L, obstacle20H100S);

    assertEquals(runway27L.getTora(), runway27L.getCtora(), "TORA should not be re-declared.");
    assertEquals(runway27L.getToda(), runway27L.getCtoda(), "TODA should not be re-declared.");
    assertEquals(runway27L.getAsda(), runway27L.getCasda(), "ASDA should not be re-declared.");
    assertEquals(runway27L.getLda(), runway27L.getClda(), "LDA should not be re-declared.");

    Calculator.takeOffAwayObsPP(runway27L, obstacle20H100S, aircraft);
    Calculator.landingOverObsPP(runway27L, obstacle20H100S, aircraft);

    assertEquals(runway27L.getTora(), runway27L.getCtora(), "TORA should not be re-declared.");
    assertEquals(runway27L.getToda(), runway27L.getCtoda(), "TODA should not be re-declared.");
    assertEquals(runway27L.getAsda(), runway27L.getCasda(), "ASDA should not be re-declared.");
    assertEquals(runway27L.getLda(), runway27L.getClda(), "LDA should not be re-declared.");
  }
}
