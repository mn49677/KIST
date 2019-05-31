package com.example.kist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.View;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import android.widget.ListView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.view.Menu;
import com.example.kist.auxiliary.OneEntry;
import com.example.kist.auxiliary.PhrasesCollection;
import com.example.kist.auxiliary.ViewDialogOneButton;
import com.example.kist.fragments.ChartFragment;
import com.example.kist.fragments.InputFragment;
import com.example.kist.fragments.SavedFragment;
import com.example.kist.fragments.SettingsFragment;
import com.example.kist.metrics.ER;
import com.example.kist.metrics.KSPC;
import com.example.kist.metrics.OtherMetrics;
import com.example.kist.metrics.WPM;
import com.example.kist.Addons.ConstituentsOfInputStream;


public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private int numberOfPhrases = 1;
    private ArrayList<OneEntry> listOfEntries;
    private SavedFragment savedFragment;
    private SharedPreferences sharedPreferences;
    private boolean firstTimeLaunching = true;
    private String inputStream = "";
    PhrasesCollection col;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new InputFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new SettingsFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new SavedFragment();
                    savedFragment = (SavedFragment) fragment;
                    break;
                case R.id.chart_viewer:
                    fragment = new ChartFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listOfEntries = new ArrayList<>();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(2);
            //findViewById(R.id.navigation).setElevation(105);
            findViewById(R.id.navigation).setTranslationZ(100);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("orientationChart", "Portrait");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.SettingsEditer) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void onClick(View view) {
        InputFragment inputFragment = (InputFragment) fragment;
        col = inputFragment.getCol();
        inputFragment.getTimer().stopTimer();
        long time = inputFragment.getTimer().calculateTime();
        inputFragment.getTimer().resetTimer();
        inputFragment.setFirstTimeToTrue();

        //uzimanje output i input stringa za daljnu analizu i broj rijeci
        String rezultat = inputFragment.getInput().getText().toString();
        int wordCount = rezultat.split(" ").length;
        String presentedText = inputFragment.getOutput().getText().toString();
        String transcribedText = inputFragment.getInput().getText().toString();

        //Calculate wpm, er, bc and other
        int backspaceCount = inputFragment.getBc().getBackspaceCount();
        float wpm = WPM.getValue(transcribedText, time);
        float kspc = KSPC.getValue(inputFragment.getInputStreamCounter(), rezultat.length());
        double er = ER.getValueOldMSDER(presentedText, transcribedText);
        OtherMetrics otherMetrics = new OtherMetrics();
        otherMetrics.setOtherMetrics(ConstituentsOfInputStream.getCorrect( presentedText, transcribedText),
                ConstituentsOfInputStream.getFixes(inputFragment.getInputStream(), presentedText),
                ConstituentsOfInputStream.getIncorrectFixed(inputFragment.getInputStream(), presentedText),
                ConstituentsOfInputStream.getIncorrectNotFixed(transcribedText, presentedText));
        Double correctionEfficiency = otherMetrics.getCorrectionEfficiency();
        Double participantConscientiousness = otherMetrics.getParticipantConscientiousness();
        Double utilisedBandwidth = otherMetrics.getUtilisedBandwidth();
        Double wastedBandwidth = otherMetrics.getWastedBandwidth();
        Double TER = otherMetrics.getTER();
        Double NCER =  otherMetrics.getNCER();
        Double CER = otherMetrics.getCER();
        inputFragment.getBc().resetBC();
        inputFragment.resetInputStreamCounter();
        inputFragment.setLastEntry("");

        //testni izlaz na standardni izlaz
        //System.out.println("Word count: " + wordCount);
        //System.out.println("Time: " + time);
        //System.out.println("WPM: " + Float.toString(wpm));
        //System.out.println("BC: " + Integer.toString(backspaceCount));
        //System.out.println("KSPC: " + Float.toString(kspc));
        //System.out.println("Atributi(prilikom upisivanja): " + wpm +" "+ kspc+" "+ backspaceCount+" "+ 0+" "+ inputFragment.getOutput().getText().toString()+" "+inputFragment.getInput().getText().toString());
        listOfEntries.add(new OneEntry(wpm, kspc, backspaceCount, er, inputFragment.getOutput().getText().toString(),inputFragment.getInput().getText().toString(), inputFragment.getInputStream(),correctionEfficiency, participantConscientiousness, utilisedBandwidth, wastedBandwidth, TER, NCER, CER));
        inputFragment.setInputStream("");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String phrasesCount = sharedPreferences.getString("numberOfPhrases", "5");
        int phrasesCountInt;
        if(!phrasesCount.equals("")) phrasesCountInt = Integer.parseInt(phrasesCount);
        else phrasesCountInt = 5;

        //Je li gotov unos?
        if(numberOfPhrases >= phrasesCountInt) { //provjera je li zadnji unos
            //izradi csv
            //System.out.println("Usao u izradu csva");
            save(view, listOfEntries);

            //prikazi gdje je spremljen csv
            //prikazi pop out da je unos gotov
            ViewDialogOneButton vdob = new ViewDialogOneButton(inputFragment);
            vdob.showDialog();
            //resetiraj broj fraza spremljenih i resetiraj tj izbrisi arraylist
            listOfEntries = new ArrayList<>();
            numberOfPhrases = 1;
            inputFragment.getInput().setText("");

        } else {
            numberOfPhrases++;
            inputFragment.getInput().setText("");

        }
        //postavi sljedeci string ako je prikazano manje od zadanih stringova
            inputFragment.getOutput().setText(col.getRandom());
            inputFragment.setCol(col);
    }

    public void changeVariable(View view) {
        String[] choices = getResources().getStringArray(R.array.metricsArray);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose what you want to compare");

        builder.setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog)dialog).getListView();
                String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
                ChartFragment chartFragment = (ChartFragment) fragment;
                chartFragment.setChosenVariable(checkedItem);
                chartFragment.setFirstChart();
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog d = builder.create();
        d.show();
    }

    public void changeOrientation(View view) {
        String[] choices = getResources().getStringArray(R.array.orientationArray);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose in which orientation you want to compare");

        builder.setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog)dialog).getListView();
                String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
                ChartFragment chartFragment = (ChartFragment) fragment;
                chartFragment.setChosenVariable(checkedItem);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("orientationChart", checkedItem);
                chartFragment.setFirstChart();

            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog d = builder.create();
        d.show();
    }

    public void save(View v, ArrayList<OneEntry> data) {

        generateCSV(this,  listOfEntries);
    }

    public void generateCSV(Context context,  ArrayList<OneEntry> data) {
        FileWriter writer = null;
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "KIST_tests");
            if (!root.exists()) {
                boolean a = root.mkdirs();
                //System.out.println("Directory in making... " + a);
            }
            File file = new File(root, getFileName());
            writer = new FileWriter(file);

            //Boolean wpmBool = sharedPreferences.getBoolean("wpmswitch", true);
            //Boolean bcBool = sharedPreferences.getBoolean("bcswitch", true);
            //Boolean msderBool = sharedPreferences.getBoolean("msderswitch", true);
            //Boolean kspcBool = sharedPreferences.getBoolean("kspcswitch", true);
            StringBuilder sb = new StringBuilder();
            sb.append("Output Input ");
            if(metricSelected("wpmswitch"))sb.append("WPM ");
            if(metricSelected("bcswitch"))sb.append("KSPC ");
            if(metricSelected("kspcswitch"))sb.append("BC ");
            if(metricSelected("msderswitch"))sb.append("MSDER ");
            if(metricSelected("ceswitch"))sb.append("CE ");
            if(metricSelected("pcswitch"))sb.append("PC ");
            if(metricSelected("ubswitch"))sb.append("UB ");
            if(metricSelected("wbswitch"))sb.append("WB ");
            if(metricSelected("terswitch"))sb.append("TER ");
            if(metricSelected("ncerswitch"))sb.append("NCER ");
            if(metricSelected("cerswitch"))sb.append("CER ");
            //metrics to add!!!!
            String[] attributeNames = sb.toString().split(" ");

            //ispisi u datoteku
            printStringArrayToCsvFile(writer, attributeNames);
            for(OneEntry attr : data) {
                float wpm = attr.getWpm();
                int bc = attr.getBc();
                float kspc = attr.getKspc();
                double msder = attr.getEr();
                String input = attr.getTextInput();
                String output = attr.getTextOutput();
                Double correctionEfficiency = attr.getCorrectionEfficiency();
                Double participantConscientiousness = attr.getParticipantConscientiousness();
                Double utilisedBandwidth = attr.getUtilisedBandwidth();
                Double wastedBandwidth = attr.getWastedBandwidth();
                Double TER = attr.getTER();
                Double NCER =  attr.getNCER();
                Double CER = attr.getCER();

                String inputStream = attr.getInputStream();
                //test
                System.out.println("Transcribed text: "+input+"\nPresented Text: "+output+"\nInput stream: "+inputStream+"\nC: "+ConstituentsOfInputStream.getCorrect(input, output) + "\nF: " +
                        ConstituentsOfInputStream.getFixes(inputStream, output) + "\nIF: " +
                        ConstituentsOfInputStream.getIncorrectFixed(inputStream, output)  + "\nINF: " +
                        ConstituentsOfInputStream.getIncorrectNotFixed(input, output) );

                ((InputFragment)fragment).setInputStream("");

                sb = new StringBuilder();
                sb.append(output+"/"+input+ "/");
                if(metricSelected("wpmswitch"))sb.append(wpm+"/");
                if(metricSelected("bcswitch"))sb.append(kspc+"/");
                if(metricSelected("kspcswitch"))sb.append(bc+"/");
                if(metricSelected("msderswitch"))sb.append(msder+"/");
                if(metricSelected("ceswitch"))sb.append(correctionEfficiency+"/");
                if(metricSelected("pcswitch"))sb.append(participantConscientiousness+"/");
                if(metricSelected("ubswitch"))sb.append(utilisedBandwidth+"/");
                if(metricSelected("wbswitch"))sb.append(wastedBandwidth+"/");
                if(metricSelected("terswitch"))sb.append(TER+"/");
                if(metricSelected("ncerswitch"))sb.append(NCER+"/");
                if(metricSelected("cerswitch"))sb.append(CER+"/");

                String[] entries = sb.toString().split("/");
                //ispisi u datoteku
                printStringArrayToCsvFile(writer, entries);
            }
            InputFragment infr = (InputFragment) fragment;
            infr.showToast(file);
            Toast.makeText(this.getApplicationContext(), "Saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getFileName() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String participantID = sharedPreferences.getString("participantID", "");
        String testMark = sharedPreferences.getString("testMark", "");
        String orientation = sharedPreferences.getString("orientation", "");
        String deviceType = sharedPreferences.getString("deviceType", "");
        String screenSize = sharedPreferences.getString("screenSize", "");
        String keyboardType = sharedPreferences.getString("keyboardType", "");

        SimpleDateFormat formatter = new SimpleDateFormat("[dd.MM.HH:mm:ss]");
        Date now = new Date();
        String fileName =  putBrackets(testMark)   + putBrackets(keyboardType)  + putBrackets(participantID) + putBrackets(orientation) + putBrackets(screenSize) + putBrackets(deviceType) + formatter.format(now) + ".csv";
        return fileName;
    }

    public void printStringArrayToCsvFile(Writer writer, String[] array) {
        for(int i = 0; i < array.length - 1; ++i) {
            try {
                //System.out.println("Element "+i+": "+array[i]);
                writer.append(array[i]+",");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.append(array[array.length-1] + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String putBrackets(String value) { return "["+value+"]";
       //else return value;
    }

    public Boolean metricSelected(String key) {
        Boolean selected = sharedPreferences.getBoolean(key, true);
        if(selected) return true;
        else return false;
    }
}
