package uk.ac.soton.comp2211.team33;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.team33.models.Airport;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The AirportTest test class to test multiple models in an airport.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
@DisplayName("Test for different models in an airport.")
public class AirportTest {

  private static Airport state;

  @BeforeAll
  @DisplayName("Setting up the objects required for runway re-declaration.")
  public static void setupObjects() {
    state = new Airport("London", "Heathrow Airport (LHR)");
  }

  @Test
  @DisplayName("Test for loading pre-defined obstacles in an airport.")
  public void loadPredefinedObstacles() {
    state.loadPredefinedObstacles();

    assertEquals("Tree", state.obstacleListProperty().get(0).getName(),
        "Either application state is reading the wrong file, or file has the wrong contents.");
    assertEquals(25, state.obstacleListProperty().get(1).getHeight(),
        "Either application state is reading the wrong file, or file has the wrong contents.");
    assertEquals(60, state.obstacleListProperty().get(2).getCenterline(),
        "Either application state is reading the wrong file, or file has the wrong contents.");
    assertEquals("Aeroplane", state.obstacleListProperty().get(3).getName(),
        "Either application state is reading the wrong file, or file has the wrong contents.");
    assertEquals(-100, state.obstacleListProperty().get(4).getCenterline(),
        "Either application state is reading the wrong file, or file has the wrong contents.");

  }

  @Test
  @DisplayName("Test for limited range of blast protection values when adding an aircraft to an airport.")
  public void addProhibitedAircraft() {
    assertThrows(IllegalArgumentException.class, () -> state.addAircraft("A380", 250));
    assertThrows(IllegalArgumentException.class, () -> state.addAircraft("A380", 550));
  }
}
