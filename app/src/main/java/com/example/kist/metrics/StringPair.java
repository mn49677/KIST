package com.example.kist.metrics;


public class StringPair
{
  public String s2 = ""; public String s1 = "";
  
  public StringPair() {}
  
  public void CopyConcat(StringPair p, char c1, char c2) {
    s1 += c1;s2 += c2;
  }
}
