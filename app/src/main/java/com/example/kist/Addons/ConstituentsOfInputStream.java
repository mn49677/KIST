package com.example.kist.Addons;

import com.example.kist.metrics.MSD;
import java.util.HashMap;
import java.util.Map;

public class ConstituentsOfInputStream {

    //INF
    public static int getIncorrectNotFixed(String presentedText, String transcribedText) {
        MSD msd = new MSD(presentedText, transcribedText);
        return msd.getMSD();
    }
    //C
    public static int getCorrect(String presentedText, String transcribedText) {
        MSD msd = new MSD(presentedText, transcribedText);
        int maxLength = presentedText.length() >= transcribedText.length() ? presentedText.length() : transcribedText.length();
        return (maxLength - msd.getMSD());
    }
    //IF
    public static int getIncorrectFixed(String inputStream, String transcribedText) {
        Pair<String> pair = diff(inputStream, transcribedText);
        int incorrectFixes= 0;
        String s = pair.first;
        for(char c : s.toCharArray()) {
            if(!(s.length() <= inputStream.indexOf(c) + 1)) return incorrectFixes;
            if (!(s.length() <= inputStream.indexOf(c) + 1) && (Character.compare(inputStream.charAt(inputStream.indexOf(c) + 1), '<') == 0))
                ++incorrectFixes;
        }
        return incorrectFixes;
    }
    //F
    public static int getFixes(String inputStream, String transcribedText) {
        Pair<String> pair = diff(inputStream, transcribedText);
        int fixes = 0;
        for(char c : pair.first.toCharArray()) {
            if(Character.compare(c, '<') == 0) ++fixes;
        }
        return fixes;
    }

    public static Pair<String> diff(String a, String b) {
        return diffHelper(a, b, new HashMap<Long, Pair<String>>());
    }

    /**
     * Recursively compute a minimal set of characters while remembering already computed substrings.
     * Runs in O(n^2).
     */
    private static Pair<String> diffHelper(String a, String b, Map<Long, Pair<String>> lookup) {
        long key = ((long) a.length()) << 32 | b.length();
        if (!lookup.containsKey(key)) {
            Pair<String> value;
            if (a.isEmpty() || b.isEmpty()) {
                value = new Pair<>(a, b);
            } else if (a.charAt(0) == b.charAt(0)) {
                value = diffHelper(a.substring(1), b.substring(1), lookup);
            } else {
                Pair<String> aa = diffHelper(a.substring(1), b, lookup);
                Pair<String> bb = diffHelper(a, b.substring(1), lookup);
                if (aa.first.length() + aa.second.length() < bb.first.length() + bb.second.length()) {
                    value = new Pair<>(a.charAt(0) + aa.first, aa.second);
                } else {
                    value = new Pair<>(bb.first, b.charAt(0) + bb.second);
                }
            }
            lookup.put(key, value);
        }
        return lookup.get(key);
    }

    public static class Pair<T> {
        public Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }

        public final T first, second;

        public String toString() {
            return "(" + first + "," + second + ")";
        }
    }
}

