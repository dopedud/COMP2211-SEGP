package uk.ac.soton.comp2211.team33.models;

abstract class Calculator {


public class Calculator {

    //Function that calculates the take-off runway available
    // TORA for Take Off Away
    protected double tora1 (Runway runway, double distanceThreshold, double displacedThreshold){
        var newTora = runway.getTora() - runway.getBlastProtection() - distanceThreshold - displacedThreshold;
        return 0;
    }

    protected double ldaOver (Runway runway, Obstacle obstacle) {
        var newLda = runway.getLda() - obstacle.getDistanceThresh() - (obstacle.getHeight() * 50) - runway.getStripEnd();
        runway.setClda(newLda);
        return runway.getClda();
    }


}
