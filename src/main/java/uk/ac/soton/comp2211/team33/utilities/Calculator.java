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
   * Reset runway values to its original values when no obstacle or aircraft is present.
   *
   * @param runway the runway to reset values
   */
  public static String resetCalculationsPP(Runway runway) {
    runway.setCtora(runway.getTora());
    runway.setCtoda(runway.getToda());
    runway.setCasda(runway.getAsda());
    runway.setClda(runway.getLda());

    if (runway.getCurrentObstacle() == null && runway.getCurrentAircraft() == null) return "Runway values do not need to be re-declared.\n\nSelect an obstacle and an aircraft to re-declare values.";
    else if (runway.getCurrentAircraft() == null) return "Runway values do not need to be re-declared.\n\nSelect an aircraft to re-declare values.";
    else return "Runway values do not need to be re-declared.\n\nSelect an obstacle to re-declare values.";
  }

  /**
   * Method that calculates runway values when taking-off towards an obstacle.
   * Updates runway with new values.
   * TORA for Take Off Away.
   * TORA = ASDA = TODA in this case.
   *
   * @param runway the runway to calculate
   * @param obstacle the obstacle to calculate
   * @return breakdown of calculations
   */
  public static String takeOffTowardsObsPP(Runway runway, Obstacle obstacle) {
    var calcBreakdown = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No re-declaration needed...");
      return "Runway values do not need to be re-declared.\n\nCurrent obstacle does not require runway re-declaration.";
    }

    logger.info("Re-declaring TORA, TODA, and ASDA for take-off towards obstacle...");

    calcBreakdown.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcBreakdown.append("Take-off towards obstacle: \n");

    calcBreakdown.append("\n");

    calcBreakdown.append("RESA = " + runway.getResa() + "\n");

    double slope = obstacle.getHeight() * runway.getTocs();
    calcBreakdown.append("Slope = " + obstacle.getHeight() + " * " + runway.getTocs() + " = " + slope + "\n");

    calcBreakdown.append("\n");

    double temp;
    String tempName;
    if (runway.getResa() >= slope) {
      calcBreakdown.append("RESA >= Slope, using RESA value: " + runway.getResa() + "\n");
      temp = runway.getResa();
      tempName = "RESA";
    } else {
      calcBreakdown.append("Slope > RESA, using Slope value: " + slope + "\n");
      temp = slope;
      tempName = "Slope";
    }

    calcBreakdown.append("\n");

    double newTora = runway.getObsDistFromThresh() + runway.getThreshold() - temp - runway.getStripEnd();
    calcBreakdown.append("TORA = Obstacle Distance from Threshold + Displaced Threshold - " + tempName + " - Strip End\n");
    calcBreakdown.append("TORA = " + runway.getObsDistFromThresh() + " + " + runway.getThreshold() + " - " + temp + " - " + runway.getStripEnd() + "\n");
    calcBreakdown.append("TORA = " + newTora + "\n");

    calcBreakdown.append("\n");

    calcBreakdown.append("TODA = TORA = " + newTora + "\n");

    calcBreakdown.append("\n");

    calcBreakdown.append("ASDA = TORA = " + newTora);

    // Update runway with new values
    runway.setCtora(newTora);
    runway.setCtoda(newTora);
    runway.setCasda(newTora);

    if (newTora <= 0) calcBreakdown = new StringBuilder("Runway values do not need to be re-declared.\n\nTORA is negative.");

    return calcBreakdown.toString();
  }

  /**
   * Method that calculates runway values when taking-off away from an obstacle.
   * For ASDA and TODA, if there exists any clearway and/or stopway then those values should be added to the reduced
   * TORA for the TODA and ASDA values.
   *
   * @param runway the runway to calculate
   * @param obstacle the obstacle to calculate
   * @param aircraft the aircraft to calculate
   * @return breakdown of calculations
   */
  public static String takeOffAwayObsPP(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    StringBuilder calcBreakdown = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No re-declaration needed...");
      return "Runway values do not need to be re-declared.\n\nCurrent obstacle does not require runway re-declaration.";
    }

    logger.info("Re-declaring TORA, TODA and ASDA for take-off away from obstacle...");

    calcBreakdown.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcBreakdown.append("Take-off away from obstacle: \n");

    calcBreakdown.append("\n");

    double stripEndPlusResa = runway.getStripEnd() + runway.getResa();
    double blastProtection = max(stripEndPlusResa, aircraft.getBlastProtection());

    if (stripEndPlusResa < aircraft.getBlastProtection()) calcBreakdown.append("TORA = Original TORA - Blast Protection - Distance from Threshold\n");
    else calcBreakdown.append("TORA = Original TORA - Strip End - RESA - Distance from Threshold\n");

    double newTora = runway.getTora() - blastProtection - runway.getObsDistFromThresh() - runway.getThreshold();

    calcBreakdown.append("TORA = " + runway.getTora() + " - " + blastProtection + " - " + runway.getObsDistFromThresh() + " - " + runway.getThreshold() + "\n");
    calcBreakdown.append("TORA = " + newTora + "\n");

    calcBreakdown.append("\n");

    calcBreakdown.append("TODA = TORA + CLEARWAY\n");
    calcBreakdown.append("TODA = " + newTora + " + " + runway.getClearway() + "\n");
    calcBreakdown.append("TODA = " + (newTora + runway.getClearway()) + "\n");

    calcBreakdown.append("\n");

    calcBreakdown.append("ASDA = TORA + STOPWAY\n");
    calcBreakdown.append("ASDA = " + newTora + " + " + runway.getStopway() + "\n");
    calcBreakdown.append("ASDA = " + (newTora + runway.getStopway()));

    // Update runway with new values
    runway.setCtora(newTora);
    runway.setCtoda(newTora + runway.getClearway());
    runway.setCasda(newTora + runway.getStopway());

    if (newTora <= 0) calcBreakdown = new StringBuilder("Runway values do not need to be re-declared.\n\nTORA is negative.");

    return calcBreakdown.toString();
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
   * Method that calculates runway values when landing towards an obstacle.
   * When landing towards, only LDA has to change.
   *
   * @param runway the runway to calculate
   * @param obstacle the obstacle to calculate
   * @return breakdown of calculations
   */
  public static String landingTowardsObsPP(Runway runway, Obstacle obstacle) {
    StringBuilder calcBreakdown = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No re-declaration needed...");
      return "";
    }

    logger.info("Re-declaring LDA for landing towards obstacle...");

    calcBreakdown.append("Landing towards obstacle: \n");

    calcBreakdown.append("\n");

    double newLda = runway.getObsDistFromThresh() - runway.getStripEnd() - runway.getResa();
    calcBreakdown.append("LDA = Obstacle distance from Threshold - Strip End - RESA\n");
    calcBreakdown.append("LDA = " + runway.getObsDistFromThresh() + " - " + runway.getStripEnd() + " - " + runway.getResa() + "\n");
    calcBreakdown.append("LDA = " + newLda);

    // Update runway with new values
    runway.setClda(newLda);

    if (newLda <= 0) calcBreakdown = new StringBuilder("Runway values do not need to be re-declared.\n\nLDA is negative.");

    return calcBreakdown.toString();
  }

  /**
   * Method that calculates runway values when landing over an obstacle.
   * When landing over, only LDA has to change.
   *
   * @param runway the runway to calculate
   * @param obstacle the obstacle to calculate
   * @param aircraft the aircraft to calculate
   * @return breakdown of calculations
   */
  public static String landingOverObsPP(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    StringBuilder calcBreakdown = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObsDistFromThresh() > -60)) {
      logger.info("No re-declaration needed...");
      return "";
    }

    logger.info("Re-declaring LDA for landing over obstacle...");

    calcBreakdown.append("Landing over obstacle: \n");

    calcBreakdown.append("\n");

    double slope = obstacle.getHeight() * runway.getAls();
    calcBreakdown.append("Slope = Obstacle Height * Runway Slope\n");
    calcBreakdown.append("Slope = " + obstacle.getHeight() + " * " + runway.getAls() + "\n");
    calcBreakdown.append("Slope = " + slope + "\n");

    calcBreakdown.append("\n");

    double temp;
    if (slope < aircraft.getBlastProtection()) {
      temp = aircraft.getBlastProtection();
      calcBreakdown.append("Slope < Blast Protection\n");
      calcBreakdown.append("LDA = Original LDA - Blast Protection - Distance from Threshold - Strip End\n");
    } else {
      temp = slope;
      calcBreakdown.append("Slope > Blast Protection\n");
      calcBreakdown.append("LDA = Original LDA - Slope Calculation - Distance from Threshold - Strip End\n");
    }

    calcBreakdown.append("\n");

    double newLda = runway.getLda() - temp - runway.getObsDistFromThresh() - runway.getStripEnd();
    calcBreakdown.append("LDA = " + runway.getLda() + " - " + temp + " - " + runway.getObsDistFromThresh() + " - " + runway.getStripEnd() + "\n");
    calcBreakdown.append("LDA = " + newLda + "\n");

    if (makeNewRESA(newLda, runway, aircraft)) {
      calcBreakdown.setLength(0);
      calcBreakdown.append("Declaring new RESA due to Blast Protection\n");
      calcBreakdown.append("LDA = Original LDA - Distance from Threshold - New RESA\n");
      newLda = runway.getLda() - runway.getObsDistFromThresh() - runway.getResa();
    }

    calcBreakdown.append("LDA = " + newLda);

    // Update runway with new values
    runway.setClda(newLda);

    if (newLda <= 0) calcBreakdown = new StringBuilder("Runway values do not need to be re-declared.\n\nLDA is negative.");

    return calcBreakdown.toString();
  }

  /**
   * Judge if a new RESA has to be re-declared. If so, then re-declare according to formula.
   *
   * @param newLda the new LDA after re-declaration
   * @param runway the runway
   * @param aircraft the aircraft
   */
  private static boolean makeNewRESA(double newLda, Runway runway, Aircraft aircraft) {
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