package uk.ac.soton.comp2211.team33;

import uk.ac.soton.comp2211.team33.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.utilities.Calculator;

public class TesterClass {

  private static Logger logger = LogManager.getLogger(TesterClass.class);

  public static void main(String[] args) {
    var airport1 = new Airport("Heathrow","London");

    var tempObs = new Obstacle("Obstacle1", 20,50);

    var runway2 = new Runway("09L",3902,3902,3902,3595,240,307);
    airport1.addRunway(runway2);
    airport1.getRunway("09L").setCurrentObs(new Obstacle("Obstacle2", 20, 3546));

    var runway1 = new Runway("27R", 3884, 3962, 3884, 3884, 240, 0,
      new Aircraft("A380", 300));
    airport1.addRunway(runway1);
    airport1.getRunway("27R").setCurrentObs(tempObs);

    System.out.println(Calculator.toraTowards(airport1.getRunway("09L")));
    System.out.println(Calculator.ldaTowards(airport1.getRunway("09L")));
    System.out.println();
    System.out.println(Calculator.toraAway(airport1.getRunway("27R")));
    System.out.println(Calculator.ldaOver(airport1.getRunway("27R")));
  }
}
