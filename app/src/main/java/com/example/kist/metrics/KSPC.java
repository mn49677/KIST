package com.example.kist.metrics;

public class KSPC {
    public static float getValue(int inputStream, int transcribedText) {

        float result = ((float)inputStream)/((float)transcribedText);
        return result;
    }
}
