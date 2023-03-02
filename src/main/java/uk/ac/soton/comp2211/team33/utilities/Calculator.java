package uk.ac.soton.comp2211.team33.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.entities.Runway;

/**
 * The static class Calculator acts as a utility class that decouples the calculation for runway re-declaration from
 * the Runway class. It is static as utility class often are not made to be inherited nor instantiated.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public final class Calculator {

  private static Logger logger = LogManager.getLogger(Calculator.class);

  /**
   * Private constructor to prevent instantiating
   */
  private Calculator() {}

  /**
   * Method to calculate the values of a given runway for take-off towards the obstacle.
   *
   * @param runway the runway to calculate
   */
  public static void toraTowards(Runway runway) {
    logger.info("Re-declaring TORA, TODA, and ASDA for take-off towards the obstacle...");
    double newTora;
    double tempSlope = (runway.getCurrentObs().getHeight() * 50);

    //Compare with RESA, if RESA is greater, use RESA
    if (tempSlope < runway.getCresa()){
      tempSlope = runway.getCresa();
    }

    if (runway.getCurrentObs().getDistanceThreshold() < 0) {
      newTora = runway.getCurrentObs().getDistanceThreshold() - tempSlope - runway.getStripEnd();
    } else {
      newTora = runway.getCurrentObs().getDistanceThreshold() +
          runway.getThreshold() - tempSlope - runway.getStripEnd();
    }

    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);
    var surfaceClimb = runway.getCurrentObs().getHeight() * runway.getCurrentObs().getHeight() * 50;
    runway.setCals(surfaceClimb);
    runway.setCtocs(surfaceClimb);
  }

  /**
   * Method to calculate the values of a given runway for take-off away from the obstacle.
   * For ASDA and TODA, if there exists any clearway and/or stopway then those values should be added to the reduced
   * TORA for the TODA and ASDA values.
   *
   * @param runway the runway to calculate
   */
  public static void toraAway(Runway runway) {
    logger.info("Re-declaring TORA, TODA, and ASDA for take-off away from the obstacle...");
    double newTora;

    if (runway.getCurrentObs().getDistanceThreshold() < 0) {
      newTora = runway.getTora() - runway.getBlastProtection() -
          runway.getCurrentObs().getDistanceThreshold() - runway.getThreshold();
    } else {
      newTora = runway.getTora() - runway.getStripEnd() - runway.getCresa() -
          runway.getCurrentObs().getDistanceThreshold();
    }

    runway.setCtora(newTora);
    runway.setCasda(newTora + runway.getStopway());
    runway.setCtoda(newTora + runway.getClearway());
    var surfaceClimb = runway.getCurrentObs().getHeight() * runway.getCurrentObs().getHeight() * 50;
    runway.setCals(surfaceClimb);
    runway.setCtocs(surfaceClimb);
  }

  /**
   * Method to calculate the values of a given runway for landing over the obstacle.
   *
   * @param runway    the runway to calculate
   * @return          the new LDA
   */
  public static double ldaOver(Runway runway) {
    logger.info("Re-declaring LDA for landing over the obstacle...");
    var newLda = runway.getLda() - runway.getCurrentObs().getDistanceThreshold() -
        runway.getStripEnd() - (runway.getCurrentObs().getHeight() * 50);
    runway.setClda(newLda);
    return runway.getClda();
  }

  /**
   * Method to calculate the values of a given runway for landing towards the obstacle.
   *
   * @param runway    the runway to calculate
   * @return          the new LDA
   */
  public static double ldaTowards(Runway runway) {
    logger.info("Re-declaring LDA for landing towards the obstacle...");
    var newLda = runway.getCurrentObs().getDistanceThreshold() - runway.getStripEnd() - runway.getCresa();
    runway.setClda(newLda);
    return runway.getClda();
  }
}
