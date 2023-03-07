package uk.ac.soton.comp2211.team33.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.max;

import uk.ac.soton.comp2211.team33.entities.Aircraft;
import uk.ac.soton.comp2211.team33.entities.Obstacle;
import uk.ac.soton.comp2211.team33.entities.Runway;

/**
 * The static class Calculator is a utility class that handles the main calculation involved in a runway re-declaration.
 *
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
   * Calculate the take-off runway available (use toraTowardsObsPrettyPrint() for pretty print). Updates runway with new values.
   *
   * @param runway the runway to perform the calculation on
   * @param obstacle the obstacle to perform the calculation on
   */
  public static void toraTowardsObs(Runway runway, Obstacle obstacle) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");

    //Gets the larger between RESA and the slope calculation
    double temp = max(runway.getResa(), obstacle.getHeight() * runway.getTocs());

    double newTora = obstacle.getDistThresh() + runway.getThreshold() - temp - runway.getStripEnd();
    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);
  }

  /**
   * Function that calculates the take-off runway available, and pretty prints the output. Updates runway with new values.
   * TORA for Take Off Away.
   * TORA = ASDA = TODA in this case.
   *
   * @param runway the runway to perform the calculation on
   * @param obstacle the obstacle to perform the calculation on
   * @return A string detailing the steps of the calculation performed
   */
  public static String toraTowardsObsPrettyPrint(Runway runway, Obstacle obstacle) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-offs towards obstacle...");
    StringBuilder calcSummary = new StringBuilder();

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

    double newTora = obstacle.getDistThresh() + runway.getThreshold() - temp - runway.getStripEnd();
    calcSummary.append("TORA = Obstacle Distance from Threshold + Displaced Threshold - " + tempName + " - Strip End\n");
    calcSummary.append("TORA = " + obstacle.getDistThresh() + " + " + runway.getThreshold() + " - " + temp + " - " + runway.getStripEnd() + "\n");
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
   * Calculate the TORA for take-off away from the obstacle present.
   * For ASDA and TODA, if there exists any clearway and/or stopway then those values should be added to the reduced
   * TORA for the TODA and ASDA values.
   *
   * @param runway the runway being calculated
   * @return the new TORA value
   */
  public static String toraAway(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off away from obstacle...");
    StringBuilder calcs = new StringBuilder();
    calcs.append(runway.getDesignator() + "(Take Off Away, Landing Over): \n");

    double newTora;
    boolean useBlast = false;
    boolean dispThresh = false;
    double tempVal = max((runway.getStripEnd() + runway.getResa()), aircraft.getBlastProtection());

    calcs.append("TORA = Original TORA ");

    //See if the calculation will be using blast protection when it is greater than RESA or TOCS * Height
    if ((runway.getStripEnd() + runway.getResa()) < aircraft.getBlastProtection()) {
      calcs.append("- Blast Protection - Distance from Threshold");
      useBlast = true;
    } else {
      calcs.append("- Strip End - RESA - Distance from Threshold");
    }

    if (runway.getThreshold() != 0) {
      calcs.append(" - Displaced Threshold");
      dispThresh = true;
    }

    //If it uses blast protection instead of RESA+StripEnd
    if (useBlast) {
      calcs.append("\n     = " + runway.getTora() + " - " + tempVal + " - "
        + obstacle.getDistThresh());
    } else {
      calcs.append("\n     = " + runway.getTora() + " - " + runway.getStripEnd() + " - " + runway.getResa() + " - "
        + obstacle.getDistThresh());
    }

    //If displacement threshold is not 0
    if (dispThresh) {
      calcs.append(" - " + runway.getThreshold());
    }

    //Calculate the new TORA
    newTora = runway.getTora() - tempVal - obstacle.getDistThresh() - runway.getThreshold();
    runway.setCtora(newTora);
    calcs.append("\n     = " + runway.getCtora());

    //Calculate new ASDA
    runway.setCasda(newTora + runway.getStopway());
    calcs.append("\nASDA = (R) TORA + STOPWAY ");
    if (runway.getStopway() != 0) {
      calcs.append("\n     = " + newTora + " + (" + runway.getAsda() + " - " + runway.getTora() + ")");
    }
    calcs.append("\n     = " + runway.getCasda());

    //Calculate new TODA
    runway.setCtoda(newTora + runway.getClearway());
    calcs.append("\nTODA = (R) TORA + CLEARWAY ");
    if (runway.getClearway() != 0) {
      calcs.append("\n     = " + newTora + " + (" + runway.getToda() + " - " + runway.getTora() + ")");
    }
    calcs.append("\n     = " + runway.getCtoda());

    return calcs.toString();
  }

  /**
   * No pretty printing TORA away from obstacle.
   *
   * @param runway the runway being calculated
   */
  public static void toraAwayObs(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off away from obstacle...");

    double blastProtection = max((runway.getStripEnd() + runway.getResa()), aircraft.getBlastProtection());

    double newTora = runway.getTora() - blastProtection - obstacle.getDistThresh() - runway.getThreshold();

    runway.setCtora(newTora);
    runway.setCtoda(newTora + runway.getClearway());
    runway.setCasda(newTora + runway.getStopway());
  }

  /**
   * Function that re-declares the LDA (Landing Distance Available) after an obstacle appears.
   * When landing over, only the LDA has to change.
   *
   * @param runway the runway being calculated
   * @return the new LDA value
   */
  public static String ldaOver(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    logger.info("Re-declaring LDA for landing over obstacle...");
    StringBuilder calcs = new StringBuilder();

    double slope = obstacle.getHeight() * runway.getAls();
    double newLda;

    //Will use blast protection if it is larger than the slope calculation
    if (slope < aircraft.getBlastProtection()) {
      calcs.append("LDA  = Original LDA - Blast Protection- Distance from Threshold - Strip End");
      calcs.append("\n     = " + runway.getLda() + " - " + aircraft.getBlastProtection()
        + " - " + obstacle.getDistThresh() + " - " + runway.getStripEnd());
      newLda = runway.getLda() - aircraft.getBlastProtection()
        - obstacle.getDistThresh() - runway.getStripEnd();
    } else {
      calcs.append("LDA  = Original LDA - Slope Calculation - Distance from Threshold - Strip End");
      calcs.append("\n     = " + runway.getLda() + " - " + obstacle.getHeight()
        + "*" + runway.getAls() + " - " + obstacle.getDistThresh() + " - " + runway.getStripEnd());
      newLda = runway.getLda() - slope - obstacle.getDistThresh() - runway.getStripEnd();
    }

    boolean changeLDA = makeNewRESA(newLda, runway, obstacle, aircraft);

    if (changeLDA) {
      calcs.setLength(0);
      calcs.append("LDA  = Original LDA - Distance from Threshold - New RESA");
      calcs.append("\n     = " + runway.getLda() + " - " + obstacle.getDistThresh() + " - " + runway.getResa());
      newLda = runway.getLda() - obstacle.getDistThresh() - runway.getResa();
    }

    runway.setClda(newLda);
    calcs.append("\n     = " + runway.getClda());

    return calcs.toString();
  }

  /**
   * No pretty printing LDA over obstacle.
   *
   * @param runway the runway being calculated
   */
  public static void ldaOverObs(Runway runway, Obstacle obstacle, Aircraft aircraft) {
    logger.info("Re-declaring LDA for landing over obstacle...");

    double newLda;
    double tempVal = max(aircraft.getBlastProtection(), obstacle.getHeight() * runway.getAls());

    newLda = runway.getLda() - tempVal - obstacle.getDistThresh() - runway.getStripEnd();

    boolean changeLDA = makeNewRESA(newLda, runway, obstacle, aircraft);

    if (changeLDA) {
      newLda = runway.getLda() - obstacle.getDistThresh() - runway.getResa();
    }

    runway.setClda(newLda);
  }

  /**
   * Function that re-declares the LDA after an obstacle appears.
   * When landing towards, only the LDA has to change.
   *
   * @param runway the runway being calculated
   * @return the new LDA value
   */
  public static String ldaTowards(Runway runway, Obstacle obstacle) {
    logger.info("Re-declaring LDA for landing towards obstacle...");
    StringBuilder calcs = new StringBuilder();

    calcs.append("LDA  = Distance from Threshold - RESA - Strip End");
    calcs.append("\n     = " + obstacle.getDistThresh()
      + " - " + runway.getStripEnd() + " - " + runway.getResa());

    double newLda = obstacle.getDistThresh() - runway.getStripEnd() - runway.getResa();
    runway.setClda(newLda);
    calcs.append("\n     = " + runway.getClda());

    return calcs.toString();
  }

  /**
   * No pretty printing LDA towards obstacle.
   *
   * @param runway the runway being calculated
   */
  public static void ldaTowardsObs(Runway runway, Obstacle obstacle) {
    logger.info("Re-declaring LDA for landing towards obstacle...");

    double newLda = obstacle.getDistThresh() - runway.getStripEnd() - runway.getResa();
    runway.setClda(newLda);
  }

  /**
   * Judge if a new RESA has to be re-declared, if yes, then re-declare according to formula.
   *
   * @param newLda the new LDA after re-declaration
   * @param runway the runway
   * @param obstacle the obstacle
   * @param aircraft the aircraft
   */
  private static boolean makeNewRESA(double newLda, Runway runway, Obstacle obstacle, Aircraft aircraft) {
    if (newLda < aircraft.getBlastProtection()) {
      logger.info("Declaring a new RESA due to blast protection...");
      double newResa = aircraft.getBlastProtection() + obstacle.getDistThresh();
      logger.info("New RESA is: " + newResa);
      runway.setResa(newResa);
      return true;
    } else {
      return  false;
    }
  }
}
