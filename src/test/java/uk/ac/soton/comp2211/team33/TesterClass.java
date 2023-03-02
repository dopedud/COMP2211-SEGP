package uk.ac.soton.comp2211.team33;

import uk.ac.soton.comp2211.team33.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.utilities.Calculator;


public class TesterClass {

  private static Logger logger = LogManager.getLogger(TesterClass.class);

  public static void main(String[] args) {
    var runway1 = new Runway("09L", 3902, 3902, 3902, 3595, 100, 306,
      0, 0, 60, new Aircraft("A380", 300));
    runway1.setCurrentObs(new Obstacle("Obstacle1", 12,100));
    //runway1.setCurrentObs(new Obstacle("Obstacle2", 15,100,50));
    String out = Calculator.toraAway(runway1);
    System.out.println(out);
    System.out.println("LDA  = " + Calculator.ldaOver(runway1));
  }
}
