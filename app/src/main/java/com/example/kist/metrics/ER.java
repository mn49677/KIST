package com.example.kist.metrics;

public class ER {

    public static double getValueOldMSDER(String presentedText, String transcribedText) {
        float MSD;
        int presentedTextSize;
        int transcribedTextSize;
        com.example.kist.metrics.MSD msder = new MSD(presentedText, transcribedText);
        double er = msder.getErrorRate();
        //double ner = msder.getErrorRateNew();
        return er;
    }

    public static double getValueNewMSDER(String presentedText, String transcribedText) {
        float MSD;
        int presentedTextSize;
        int transcribedTextSize;
        com.example.kist.metrics.MSD msder = new MSD(presentedText, transcribedText);
        //double er = msder.getErrorRateNew();
        double ner = msder.getErrorRateNew();
        return ner;
    }
}
