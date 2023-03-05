package uk.ac.soton.comp2211.team33.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.max;

import uk.ac.soton.comp2211.team33.entities.Runway;

/**
 * The static class Calculator is a utility class that handles the main calculation involved in a runway re-declaration.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public final class Calculator {

  private static final Logger logger = LogManager.getLogger(Calculator.class);

  /**
   * Private constructor to prevent instantiating.
   */
  private Calculator() {}

  /**
   * Function that calculates the take-off runway available.
   * TORA for Take Off Away.
   * TORA = ASDA = TODA in this case.
   *
   * @param runway  the runway being calculated
   * @return        the new TORA value
   */
  public static String toraTowards(Runway runway) {
    // TODO: 02/03/2023 Add string output 
    logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");

    StringBuilder calcs = new StringBuilder();
    calcs.append(runway.getRdesignator() + " (Take Off Towards, Landing Towards): ");

    double newTora;
    double tempSlope = runway.getCurrentObs().getHeight() * runway.getTocs();
    boolean zeroDisp = false; //True if displaced threshold is 0
    boolean useRESA = false; // will use RESA if RESA > slope

    //Check if threshold is 0
    if (runway.getThreshold() == 0) {
      zeroDisp = true;
      calcs.append("\nTORA = Distance from Threshold");
    } else {
      calcs.append("\nTORA = Distance from Threshold + Displaced Threshold");
    }

    //Compare with RESA, if RESA is greater, use RESA
    if (tempSlope < runway.getResa()) {
      tempSlope = runway.getResa();
      useRESA = true;
      calcs.append(" - Strip End");
    } else {
      calcs.append(" - Slope Calculation - Strip End");
    }

    //When there is no displacement, ignore it
    if (zeroDisp) {
      calcs.append("\n     = " + runway.getCurrentObs().getDistThresh());
    } else {
      calcs.append("\n     = " + runway.getCurrentObs().getDistThresh() + " + " + runway.getThreshold());
    }

    //If RESA is used, append accordingly
    if (useRESA) {
      calcs.append(" - " + runway.getResa() + " - " + runway.getStripEnd());
    } else {
      calcs.append(" - " + runway.getCurrentObs().getHeight() + "*"
          + runway.getTocs() + " - " + runway.getStripEnd());
    }

    newTora = runway.getCurrentObs().getDistThresh() + runway.getThreshold() - tempSlope - runway.getStripEnd();
    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);

    calcs.append("\n     = " + newTora);
    calcs.append("\nTODA = (R) TORA" + "\n     = " + newTora);
    calcs.append("\nASDA = (R) TORA" + "\n     = " + newTora);

    return calcs.toString();
  }

  public static void toraTowardsObs(Runway runway) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");

    double newTora;
    double slope = max(runway.getResa(), runway.getCurrentObs().getHeight() * runway.getTocs());

    newTora = runway.getCurrentObs().getDistThresh() + runway.getThreshold() - slope - runway.getStripEnd();
    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);
  }

  /**
   * Calculate the TORA for take-off away from the obstacle present.
   * For ASDA and TODA, if there exists any clearway and/or stopway then those values should be added to the reduced
   * TORA for the TODA and ASDA values.
   *
   * @param runway  the runway being calculated
   * @return        the new TORA value
   */
  public static String toraAway(Runway runway) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off away from obstacle...");
    StringBuilder calcs = new StringBuilder();
    calcs.append(runway.getRdesignator() + "(Take Off Away, Landing Over): \n");

    double newTora;
    boolean useBlast = false;
    boolean dispThresh = false;
    double tempVal = max((runway.getStripEnd() + runway.getResa()), runway.getAircraft().getBlastProtection());

    calcs.append("TORA = Original TORA ");

    //See if the calculation will be using blast protection when it is greater than RESA or TOCS * Height
    if ((runway.getStripEnd() + runway.getResa()) < runway.getAircraft().getBlastProtection()) {
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
        + runway.getCurrentObs().getDistThresh());
    } else {
      calcs.append("\n     = " + runway.getTora() + " - " + runway.getStripEnd() + " - " + runway.getResa() + " - "
        + runway.getCurrentObs().getDistThresh());
    }

    //If displacement threshold is not 0
    if (dispThresh) {
      calcs.append(" - " + runway.getThreshold());
    }

    //Calculate the new TORA
    newTora = runway.getTora() - tempVal - runway.getCurrentObs().getDistThresh() - runway.getThreshold();
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

  public static void toraAwayObs(Runway runway) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off away from obstacle...");

    double newTora;
    double blastProtection = max((runway.getStripEnd() + runway.getResa()), runway.getAircraft().getBlastProtection());

    newTora = runway.getTora() - blastProtection - runway.getCurrentObs().getDistThresh() - runway.getThreshold();

    runway.setCtora(newTora);
    runway.setCtoda(newTora + runway.getClearway());
    runway.setCasda(newTora + runway.getStopway());
  }

  /**
   * Function that re-declares the LDA (Landing Distance Available) after an obstacle appears.
   * When landing over, only the LDA has to change.
   *
   * @param runway  the runway being calculated
   * @return        the new LDA value
   */
  public static String ldaOver(Runway runway) {
    logger.info("Re-declaring LDA for landing over obstacle...");
    StringBuilder calcs = new StringBuilder();

    double slope = runway.getCurrentObs().getHeight() * runway.getAls();
    double newLda;

    //Will use blast protection if it is larger than the slope calculation
    if (slope < runway.getAircraft().getBlastProtection()) {
      calcs.append("LDA  = Original LDA - Blast Protection- Distance from Threshold - Strip End");
      calcs.append("\n     = " + runway.getLda() + " - " + runway.getAircraft().getBlastProtection()
          + " - " + runway.getCurrentObs().getDistThresh() + " - " + runway.getStripEnd());
      newLda = runway.getLda() - runway.getAircraft().getBlastProtection()
          - runway.getCurrentObs().getDistThresh() - runway.getStripEnd();
    } else {
      calcs.append("LDA  = Original LDA - Slope Calculation - Distance from Threshold - Strip End");
      calcs.append("\n     = " + runway.getLda() + " - " + runway.getCurrentObs().getHeight()
          + "*" + runway.getAls() + " - " + runway.getCurrentObs().getDistThresh() + " - " + runway.getStripEnd());
      newLda = runway.getLda() - slope - runway.getCurrentObs().getDistThresh() - runway.getStripEnd();
    }

    runway.setClda(newLda);
    calcs.append("\n     = " + runway.getClda());
    return calcs.toString();
  }

  public static void ldaOverObs(Runway runway) {
    logger.info("Re-declaring LDA for landing over obstacle...");

    double newLda;
    double slope = max(runway.getAircraft().getBlastProtection(), runway.getCurrentObs().getHeight() * runway.getAls());

    newLda = runway.getLda() - slope - runway.getCurrentObs().getDistThresh() - runway.getStripEnd();

    runway.setClda(newLda);
  }

  /**
   * Function that re-declares the LDA after an obstacle appears.
   * When landing towards, only the LDA has to change.
   *
   * @param runway  the runway being calculated
   * @return        the new LDA value
   */
  public static String ldaTowards(Runway runway) {
    logger.info("Re-declaring LDA for landing towards obstacle...");
    StringBuilder calcs = new StringBuilder();

    calcs.append("LDA  = Distance from Threshold - RESA - Strip End");
    calcs.append("\n     = " + runway.getCurrentObs().getDistThresh()
        + " - " + runway.getStripEnd() + " - " + runway.getResa());

    var newLda = runway.getCurrentObs().getDistThresh() - runway.getStripEnd() - runway.getResa();
    runway.setClda(newLda);
    calcs.append("\n     = " + runway.getClda());

    return calcs.toString();
  }

  public static void ldaTowardsObs(Runway runway) {
    logger.info("Re-declaring LDA for landing towards obstacle...");

    double newLda = runway.getCurrentObs().getDistThresh() - runway.getStripEnd() - runway.getResa();
    runway.setClda(newLda);
  }
}
