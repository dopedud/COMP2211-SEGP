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
  public static void toraTowards(Runway runway) {
    // TODO: 02/03/2023 Add string output 
    logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");
    double newTora;
    double tempSlope = (runway.getCurrentObs().getHeight() * 50);
    //Compare with RESA, if RESA is greater, use RESA
    if (tempSlope < runway.getResa()) {
      tempSlope = runway.getResa();
    }

    if (runway.getCurrentObs().getDistanceThresh() < 0) {
      newTora = runway.getCurrentObs().getDistanceThresh() - tempSlope - runway.getStripEnd();
    } else {
      newTora = runway.getCurrentObs().getDistanceThresh() + runway.getThreshold() - tempSlope - runway.getStripEnd();
    }

    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);
    var surfaceClimb = runway.getCurrentObs().getHeight() * runway.getCurrentObs().getHeight() * 50;
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

    var surfaceClimb = runway.getCurrentObs().getHeight() * runway.getTocs();

    double newTora;

    if (runway.getCurrentObs().getDistanceThresh() < 0) {
      //When the obstacle is outside the runway
      newTora = runway.getTora() - runway.getAircraft().getBlastProtection() - runway.getCurrentObs().getDistanceThresh() - runway.getThreshold();
      calcs.append("TORA = Original TORA - Blast Protection - Distance from Threshold");
      if (runway.getThreshold() !=0) {
        calcs.append(" - Displaced Threshold");
      }
      calcs.append("\n     = " + runway.getTora() + " - " + runway.getAircraft().getBlastProtection() + " - "
        + runway.getCurrentObs().getDistanceThresh());
      if (runway.getThreshold() !=0){
        calcs.append(" - " + runway.getThreshold());
      }
      calcs.append("\n     = " + newTora + "\n");
      
    } else if (max(surfaceClimb, runway.getResa()) < runway.getAircraft().getBlastProtection()) {
      //When the obstacle is inside the runway and the blast protection is greater than RESA or TOCS * Height

      calcs.append("TORA = Original TORA - Blast Protection - Distance from Threshold \n");
      calcs.append("     = " + runway.getTora() + " - " + runway.getAircraft().getBlastProtection() + " - "
        + runway.getCurrentObs().getDistanceThresh() + "\n");
      newTora = runway.getTora() - runway.getAircraft().getBlastProtection() - runway.getCurrentObs().getDistanceThresh();

    } else {
      //When the obstacle is inside the runway and the blast protection is not greater than RESA or TOCS * Height
      if (runway.getResa() < surfaceClimb) {
        calcs.append("TORA = Original TORA - Strip End - Slope Calculation - Distance From Threshold \n");
        calcs.append("     = " + runway.getTora() + " - " + runway.getStripEnd() + " - " + surfaceClimb + " - "
          + runway.getCurrentObs().getDistanceThresh() + "\n");
        newTora = runway.getTora() - runway.getStripEnd() - surfaceClimb - runway.getCurrentObs().getDistanceThresh();
      } else {
        calcs.append("TORA = Original TORA - Strip End - RESA - Distance From Threshold \n");
        calcs.append("     = " + runway.getTora() + " - " + runway.getStripEnd() + " - " + runway.getResa() + " - "
          + runway.getCurrentObs().getDistanceThresh() + "\n");
        newTora = runway.getTora() - runway.getStripEnd() - runway.getResa() - runway.getCurrentObs().getDistanceThresh();
      }

    }

    runway.setCtora(newTora);
    runway.setCasda(newTora + runway.getStopway());
    calcs.append("ASDA = (R) TORA + STOPWAY \n" + "     = " + runway.getCasda() + "\n");
    runway.setCtoda(newTora + runway.getClearway());
    calcs.append("TODA = (R) TORA + CLEARWAY \n" + "    = " + runway.getCtoda() + "\n");

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
