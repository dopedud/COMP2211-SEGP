package uk.ac.soton.comp2211.team33.models;

public class Calculator {
    //Function that calculates the take-off runway available
    // TORA for Take Off Away and Landing Over
    private double tora1 (Runway runway, double distanceThreshold, double displacedThreshold){
        var newTora = runway.getTora() - runway.getBlastProtection() - distanceThreshold - displacedThreshold;
        return 0;
    }
}
