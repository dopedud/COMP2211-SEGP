package uk.ac.soton.comp2211.team33.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.App;

abstract class Calculator {

  private static Logger logger = LogManager.getLogger(Calculator.class);

  /**
   * Function that calculates the take-off runway available
   * TORA for Take Off Away
   * TORA = ASDA = TODA in this case
   */
  protected double toraTowards(Runway runway, Obstacle obstacle, double displacedThreshold) {
    logger.info("Re-declaring TORA, TODA and ASDA for take-off towards obstacle...");
    var newTora = runway.getTora() - runway.getBlastProtection() - obstacle.getDistanceThresh() - displacedThreshold;
    runway.setCtora(newTora);
    runway.setCasda(newTora);
    runway.setCtoda(newTora);
    return newTora;
  }

  /**
   * Function that re-declares the LDA (Landing Distance Available) after an obstacle appears
   * When landing over, only the LDA has to change.
   */
  protected double ldaOver(Runway runway, Obstacle obstacle) {
    logger.info("Re-declaring LDA for landing over obstacle...");
    var newLda = runway.getLda() - obstacle.getDistanceThresh() - (obstacle.getHeight() * 50) - runway.getStripEnd();
    runway.setClda(newLda);
    return runway.getClda();
  }

  /**
   * Function that re-declares the LDA after an obstacle appears
   * When landing towards, only the LDA has to change.
   */
  protected double ldaTowards(Runway runway, Obstacle obstacle) {
    logger.info("Re-declaring LDA for landing towards obstacle...");
    var newLda = obstacle.getDistanceThresh() - runway.getStripEnd() - runway.getCresa();
    runway.setClda(newLda);
    return runway.getClda();
  }


}
