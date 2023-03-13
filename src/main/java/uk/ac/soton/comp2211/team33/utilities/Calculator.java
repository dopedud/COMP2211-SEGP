package uk.ac.soton.comp2211.team33.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.max;

import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;

/**
 * The static class Calculator is a utility class that handles the main calculation involved in a runway re-declaration.
 * <p>
 * Corresponds to user story #3, #5, #6.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public final class Calculator {

  private static final Logger logger = LogManager.getLogger(Calculator.class);

  /**
   * Private constructor to avoid instantiating.
   */
  private Calculator() {}

  /**
   * Reset calculations when no obstacle or aircraft is present.
   *
   * @param runway the runway to reset values
   */
  public static void resetCalculations(Runway runway) {
    runway.setCtora(runway.getTora());
    runway.setCtoda(runway.getToda());
    runway.setCasda(runway.getAsda());
    runway.setClda(runway.getLda());
  }

  /**
   * Pretty print output.
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
   * Calculate the take-off runway available (use toraTowardsObsPrettyPrint() for pretty print).
   * Updates runway with new values.
   *
   * @param runway   the runway to perform the calculation on
   * @param obstacle the obstacle to perform the calculation on
   */
  public static void toraTowardsObs(Runway runway, Obstacle obstacle) {
    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
      logger.info("No need for re-declaration...");
    } else {
      logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");

      //Gets the larger between RESA and the slope calculation
      double temp = max(runway.getResa(), obstacle.getHeight() * runway.getTocs());

      double newTora = runway.getObstacleDistance() + runway.getThreshold() - temp - runway.getStripEnd();
      runway.setCtora(newTora);
      runway.setCasda(newTora);
      runway.setCtoda(newTora);
    }
  }

  /**
   * Function that calculates the take-off runway available, and pretty prints the output.
   * Updates runway with new values.
   * <p>
   * TORA for Take Off Away.
   * TORA = ASDA = TODA in this case.
   *
   * @param runway   the runway to perform the calculation on
   * @param obstacle the obstacle to perform the calculation on
   * @return A string detailing the steps of the calculation performed
   */
  public static String toraTowardsObsPP(Runway runway, Obstacle obstacle) {
    StringBuilder calcSummary = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
      logger.info("No re-declaration needed...");
      calcSummary.append("\nTORA = " + runway.getTora());
      calcSummary.append("\nTODA = " + runway.getToda());
      calcSummary.append("\nASDA = " + runway.getAsda());
      return calcSummary.toString();
    }

    logger.info("Re-declaring TORA, TODA and ASDA for take-offs towards obstacle...");
    calcSummary.append("Summary of calculations for runway " + runway.getDesignator() + "\n");
    calcSummary.append("Takeoff towards obstacle: \n");

    calcSummary.append("RESA = " + runway.getResa() + "\n");

    double slope = obstacle.getHeight() * runway.getTocs();
    calcSummary.append("Slope = " + obstacle.getHeight() + " * " + runway.getTocs() + " = " + slope + "\n");

    double temp;
    String tempName;
    if (runway.getResa() >= slope) {
      calcSummary.append("RESA >= Slope, using RESA value: " + runway.getResa() + "\n");
      temp = runway.getResa();
      tempName = "RESA";
    } else {
      calcSummary.append("Slope > RESA, using Slope value: " + slope + "\n");
      temp = slope;
      tempName = "Slope";
    }

    double newTora = runway.getObstacleDistance() + runway.getThreshold() - temp - runway.getStripEnd();
    calcSummary.append("TORA = Obstacle Distance from Threshold + Displaced Threshold - " + tempName + " - Strip End\n");
    calcSummary.append("TORA = " + runway.getObstacleDistance() + " + " + runway.getThreshold() + " - " + temp + " - " + runway.getStripEnd() + "\n");
    calcSummary.append("TORA = " + newTora + "\n");
    calcSummary.append("TODA = TORA = " + newTora + "\n");
    calcSummary.append("ASDA = TORA = " + newTora + "\n");


    // updating runway with new values
    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);

    return calcSummary.toString();
  }

  /**
   * No pretty printing TORA away from obstacle.
   *
   * @param runway the runway being calculated
   */
  public static void toraAwayObs(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
      logger.info("No need for re-declaration...");
    } else {
      logger.info("Re-declaring TORA, TODA and ASDA for take-off away from obstacle...");

      double blastProtection = max((runway.getStripEnd() + runway.getResa()), aircraft.getBlastProtection());

      double newTora = runway.getTora() - blastProtection - runway.getObstacleDistance() - runway.getThreshold();

      runway.setCtora(newTora);
      runway.setCtoda(newTora + runway.getClearway());
      runway.setCasda(newTora + runway.getStopway());
    }
  }

  /**
   * Calculate the TORA for take-off away from the obstacle present.
   * For ASDA and TODA, if there exists any clearway and/or stopway then those values should be added to the reduced
   * TORA for the TODA and ASDA values.
   *
   * @param runway the runway being calculated
   * @return string detailing the calculations
   */
  public static String toraAwayObsPP(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    StringBuilder calcSummary = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
      logger.info("No re-declaration needed...");
      calcSummary.append("\nTORA = " + runway.getTora());
      calcSummary.append("\nTODA = " + runway.getToda());
      calcSummary.append("\nASDA = " + runway.getAsda());
      return calcSummary.toString();
    }

    logger.info("Re-declaring TORA, TODA and ASDA for take-off away from obstacle...");
    calcSummary.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcSummary.append("Takeoff away from obstacle: \n");

    double stripEndPlusResa = runway.getStripEnd() + runway.getResa();
    double blastProtection = max(stripEndPlusResa, aircraft.getBlastProtection());

    if (stripEndPlusResa < aircraft.getBlastProtection()) {
      calcSummary.append("TORA = Original TORA - Blast Protection - Distance from Threshold\n");
    } else {
      calcSummary.append("TORA = Original TORA - Strip End - RESA - Distance from Threshold\n");
    }

    double newTora = runway.getTora() - blastProtection - runway.getObstacleDistance() - runway.getThreshold();

    calcSummary.append("TORA = " + runway.getTora() + " - " + blastProtection + " - " + runway.getObstacleDistance() + " - " + runway.getThreshold() + "\n");
    calcSummary.append("TORA = " + newTora + "\n");
    calcSummary.append("ASDA = TORA + STOPWAY\n");
    calcSummary.append("ASDA = " + newTora + " + " + runway.getStopway() + "\n");
    calcSummary.append("ASDA = " + (newTora + runway.getStopway()) + "\n");
    calcSummary.append("TODA = TORA + CLEARWAY\n");
    calcSummary.append("TODA = " + newTora + " + " + runway.getClearway() + "\n");
    calcSummary.append("TODA = " + (newTora + runway.getClearway()) + "\n");

    runway.setCtora(newTora);
    runway.setCtoda(newTora + runway.getClearway());
    runway.setCasda(newTora + runway.getStopway());

    return calcSummary.toString();

  }


  /*

  public static String ldaOver(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    StringBuilder calcs = new StringBuilder();

    //In case of no re-declaration
    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
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
        + " - " + runway.getObstacleDistance() + " - " + runway.getStripEnd());
      newLda = runway.getLda() - aircraft.getBlastProtection()
        - runway.getObstacleDistance() - runway.getStripEnd();
    } else {
      calcs.append("LDA  = Original LDA - Slope Calculation - Distance from Threshold - Strip End");
      calcs.append("\n     = " + runway.getLda() + " - " + obstacle.getHeight()
        + "*" + runway.getAls() + " - " + runway.getObstacleDistance() + " - " + runway.getStripEnd());
      newLda = runway.getLda() - slope - runway.getObstacleDistance() - runway.getStripEnd();
    }

    boolean changeLDA = makeNewRESA(newLda, runway, obstacle, aircraft);

    if (changeLDA) {
      calcs.setLength(0);
      calcs.append("LDA  = Original LDA - Distance from Threshold - New RESA");
      calcs.append("\n     = " + runway.getLda() + " - " + runway.getObstacleDistance() + " - " + runway.getResa());
      newLda = runway.getLda() - runway.getObstacleDistance() - runway.getResa();
    }

    runway.setClda(newLda);
    calcs.append("\n     = " + runway.getClda());

    return calcs.toString();
  }
  */


  /**
   * No pretty printing LDA over obstacle.
   *
   * @param runway the runway being calculated
   */
  public static void ldaOverObs(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
      logger.info("No need for re-declaration...");
    } else {
      logger.info("Re-declaring LDA for landing over obstacle...");

      double newLda;
      double tempVal = max(aircraft.getBlastProtection(), obstacle.getHeight() * runway.getAls());

      newLda = runway.getLda() - tempVal - runway.getObstacleDistance() - runway.getStripEnd();

      boolean changeLDA = makeNewRESA(newLda, runway, obstacle, aircraft);

      if (changeLDA) {
        newLda = runway.getLda() - runway.getObstacleDistance() - runway.getResa();
      }

      runway.setClda(newLda);
    }
  }


  /**
   * Pretty prints the calculations for LDA over obstacle. Updates the runway object with the new calculated values.
   *
   * @param runway   the runway to perform calculations on
   * @param obstacle the obstacle to base calculations on
   * @param aircraft the aircraft to base calculations on
   * @return a string containing the calculations performed
   */
  public static String ldaOverObsPP(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    StringBuilder calcSummary = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
      logger.info("No need for re-declaration...");
      calcSummary.append("LDA  = " + runway.getLda());
      return calcSummary.toString();
    }

    logger.info("Re-declaring LDA for landing over obstacle...");
    calcSummary.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcSummary.append("Landing over obstacle: \n");

    double slope = obstacle.getHeight() * runway.getAls();
    double tempVal = max(aircraft.getBlastProtection(), slope);

    calcSummary.append("Slope = Obstacle Height * Runway Slope\n");
    calcSummary.append("Slope = " + obstacle.getHeight() + " * " + runway.getAls() + "\n");
    calcSummary.append("Slope = " + slope + "\n");

    if (slope < aircraft.getBlastProtection()) {
      calcSummary.append("Slope < Blast Protection\n");
      calcSummary.append("LDA = Original LDA - Blast Protection - Distance from Threshold - Strip End\n");
    } else {
      calcSummary.append("Slope > Blast Protection\n");
      calcSummary.append("LDA = Original LDA - Slope Calculation - Distance from Threshold - Strip End\n");
    }

    double newLda = runway.getLda() - tempVal - runway.getObstacleDistance() - runway.getStripEnd();
    calcSummary.append("LDA = " + runway.getLda() + " - " + tempVal + " - " + runway.getObstacleDistance() + " - " + runway.getStripEnd() + "\n");
    calcSummary.append("LDA = " + newLda + "\n");

    boolean changeLDA = makeNewRESA(newLda, runway, obstacle, aircraft);

    if (changeLDA) {
      calcSummary.setLength(0);
      calcSummary.append("Declaring new RESA due to Blast Protection\n");
      calcSummary.append("LDA = Original LDA - Distance from Threshold - New RESA\n");
      newLda = runway.getLda() - runway.getObstacleDistance() - runway.getResa();
    }

    runway.setClda(newLda);
    calcSummary.append("LDA = " + runway.getClda() + "\n");

    return calcSummary.toString();
  }

  /**
   * No pretty printing LDA towards obstacle.
   *
   * @param runway the runway being calculated
   */
  public static void ldaTowardsObs(Runway runway, Obstacle obstacle) {
    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
      logger.info("No need for re-declaration...");
    } else {
      logger.info("Re-declaring LDA for landing towards obstacle...");
      double newLda = runway.getObstacleDistance() - runway.getStripEnd() - runway.getResa();
      runway.setClda(newLda);
    }
  }


  /**
   * Function that re-declares the LDA after an obstacle appears, and pretty prints the calculations. Updates runway object with new values.
   * When landing towards, only the LDA has to change.
   *
   * @param runway   the runway to perform calculations on
   * @param obstacle the obstacle to base calculations on
   * @return A string containing a summary of the calculations performed
   */
  public static String ldaTowardsObsPP(Runway runway, Obstacle obstacle) {
    StringBuilder calcSummary = new StringBuilder();

    if (!(Math.abs(obstacle.getCenterline()) < 75 && runway.getObstacleDistance() > -60)) {
      logger.info("No need for re-declaration...");
      calcSummary.append("LDA  = " + runway.getLda());
      return calcSummary.toString();
    }

    logger.info("Re-declaring LDA for landing towards obstacle...");
    calcSummary.append("Summary of calculations for runway: " + runway.getDesignator() + "\n");
    calcSummary.append("Landing towards obstacle: \n");

    calcSummary.append("LDA = Obstacle distance from Threshold - Strip End - RESA\n");
    calcSummary.append("LDA = " + runway.getObstacleDistance() + " - " + runway.getStripEnd() + " - " + runway.getResa() + "\n");

    double newLda = runway.getObstacleDistance() - runway.getStripEnd() - runway.getResa();
    runway.setClda(newLda);
    calcSummary.append("LDA = " + runway.getClda() + "\n");

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
      double newResa = aircraft.getBlastProtection() + runway.getObstacleDistance();
      logger.info("New RESA is: " + newResa);
      runway.setCresa(newResa);
      return true;
    } else {
      return false;
    }
  }
}
