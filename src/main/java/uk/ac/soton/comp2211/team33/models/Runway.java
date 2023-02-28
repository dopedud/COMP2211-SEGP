package uk.ac.soton.comp2211.team33.models;

import java.util.ArrayList;

public class Runway {

    //Obstacles on the runway
    private ArrayList<Obstacle> obstacles;
    //Current obstacle
    private Obstacle currentObs;
    private String rdesignator;
    //Initial values of the runway
    private double tora, toda, asda, lda, resa, als, tocs;
    //Currently used runway values (after calculation)
    private double ctora, ctoda, casda, clda, cresa, cals, ctocs;
    private double threshold, clearway, stopway, stripEnd, blastProtection;
    private double strip;

    public Runway(String rdesignator, double tora, double toda, double asda, double lda, double als,
                  double tocs, double threshold, double clearway, double stopway, double stripEnd, double blastProtection, double strip) {
        this.rdesignator = rdesignator;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
        this.resa = 240;
        this.als = als;
        this.tocs = tocs;
        this.ctora = tora;
        this.ctoda = toda;
        this.casda = asda;
        this.clda = lda;
        this.cresa = resa;
        this.cals = als;
        this.ctocs = tocs;
        this.threshold = threshold;
        this.clearway = clearway;
        this.stopway = stopway;
        this.stripEnd = stripEnd;
        this.blastProtection = blastProtection;
        this.strip = strip;
    }

    //Below are getters for some values that don't have to change but may be used in certain calculations
    public double getTora() {
        return tora;
    }

    public double getToda() {
        return toda;
    }

    public double getAsda() {
        return asda;
    }

    public double getLda() {
        return lda;
    }

    public double getResa() {
        return resa;
    }

    public double getAls() {
        return als;
    }

    public double getTocs() {
        return tocs;
    }

    public double getThreshold() {
        return threshold;
    }

    public double getClearway() {
        return clearway;
    }

    public double getStopway() {
        return stopway;
    }

    public double getStripEnd() {
        return stripEnd;
    }

    public double getBlastProtection() {
        return blastProtection;
    }

    public double getStrip() {
        return strip;
    }

    //Getters and setter for all current values that can be changed by a re-declaration
    public double getCtora() {
        return ctora;
    }

    public void setCtora(double ctora) {
        this.ctora = ctora;
    }

    public double getCtoda() {
        return ctoda;
    }

    public void setCtoda(double ctoda) {
        this.ctoda = ctoda;
    }

    public double getCasda() {
        return casda;
    }

    public void setCasda(double casda) {
        this.casda = casda;
    }

    public double getClda() {
        return clda;
    }

    public void setClda(double clda) {
        this.clda = clda;
    }

    public double getCresa() {
        return cresa;
    }

    public void setCresa(double cresa) {
        this.cresa = cresa;
    }

    public double getCals() {
        return cals;
    }

    public void setCals(double cals) {
        this.cals = cals;
    }

    public double getCtocs() {
        return ctocs;
    }

    public void setCtocs(double ctocs) {
        this.ctocs = ctocs;
    }
}
