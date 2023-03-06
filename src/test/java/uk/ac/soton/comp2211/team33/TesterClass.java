package uk.ac.soton.comp2211.team33;

import uk.ac.soton.comp2211.team33.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.utilities.Calculator;

public class TesterClass {

  private static Logger logger = LogManager.getLogger(TesterClass.class);

  public static void main(String[] args) {
    var tempObs = new Obstacle("Obstacle1", 20,50);

    var runway2 = new Runway("09L",3902,3902,3902,3595,240,307);
    runway2.setCurrentObs(new Obstacle("Obstacle2", 20, 3546));


    var runway1 = new Runway("27R", 3884, 3962, 3884, 3884, 240, 0,
      new Aircraft("A380", 300));
    runway1.setCurrentObs(tempObs);

    System.out.println(Calculator.toraTowards(runway2));
    System.out.println(Calculator.ldaTowards(runway2));
    System.out.println();
    System.out.println(Calculator.toraAway(runway1));
    System.out.println(Calculator.ldaOver(runway1));
  }
}
