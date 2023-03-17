package uk.ac.soton.comp2211.team33.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.max;

import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;

/**
 * The Calculator static class is a utility class that handles the main calculation involved in a runway re-declaration.
 * Corresponds to user story #3, #5, #6.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public final class Calculator {

  private static final Logger logger = LogManager.getLogger(Calculator.class);

  /**
   * Private constructor to avoid instancing.
   */
  private Calculator() {}

  /**
   * Reset calculations when no obstacle or aircraft is present.
   *
   * @param runway the runway to reset values
   */
  public static String resetCalculationsPP(Runway runway) {
    runway.setCtora(runway.getTora());
    runway.setCtoda(runway.getToda());
    runway.setCasda(runway.getAsda());
    runway.setClda(runway.getLda());

    return "Runway values do not need to be re-declared.\n\nSelect an obstacle and an aircraft to re-declare values.";
  }

  /**
   * Function that calculates the take-off runway available.
   * Updates runway with new values.
   * <p>
   * TORA for Take Off Away.
   * TORA = ASDA = TODA in this case.
   *
   * @param runway the runway to perform the calculation on
   * @param obstacle the obstacle to perform the calculation on
   * @return A string detailing the steps of the calculation performed
   */
  public static String takeOffTowardsObsPP(Runway runway, Obstacle obstacle) {
    var calcSummary = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No re-declaration needed...");
      return "Runway values do not need to be re-declared.\n\nCurrent obstacle does not require runway re-declaration.";
    }

    logger.info("Re-declaring TORA, TODA, and ASDA for take-off towards obstacle...");

    calcSummary.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcSummary.append("Take-off towards obstacle: \n");

    calcSummary.append("\n");

    calcSummary.append("RESA = " + runway.getResa() + "\n");

    double slope = obstacle.getHeight() * runway.getTocs();
    calcSummary.append("Slope = " + obstacle.getHeight() + " * " + runway.getTocs() + " = " + slope + "\n");

    calcSummary.append("\n");

    double temp;
    String tempName;
    if (runway.getResa() >= slope) {
      calcSummary.append("RESA >= Slope, using RESA value: " + runway.getResa() + "\n");
      temp = runway.getResa();
      tempName = "RESA";
    } else {
      calcSummary.append("Slope > RESA, using slope value: " + slope + "\n");
      temp = slope;
      tempName = "Slope";
    }

    calcSummary.append("\n");

    double newTora = runway.getObsDistFromThresh() + runway.getThreshold() - temp - runway.getStripEnd();
    calcSummary.append("TORA = Obstacle Distance from Threshold + Displaced Threshold - " + tempName + " - Strip End\n");
    calcSummary.append("TORA = " + runway.getObsDistFromThresh() + " + " + runway.getThreshold() + " - " + temp + " - " + runway.getStripEnd() + "\n");
    calcSummary.append("TORA = " + newTora + "\n");

    calcSummary.append("\n");

    calcSummary.append("TODA = TORA = " + newTora + "\n");

    calcSummary.append("\n");

    calcSummary.append("ASDA = TORA = " + newTora + "\n");

    // updating runway with new values
    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);

    return calcSummary.toString();
  }

  /**
   * Calculate the TORA for take-off away from the obstacle present.
   * For ASDA and TODA, if there exists any clearway and/or stopway then those values should be added to the reduced
   * TORA for the TODA and ASDA values.
   *
   * @param  runway the runway being calculated
   * @return string detailing the calculations
   */
  public static String takeOffAwayObsPP(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    StringBuilder calcSummary = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No re-declaration needed...");
      return "Runway values do not need to be re-declared.\n\nCurrent obstacle does not require runway re-declaration.";
    }

    logger.info("Re-declaring TORA, TODA and ASDA for take-off away from obstacle...");

    calcSummary.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcSummary.append("Take-off away from obstacle: \n");

    calcSummary.append("\n");

    double stripEndPlusResa = runway.getStripEnd() + runway.getResa();
    double blastProtection = max(stripEndPlusResa, aircraft.getBlastProtection());

    if (stripEndPlusResa < aircraft.getBlastProtection()) {
      calcSummary.append("TORA = Original TORA - Blast Protection - Distance from Threshold\n");
    } else {
      calcSummary.append("TORA = Original TORA - Strip End - RESA - Distance from Threshold\n");
    }

    double newTora = runway.getTora() - blastProtection - runway.getObsDistFromThresh() - runway.getThreshold();

    calcSummary.append("TORA = " + runway.getTora() + " - " + blastProtection + " - " + runway.getObsDistFromThresh() + " - " + runway.getThreshold() + "\n");
    calcSummary.append("TORA = " + newTora + "\n");

    calcSummary.append("\n");

    calcSummary.append("TODA = TORA + CLEARWAY\n");
    calcSummary.append("TODA = " + newTora + " + " + runway.getClearway() + "\n");
    calcSummary.append("TODA = " + (newTora + runway.getClearway()) + "\n");

    calcSummary.append("\n");

    calcSummary.append("ASDA = TORA + STOPWAY\n");
    calcSummary.append("ASDA = " + newTora + " + " + runway.getStopway() + "\n");
    calcSummary.append("ASDA = " + (newTora + runway.getStopway()) + "\n");

    runway.setCtora(newTora);
    runway.setCtoda(newTora + runway.getClearway());
    runway.setCasda(newTora + runway.getStopway());

    return calcSummary.toString();
  }

  /*

  public static String ldaOver(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    StringBuilder calcs = new StringBuilder();

    //In case of no re-declaration
    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No need for re-declaration...");
      calcs.append("LDA  = " + runway.getLda());
      return calcs.toString();
    }

    logger.info("Re-declaring LDA for landing over obstacle...");
    double slope = obstacle.getHeight() * runway.getAls();
    double newLda;

    //Will use blast protection if it is larger than the slope calculation
    if (slope < aircraft.getBlastProtection()) {
      calcs.append("LDA  = Original LDA - Blast Protection- Distance from Threshold - Strip End");
      calcs.append("\n     = " + runway.getLda() + " - " + aircraft.getBlastProtection()
        + " - " + runway.getObsDistFromThresh() + " - " + runway.getStripEnd());
      newLda = runway.getLda() - aircraft.getBlastProtection()
        - runway.getObsDistFromThresh() - runway.getStripEnd();
    } else {
      calcs.append("LDA  = Original LDA - Slope Calculation - Distance from Threshold - Strip End");
      calcs.append("\n     = " + runway.getLda() + " - " + obstacle.getHeight()
        + "*" + runway.getAls() + " - " + runway.getObsDistFromThresh() + " - " + runway.getStripEnd());
      newLda = runway.getLda() - slope - runway.getObsDistFromThresh() - runway.getStripEnd();
    }

    boolean changeLDA = makeNewRESA(newLda, runway, obstacle, aircraft);

    if (changeLDA) {
      calcs.setLength(0);
      calcs.append("LDA  = Original LDA - Distance from Threshold - New RESA");
      calcs.append("\n     = " + runway.getLda() + " - " + runway.getObsDistFromThresh() + " - " + runway.getResa());
      newLda = runway.getLda() - runway.getObsDistFromThresh() - runway.getResa();
    }

    runway.setClda(newLda);
    calcs.append("\n     = " + runway.getClda());

    return calcs.toString();
  }
  */

  /**
   * Function that re-declares the LDA after an obstacle appears, and pretty prints the calculations. Updates runway
   * object with new values.
   * When landing towards, only the LDA has to change.
   *
   * @param runway   the runway to perform calculations on
   * @param obstacle the obstacle to base calculations on
   * @return A string containing a summary of the calculations performed
   */
  public static String landingTowardsObsPP(Runway runway, Obstacle obstacle) {
    StringBuilder calcSummary = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No re-declaration needed...");
      return "Runway values do not need to be re-declared.\n\nCurrent obstacle does not require runway re-declaration.";
    }

    logger.info("Re-declaring LDA for landing towards obstacle...");

    calcSummary.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcSummary.append("Landing towards obstacle: \n");

    calcSummary.append("\n");

    double newLda = runway.getObsDistFromThresh() - runway.getStripEnd() - runway.getResa();
    calcSummary.append("LDA = Obstacle distance from Threshold - Strip End - RESA\n");
    calcSummary.append("LDA = " + runway.getObsDistFromThresh() + " - " + runway.getStripEnd() + " - " + runway.getResa() + "\n");

    calcSummary.append("LDA = " + newLda + "\n");

    runway.setClda(newLda);

    return calcSummary.toString();
  }

  /**
   * Pretty prints the calculations for LDA over obstacle. Updates the runway object with the new calculated values.
   *
   * @param runway the runway to perform calculations on
   * @param obstacle the obstacle to base calculations on
   * @param aircraft the aircraft to base calculations on
   * @return a string containing the calculations performed
   */
  public static String landingOverObsPP(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    StringBuilder calcSummary = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No re-declaration needed...");
      return "Runway values do not need to be re-declared.\n\nCurrent obstacle does not require runway re-declaration.";
    }

    logger.info("Re-declaring LDA for landing over obstacle...");

    calcSummary.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcSummary.append("Landing over obstacle: \n");

    calcSummary.append("\n");

    double slope = obstacle.getHeight() * runway.getAls();
    calcSummary.append("Slope = Obstacle Height * Runway Slope\n");
    calcSummary.append("Slope = " + obstacle.getHeight() + " * " + runway.getAls() + "\n");
    calcSummary.append("Slope = " + slope + "\n");

    calcSummary.append("\n");

    double tempVal;
    if (slope < aircraft.getBlastProtection()) {
      tempVal = aircraft.getBlastProtection();
      calcSummary.append("Slope < Blast Protection\n");
      calcSummary.append("LDA = Original LDA - Blast Protection - Distance from Threshold - Strip End\n");
    } else {
      tempVal = slope;
      calcSummary.append("Slope > Blast Protection\n");
      calcSummary.append("LDA = Original LDA - Slope Calculation - Distance from Threshold - Strip End\n");
    }

    calcSummary.append("\n");

    double newLda = runway.getLda() - tempVal - runway.getObsDistFromThresh() - runway.getStripEnd();
    calcSummary.append("LDA = " + runway.getLda() + " - " + tempVal + " - " + runway.getObsDistFromThresh() + " - " + runway.getStripEnd() + "\n");
    calcSummary.append("LDA = " + newLda + "\n");

    boolean changeLDA = makeNewRESA(newLda, runway, obstacle, aircraft);

    if (changeLDA) {
      calcSummary.setLength(0);
      calcSummary.append("Declaring new RESA due to Blast Protection\n");
      calcSummary.append("LDA = Original LDA - Distance from Threshold - New RESA\n");
      newLda = runway.getLda() - runway.getObsDistFromThresh() - runway.getResa();
    }

    calcSummary.append("LDA = " + newLda + "\n");

    runway.setClda(newLda);

    return calcSummary.toString();
  }

  /**
   * Judge if a new RESA has to be re-declared, if yes, then re-declare according to formula.
   *
   * @param newLda   the new LDA after re-declaration
   * @param runway   the runway
   * @param obstacle the obstacle
   * @param aircraft the aircraft
   */
  private static boolean makeNewRESA(double newLda, Runway runway, Obstacle obstacle, Aircraft aircraft) {
    if (newLda < aircraft.getBlastProtection()) {
      logger.info("Declaring a new RESA due to blast protection...");
      double newResa = aircraft.getBlastProtection() + runway.getObsDistFromThresh();
      logger.info("New RESA is: " + newResa);
      runway.setCresa(newResa);
      return true;
    } else {
      return false;
    }
  }
}