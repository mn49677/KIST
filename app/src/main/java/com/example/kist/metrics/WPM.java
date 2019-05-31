package com.example.kist.metrics;

import android.service.autofill.RegexValidator;

public class WPM {
    //algorithm for calculating typing speed (variable WPM), taken from: https://www.speedtypingonline.com/typing-equations

    public static float getValue(String transcribedText, long timeInMilli) {
        float timeInSeconds = (float)(((float)timeInMilli)/(float)1000);
        //System.out.println("Time in seconds: " + timeInSeconds);
        String convertedText = transcribedText.replaceAll("[^a-zA-Z0-9]", "");
        float timeInMinutes = timeInSeconds/((float)60);
        int characterNumber = convertedText.length();
        System.out.println("WPM checker: " + characterNumber + " " + timeInSeconds);
        return ((float)characterNumber/(float)5)/timeInMinutes;
    }


}
