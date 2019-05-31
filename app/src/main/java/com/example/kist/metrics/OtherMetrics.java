package com.example.kist.metrics;

public class OtherMetrics {
    private Double correctionEfficiency;
    private Double participantConscientiousness;
    private Double utilisedBandwidth;
    private Double wastedBandwidth;
    private Double TER;
    private Double NCER;
    private Double CER;
    private Double IF;
    private Double F;
    private Double C;
    private Double INF;


    public void setOtherMetrics(int C, int F, int IF, int INF) {
        this.C = (double)C;
        this.F = (double)F;
        this.INF = (double)INF;
        this.IF = (double)IF;

    }

    public Double getCorrectionEfficiency() {
        correctionEfficiency = (double)IF/(double)F;
        return correctionEfficiency;
    }

    public Double getParticipantConscientiousness() {
        participantConscientiousness = (double)IF/(double)(IF+INF);
        return participantConscientiousness;
    }

    public Double getUtilisedBandwidth() {
        utilisedBandwidth = (double)C/(double)(C+INF+IF+F);
        return utilisedBandwidth;
    }

    public Double getWastedBandwidth() {
        wastedBandwidth = 1-utilisedBandwidth;
        return wastedBandwidth;
    }

    public Double getTER() {
        TER = getNCER() + getCER();
        return TER;
    }

    public Double getNCER() {
        NCER = (double)INF/(double)(INF+IF+F);
        return NCER;
    }

    public Double getCER() {
        CER = (double)IF/(double)(INF+IF+F);
        return CER;
    }
}
