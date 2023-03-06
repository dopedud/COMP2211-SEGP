package uk.ac.soton.comp2211.team33;

import uk.ac.soton.comp2211.team33.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.utilities.Calculator;

public class TesterClass {

  private static Logger logger = LogManager.getLogger(TesterClass.class);

  public static void main(String[] args) {
    var runway1 = new Runway("27R", 3884, 3962, 3884, 3884, 240, 0, new Aircraft("A380", 300));
    runway1.setCurrentObs(new Obstacle("Obstacle1", 20,50));
    //runway1.setCurrentObs(new Obstacle("Obstacle2", 15,100,50));
    String out = Calculator.toraAway(runway1);
    System.out.println(out);
    System.out.println(Calculator.ldaOver(runway1));
  }
}
