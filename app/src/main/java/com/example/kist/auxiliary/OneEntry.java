package com.example.kist.auxiliary;

public class OneEntry {

    private float wpm;
    private float kspc;
    private int bc;
    private double er;
    private String textOutput;
    private String textInput;
    private String inputStream;
    private Double correctionEfficiency;
    private Double participantConscientiousness;
    private Double utilisedBandwidth;
    private Double wastedBandwidth;
    private Double TER;
    private Double NCER;
    private Double CER;

    //konstruktor
    public OneEntry(
            float wpm,
            float kspc,
            int bc,
            double er,
            String textOutput,
            String textInput,
            String inputStream,
            Double correctionEfficiency,
            Double participantConscientiousness,
            Double utilisedBandwidth,
            Double wastedBandwidth,
            Double TER,
            Double NCER,
            Double CER) {
        this.bc = bc;
        this.er = er;
        this.kspc = kspc;
        this.wpm = wpm;
        this.correctionEfficiency = correctionEfficiency;
        this.participantConscientiousness = participantConscientiousness;
        this.utilisedBandwidth = utilisedBandwidth;
        this.wastedBandwidth = wastedBandwidth;
        this.TER = TER;
        this.NCER = NCER;
        this.CER = CER;
        this.textInput = textInput;
        this.textOutput = textOutput;
        this.inputStream = inputStream;
    }

    //getteri
    public double getEr() {
        return er;
    }

    public float getKspc() {
        return kspc;
    }

    public float getWpm() {
        return wpm;
    }

    public int getBc() {
        return bc;
    }

    public String getTextInput() {
        return textInput;
    }

    public String getTextOutput() {
        return textOutput;
    }

    public String getInputStream() {
        return inputStream;
    }

    public Double getCorrectionEfficiency() {
        return correctionEfficiency;
    }

    public Double getParticipantConscientiousness() {
        return participantConscientiousness;
    }

    public Double getUtilisedBandwidth() {
        return utilisedBandwidth;
    }

    public Double getWastedBandwidth() {
        return wastedBandwidth;
    }

    public Double getTER() {
        return TER;
    }

    public Double getNCER() {
        return NCER;
    }

    public Double getCER() {
        return CER;
    }
}
