package uk.ac.soton.comp2211.team33;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.utilities.Calculator;
import uk.ac.soton.comp2211.team33.entities.Obstacle;
import uk.ac.soton.comp2211.team33.entities.Runway;

public class TesterClass {

  private static Logger logger = LogManager.getLogger(TesterClass.class);

  public static void main(String[] args) {
    var runway1 = new Runway("09L", 3902, 3902, 3902, 3595, 100, 306,
      0, 0, 60, 300);
    runway1.addObstacle(new Obstacle("Obstacle1", 12,100));
    runway1.addObstacle(new Obstacle("Obstacle2", 15,100));
    runway1.selectObstacle("Obstacle1");
    System.out.println("09L (Take Off Away, Landing Over): ");
    Calculator.toraAway(runway1);
    System.out.println("TORA = " + runway1.getTora() + "-" + runway1.getBlastProtection() + "-" + runway1.getCurrentObs().getDistanceThreshold() + "-" + runway1.getThreshold() + "\n =" + runway1.getCtora());
    System.out.println("ASDA = " + runway1.getCasda());
    System.out.println("TODA = " + runway1.getCtoda());
    System.out.println("LDA  = " + Calculator.ldaOver(runway1));
  }
}
