package uk.ac.soton.comp2211.team33.models;

public class Runway {
    private String rdesignator;
    //Initial values of the runway
    private Double tora, toda, asda, lda, resa, als, tocs;
    //Currently used runway values (after calculation)
    private Double ctora, ctoda, casda, clda, cresa, cals, ctocs;
    private Double threshold, clearway, stopway, stripEnd, blastProtection;
    private Double strip;

    public Runway(String rdesignator, Double tora, Double toda, Double asda, Double lda, Double resa, Double als,
                  Double tocs, Double threshold, Double clearway, Double stopway, Double stripEnd, Double blastProtection, Double strip) {
        this.rdesignator = rdesignator;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
        this.resa = resa;
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
    public Double getTora() {
        return tora;
    }

    public Double getToda() {
        return toda;
    }

    public Double getAsda() {
        return asda;
    }

    public Double getLda() {
        return lda;
    }

    public Double getResa() {
        return resa;
    }

    public Double getAls() {
        return als;
    }

    public Double getTocs() {
        return tocs;
    }

    public Double getThreshold() {
        return threshold;
    }

    public Double getClearway() {
        return clearway;
    }

    public Double getStopway() {
        return stopway;
    }

    public Double getStripEnd() {
        return stripEnd;
    }

    public Double getBlastProtection() {
        return blastProtection;
    }

    public Double getStrip() {
        return strip;
    }

    //Getters and setter for all current values that can be changed by a re-declaration
    public Double getCtora() {
        return ctora;
    }

    public void setCtora(Double ctora) {
        this.ctora = ctora;
    }

    public Double getCtoda() {
        return ctoda;
    }

    public void setCtoda(Double ctoda) {
        this.ctoda = ctoda;
    }

    public Double getCasda() {
        return casda;
    }

    public void setCasda(Double casda) {
        this.casda = casda;
    }

    public Double getClda() {
        return clda;
    }

    public void setClda(Double clda) {
        this.clda = clda;
    }

    public Double getCresa() {
        return cresa;
    }

    public void setCresa(Double cresa) {
        this.cresa = cresa;
    }

    public Double getCals() {
        return cals;
    }

    public void setCals(Double cals) {
        this.cals = cals;
    }

    public Double getCtocs() {
        return ctocs;
    }

    public void setCtocs(Double ctocs) {
        this.ctocs = ctocs;
    }
}
