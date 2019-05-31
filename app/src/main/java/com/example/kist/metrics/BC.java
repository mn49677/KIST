package com.example.kist.metrics;

public class BC {
    private int backspaceCount = 0;

    public int getBackspaceCount() {
        return backspaceCount;
    }
    public Boolean increaseBC() {
        ++backspaceCount;
        return true;
    }
    public Boolean resetBC() {
        backspaceCount = 0;
        return true;
    }
}
