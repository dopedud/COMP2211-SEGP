package uk.ac.soton.comp2211.team33.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class Calculator {

  private static Logger logger = LogManager.getLogger(Calculator.class);

  /**
   * Function that calculates the take-off runway available
   * TORA for Take Off Away
   * TORA = ASDA = TODA in this case
   *
   * @param runway
   * @return the new TORA
   */
  protected static void toraTowards(Runway runway) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");
    double newTora;
    double tempSlope = (runway.getCurrentObs().getHeight() * 50);
    //Compare with RESA, if RESA is greater, use RESA
    if (tempSlope < runway.getCresa()){
      tempSlope = runway.getCresa();
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
    runway.setCals(surfaceClimb);
    runway.setCtocs(surfaceClimb);
  }

  /**
   * Calculate the TORA for take-off away from the obstacle present
   * For ASDA and TODA, if	there	exist	any	Clearway	and/or	Stopway	then	those	values	should	be	added	to	the	reduced
   * TORA	for	the	TODA	and	ASDA	values.
   *
   * @param runway
   * @return the new TORA value
   */
  protected static void toraAway(Runway runway) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");
    double newTora;
    if (runway.getCurrentObs().getDistanceThresh() < 0) {
      newTora = runway.getTora() - runway.getBlastProtection() - runway.getCurrentObs().getDistanceThresh() - runway.getThreshold();
    } else {
      newTora = runway.getTora() - runway.getStripEnd() - runway.getCresa() - runway.getCurrentObs().getDistanceThresh();
    }

    runway.setCtora(newTora);
    runway.setCasda(newTora + runway.getStopway());
    runway.setCtoda(newTora + runway.getClearway());
    var surfaceClimb = runway.getCurrentObs().getHeight() * runway.getCurrentObs().getHeight() * 50;
    runway.setCals(surfaceClimb);
    runway.setCtocs(surfaceClimb);
  }

  /**
   * Function that re-declares the LDA (Landing Distance Available) after an obstacle appears
   * When landing over, only the LDA has to change.
   *
   * @param runway
   * @return the new LDA
   */
  protected static double ldaOver(Runway runway) {
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
  protected static double ldaTowards(Runway runway) {
    logger.info("Re-declaring LDA for landing towards obstacle...");
    var newLda = runway.getCurrentObs().getDistanceThresh() - runway.getStripEnd() - runway.getCresa();
    runway.setClda(newLda);
    return runway.getClda();
  }


}
