package com.example.kist.auxiliary;

import java.util.Date;

public class Timer {
    private Date now;
    private long beginningMilliTime;
    private long endMilliTime;
    private long period;

    public Timer() {
        this.beginningMilliTime = 0;
        this.endMilliTime = 0;
        this.period = 0;
        this.now = new Date();
    }
    public void stopTimer() {
        //Milliseconds for end
        this.now = new Date();
        endMilliTime = this.now.getTime();
        //System.out.println("stopTimer -> onKeyListener");
    }
    public long calculateTime() {
        //sub of two values beginningMilliTime and endMilliTime
         return period = endMilliTime - beginningMilliTime;
    }
    public void writeTime() {
        //System.out zasad
        //System.out.println(Long.toString(period));
    }
    public void resetTimer() {
        beginningMilliTime = 0;
        endMilliTime = 0;
        period = 0;
    }
    public void startTimer() {
        //Milliseconds for start
        this.now = new Date();
        beginningMilliTime = this.now.getTime();
        //System.out.println("startTimer -> onTextChanged");

    }

}
