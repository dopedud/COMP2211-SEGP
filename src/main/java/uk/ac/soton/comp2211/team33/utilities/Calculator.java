package uk.ac.soton.comp2211.team33.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.entities.Runway;

import static java.lang.Math.max;

public final class Calculator {

  private static Logger logger = LogManager.getLogger(Calculator.class);

  /**
   * Private constructor to prevent instantiating
   */
  private Calculator() {
    //Private class
  }

  /**
   * Function that calculates the take-off runway available
   * TORA for Take Off Away
   * TORA = ASDA = TODA in this case
   *
   * @param runway
   * @return the new TORA
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
      calcs.append("\n     = " + runway.getCurrentObs().getDistanceThresh());
    } else {
      calcs.append("\n     = " + runway.getCurrentObs().getDistanceThresh() + " + " + runway.getThreshold());
    }

    //If RESA is used, append accordingly
    if (useRESA) {
      calcs.append(" - " + runway.getResa() + " - " + runway.getStripEnd());
    } else {
      calcs.append(" - " + runway.getCurrentObs().getHeight() + "*" + runway.getTocs() + " - " + runway.getStripEnd());
    }

    newTora = runway.getCurrentObs().getDistanceThresh() + runway.getThreshold() - tempSlope - runway.getStripEnd();
    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);

    calcs.append("\n     = " + newTora);
    calcs.append("\nTODA = (R) TORA" + "\n     = " + newTora);
    calcs.append("\nASDA = (R) TORA" + "\n     = " + newTora);

    return calcs.toString();
  }

  /**
   * Calculate the TORA for take-off away from the obstacle present
   * For ASDA and TODA, if	there	exist	any	Clearway	and/or	Stopway	then	those	values	should	be	added	to	the	reduced
   * TORA	for	the	TODA	and	ASDA	values.
   *
   * @param runway
   * @return the new TORA value
   */
  public static String toraAway(Runway runway) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");
    StringBuilder calcs = new StringBuilder();
    calcs.append(runway.getRdesignator() + "(Take Off Away, Landing Over): \n");

    double newTora;
    boolean useBlast = false;
    boolean useRESA = false;
    boolean dispThresh = false;
    double tempVal = max((runway.getStripEnd()+runway.getResa()), runway.getAircraft().getBlastProtection());


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
    if(useBlast){
      calcs.append("\n     = " + runway.getTora() + " - " + tempVal + " - "
        + runway.getCurrentObs().getDistanceThresh());
    } else {
      calcs.append("\n     = " + runway.getTora() + " - " + runway.getStripEnd() + " - " + runway.getResa() + " - "
        + runway.getCurrentObs().getDistanceThresh());
    }

    //If displacement threshold is not 0
    if (dispThresh) {
      calcs.append(" - " + runway.getThreshold());
    }

    //Claculate the new TORA
    newTora = runway.getTora() - tempVal - runway.getCurrentObs().getDistanceThresh() - runway.getThreshold();
    runway.setCtora(newTora);
    calcs.append("\n     = " + runway.getCtora());
    runway.setCasda(newTora + runway.getStopway());
    calcs.append("\nASDA = (R) TORA + STOPWAY " + "\n     = " + runway.getCasda());
    runway.setCtoda(newTora + runway.getClearway());
    calcs.append("\nTODA = (R) TORA + CLEARWAY " + "\n     = " + runway.getCtoda());

    return calcs.toString();
  }

  /**
   * Function that re-declares the LDA (Landing Distance Available) after an obstacle appears
   * When landing over, only the LDA has to change.
   *
   * @param runway
   * @return the new LDA
   */
  public static double ldaOver(Runway runway) {
    // TODO: 02/03/2023 Add string output 
    logger.info("Re-declaring LDA for landing over obstacle...");
    var newLda = runway.getLda() - runway.getCurrentObs().getDistanceThresh() - runway.getStripEnd() - (runway.getCurrentObs().getHeight() * 50);
    runway.setClda(newLda);
    return runway.getClda();
  }

  /**
   * Function that re-declares the LDA after an obstacle appears
   * When landing towards, only the LDA has to change.
   *
   * @param runway
   * @return the new LDA
   */
  public static double ldaTowards(Runway runway) {
    // TODO: 02/03/2023 Add string output 
    logger.info("Re-declaring LDA for landing towards obstacle...");
    var newLda = runway.getCurrentObs().getDistanceThresh() - runway.getStripEnd() - runway.getResa();
    runway.setClda(newLda);
    return runway.getClda();
  }


}
