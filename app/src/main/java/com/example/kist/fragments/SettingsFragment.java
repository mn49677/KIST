package com.example.kist.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.kist.R;

public class SettingsFragment extends Fragment {

    private View settingsView;

    //DEFAULT VALUES
    public SettingsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        setSettingsScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingsView = inflater.inflate(R.layout.settings_layout, container, false);
        //Elements initialization
        setSettingsScreen();
        return settingsView;
    }

    @SuppressLint("SetTextI18n")
    public void setSettingsScreen() {
        //Shared Preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String participantID = sharedPreferences.getString("participantID", "");
        String testMark = sharedPreferences.getString("testMark", "");
        String numberOfPhrases = sharedPreferences.getString("numberOfPhrases", "5");

        boolean wpm = sharedPreferences.getBoolean("wpmswitch", true);
        boolean bc = sharedPreferences.getBoolean("bcswitch", true);
        boolean msder = sharedPreferences.getBoolean("msderswitch", true);
        boolean kspc = sharedPreferences.getBoolean("kspcswitch", true);
        boolean ce = sharedPreferences.getBoolean("ceswitch", true);
        boolean pc = sharedPreferences.getBoolean("pcswitch", true);
        boolean ub = sharedPreferences.getBoolean("ubswitch", true);
        boolean wb = sharedPreferences.getBoolean("wbswitch", true);
        boolean ter = sharedPreferences.getBoolean("terswitch", true);
        boolean cer = sharedPreferences.getBoolean("cerswitch", true);
        boolean ncer = sharedPreferences.getBoolean("ncerswitch", true);

        String orientation = sharedPreferences.getString("orientation", "notDefined");
        String deviceType = sharedPreferences.getString("deviceType", "notDefined");
        String screenSize = sharedPreferences.getString("screenSize", "0");
        String keyboardType = sharedPreferences.getString("keyboardType", "");

        TextView textView;

        textView = settingsView.findViewById(R.id.id);
        textView.setText(participantID);
        textView = settingsView.findViewById(R.id.testMark);
        textView.setText(testMark);
        textView = settingsView.findViewById(R.id.numberOfPhrases);
        textView.setText(numberOfPhrases);
        textView = settingsView.findViewById(R.id.keyboardType);
        textView.setText(keyboardType);

        textView = settingsView.findViewById(R.id.wpm);
        if(wpm) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.bc);
        if(bc) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.msder);
        if(msder) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.kspc);
        if(kspc) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.ce);
        if(ce) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.pc);
        if(pc) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.ub);
        if(ub) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.wb);
        if(wb) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.ter);
        if(ter) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.cer);
        if(cer) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }
        textView = settingsView.findViewById(R.id.ncer);
        if(ncer) {
            textView.setText("Calculating");
        } else {
            textView.setText("Not calculating");
        }


        textView = settingsView.findViewById(R.id.orientation);
        if(orientation.equals(getString(R.string.not_defined))) textView.setText("Not defined");
        else if(orientation.equals("Portrait")) textView.setText("Portrait mode");
        else if(orientation.equals("Landscape")) textView.setText("Landscape");
        textView = settingsView.findViewById(R.id.deviceType);
        switch (deviceType) {
            case "Mobile phone":
                textView.setText("Mobile phone");
                break;
            case "Tablet":
                textView.setText("Tablet");
                break;
            case "Other":
                textView.setText("Other");
                break;
            case "Not defined":
                textView.setText("Not defined");
                break;
            default:
                textView.setText("Not defined");
                break;
        }
        textView = settingsView.findViewById(R.id.screenSize);
        textView.setText(screenSize + " inches");

    }
}
