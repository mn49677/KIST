package com.example.kist.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.os.Environment;

import com.example.kist.R;
import com.example.kist.auxiliary.ViewDialog;


public class SavedFragment extends Fragment {
    private ListView lv;
    private View savedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedView = inflater.inflate(R.layout.saved_layout,container, false);
        File root = Environment.getExternalStorageDirectory();
        File directory = new File(root, "KIST_tests");
        if(!directory.exists()) {
            directory.mkdirs();
        }
        ArrayList<String> listaCSV = new ArrayList<>();
        for(File f : directory.listFiles()) {
            listaCSV.add(f.getName());
        }

        Collections.sort(listaCSV);
        String[] array = Arrays.copyOf(listaCSV.toArray(), listaCSV.toArray().length, String[].class);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.listview_element);
        adapter.addAll(array);

        lv = savedView.findViewById(R.id.ListView);
        lv.setAdapter(adapter);
        final SavedFragment f = this;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String val =(String) parent.getItemAtPosition(position);
                System.out.println("Value is "+val);
                ViewDialog dialog = new ViewDialog(val,  f);
                dialog.showDialog(f);
            }
         });
        return savedView;
    }

    public View getSavedView() {
        return savedView;
    }
}

