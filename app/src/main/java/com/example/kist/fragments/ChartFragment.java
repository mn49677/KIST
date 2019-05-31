package com.example.kist.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.kist.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChartFragment extends Fragment {
    View chartsView;
    BarChart chart;


    public void setChosenVariable(String chosenVariable) {
        this.chosenVariable = chosenVariable;
    }
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    //1. prikaz podataka
    private String chosenVariable = "WPM";
    private String orientation = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chartsView = inflater.inflate(R.layout.charts_layout,container, false);
        setFirstChart();
        return chartsView;
    }

    public void setFirstChart() {
        chart = chartsView.findViewById(R.id.chart1);
        /*
        dovesti podatke
        1. dohvatiti sve fileove -> pristupiti folderu i uzeti popis fileova i napraviti fju koja za zadane parametre vraca je li datoteka za citati
                                 -> ako je za citati, uzeti stupac i dodati tu parsiranu vrijednost u zbroj za zadani naziv tipkovnice
                                 -> izracunati srednju vrijednost

        2. ubaciti vrijednosti u chart -> jos vidjeti kako
        */
        File root = Environment.getExternalStorageDirectory();
        File directory = new File(root, "KIST_tests");
        if(!directory.exists()) {
            directory.mkdirs();
        }

        ArrayMap<String, ArrayList<String>> am = organizeByKeyboard();

        Set<String> amKeys = am.keySet();
        for(String key : amKeys) {
            ArrayList<String> al = am.get(key);
            ArrayList<String> pomocna = new ArrayList<>(am.get(key));
            for(String fileName : al) {
                if(!checkOrientation(fileName)) {
                    pomocna.remove(pomocna.indexOf(fileName));
                }
                am.put(key, pomocna);
            }
        }

        //------------------------
        final ArrayMap<String, Double> valuesForKeyboards = new ArrayMap<>();
        FileReader reader = null;
        ArrayList<String> filesSameKeyboard;
        ArrayMap<String, Integer> numberOfValues = new ArrayMap<>();
        for(String keyboard : am.keySet()) {
            filesSameKeyboard = am.get(keyboard);
            System.out.println(filesSameKeyboard.toString());
            for(String fileName : filesSameKeyboard) {
                 try {
                     File rootKISTtests = new File(Environment.getExternalStorageDirectory(), "KIST_tests");
                     File file = new File(rootKISTtests + File.separator + fileName);
                     reader = new FileReader(file);
                     BufferedReader r = new BufferedReader(reader);
                     int variablePosition = -1;
                     String line = r.readLine();
                     String[] elements = line.split(",");
                     for(int i = 0; i < elements.length; ++i) {
                         if(elements[i].equals(chosenVariable)) {
                             variablePosition = i;
                             System.out.println("Variable position: " + i);
                             break;
                         }
                     }
                     while((line = r.readLine()) != null) {
                         String[] elementsValues = line.split(",");
                         Double valueInDouble = 0.0;
                         if(variablePosition != -1) {
                             try {
                                 valueInDouble = Double.parseDouble(elementsValues[variablePosition]);
                             } catch (Exception ex) {
                                 continue;
                             }
                         }
                         if(valuesForKeyboards.containsKey(keyboard)) {
                             valuesForKeyboards.put(keyboard, valuesForKeyboards.get(keyboard)+valueInDouble);
                             numberOfValues.put(keyboard, numberOfValues.get(keyboard) + 1);
                         }
                         else {
                             valuesForKeyboards.put(keyboard, valueInDouble);
                             numberOfValues.put(keyboard, 1);
                         }
                     }
                 } catch (IOException ex) {
                     System.err.println("Error while reading file.");
                }
                //uzeti vrijednost varijable koja se racuna
                //dodati tu vrijednost u novu mapu valuesForKeyboards
            }
        }
        //System.out.println(am.keySet().contains("qwerty"));
        //System.out.println(am.get("qwerty"));
        for(String key: am.keySet()) {

            System.out.println(numberOfValues.get(key));
            valuesForKeyboards.put(key, valuesForKeyboards.get(key) / numberOfValues.get(key));
            System.out.println( key + ": "+valuesForKeyboards.get(key));
        }
        //u novoj mapi podijeliti vrijednosti sa brojem vrijednosti kako bi se dobila srednja vrijednost
        //valuesForKeyboards has all variable means
        List<BarEntry> entries = new ArrayList<>();
        float i =0f;
        final ArrayMap<Float, String> keyForValue = new ArrayMap<>();
        for(String key : valuesForKeyboards.keySet()) {
            entries.add(new BarEntry(i, (float) valuesForKeyboards.get(key).doubleValue()));
            i = i+ 1f;
            keyForValue.put((float) valuesForKeyboards.get(key).doubleValue(), key);
        }
        System.out.println("Log entriesa: "+entries.toString());
        BarDataSet set = new BarDataSet(entries, this.chosenVariable);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.5f);
        String[] labels = valuesForKeyboards.keySet().toArray(new String[valuesForKeyboards.size()]);

        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate();
        Description d = new Description();
        d.setText("");
        chart.setDescription(d);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setCenterAxisLabels(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public Boolean checkOrientation(String fileName) {
        fileName = fileName.replace("][", ",");
        fileName = fileName.replace("[", "");
        fileName = fileName.replace("]", "");
        String[] prefs = fileName.split(",");
        String orientation = prefs[3];
        System.out.println(orientation);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String orientationSP = sharedPreferences.getString("orientationChart", "Portrait");
        if(orientation.equals("Not defined")) return true;
        else if(orientation.equals(orientationSP)) return true;
        else return false;
    }

    public ArrayMap<String, ArrayList<String>> organizeByKeyboard() {
        ArrayMap<String, ArrayList<String>> am = new ArrayMap<>();
        File root = Environment.getExternalStorageDirectory();
        File directory = new File(root, "KIST_tests");
        if(!directory.exists()) {
            directory.mkdirs();
        }
        for(File f : directory.listFiles()) {
            String keyboard = f.getName()
                    .replace("][", ",")
                    .replace("]", "")
                    .replace("[", "")
                    .split(",")[1];
            if (am.containsKey(keyboard)) {
                System.out.println(keyboard + ": " + f.getName());
                am.get(keyboard).add(f.getName());
            } else {
                System.out.println(keyboard + ": " + f.getName());
                am.put(keyboard, new ArrayList<String>());
                am.get(keyboard).add(f.getName());
            }
        }
        return am;
    }


}
