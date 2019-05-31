package com.example.kist.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.example.kist.Addons.ConstituentsOfInputStream;
import com.example.kist.auxiliary.PhrasesCollection;
import com.example.kist.R;
import com.example.kist.auxiliary.Timer;
import com.example.kist.metrics.BC;
import java.io.File;
import java.io.IOException;

public class InputFragment extends Fragment {
    public View inputView;
    private EditText input;
    private EditText output;
    private Boolean firstTime = true;
    private Timer timer = new Timer();
    private BC bc;
    private int inputStreamCounter = 0;
    private String lastEntry;
    private String inputStream = "";

    public PhrasesCollection getCol() {
        return col;
    }

    public void setCol(PhrasesCollection col) {
        this.col = col;
    }

    PhrasesCollection col;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //1.Inicijalizacija input text boxa i output text boxa
        inputView = inflater.inflate(R.layout.input_layout, container, false);
        input = inputView.findViewById(R.id.text_input);
        output =  inputView.findViewById(R.id.text_output);
        bc = new BC();
        lastEntry = "";
        //2.§
        output.setKeyListener(null);
        try {
            this.col = new PhrasesCollection(getActivity().getAssets().open("phrases.txt"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        output.setText(col.getRandom());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            inputView.findViewById(R.id.button).setElevation(5);
            inputView.setElevation(0);
            inputView.setTranslationZ(0);
        }
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(firstTime.equals(true)){
                    timer.startTimer();
                    firstTime = false;

                }
                //System.out.println("Text changed!");
                String pom = lastEntry;
                if(lastEntry.length() > input.getText().length()) {
                    bc.increaseBC();
                    inputStream+="<";
                    //System.out.println("Bc increased to " + bc.getBackspaceCount());
                }
                lastEntry = input.getText().toString();
                inputStreamCounter++;
                if(lastEntry.length() > pom.length()) {
                    ConstituentsOfInputStream.Pair pair = ConstituentsOfInputStream.diff(lastEntry, pom);
                    String addedChar = (String)pair.first;
                    inputStream = inputStream+addedChar;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ConstituentsOfInputStream.Pair<String> p = ConstituentsOfInputStream.diff("th quix<ck brpown","the quick brown");
        System.out.println(p.toString());
        return inputView;
    }

    //Timer getter
    public Timer getTimer() {
        return timer;
    }
    //Output getter
    public EditText getOutput() { return output; }
    //input getter
    public EditText getInput() { return input; }
    //BC getter
    public BC getBc() { return bc; }
    //button boolean setter
    public void setFirstTimeToTrue() { this.firstTime = true; }
    //inputStreamCount getter
    public int getInputStreamCounter() { return inputStreamCounter; }
    //inputStream getter setter

    public String getInputStream() {
        return inputStream;
    }

    public void setInputStream(String inputStream) {
        this.inputStream = inputStream;
    }

    //reset inputStreamCount
    public void resetInputStreamCounter() {
        this.inputStreamCounter = 0;
    }

    public void setLastEntry(String lastEntry) {
        this.lastEntry = lastEntry;
    }

    public void showToast(File file) {
        Toast.makeText(getContext(), "Saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
    }
}
