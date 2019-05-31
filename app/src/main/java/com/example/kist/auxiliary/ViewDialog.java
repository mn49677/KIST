package com.example.kist.auxiliary;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kist.R;
import com.example.kist.fragments.SavedFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ViewDialog {
    private String filename;
    private SavedFragment savedFragment;

    public ViewDialog(String filename, SavedFragment savedFragment) {
        this.filename = filename;
        this.savedFragment = savedFragment;
    }

    public void showDialog(final Fragment fragment) {

        final Dialog dialog = new Dialog(fragment.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        FrameLayout mDialogNo = dialog.findViewById(R.id.frmNot);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(fragment.getContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        FrameLayout mDialogOk = dialog.findViewById(R.id.frmYes);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File root = Environment.getExternalStorageDirectory();
                File directory = new File(root, "KIST_tests");
                File csv = new File(directory, filename);
                if(csv.exists()) {
                    System.out.println("Usao u brisanje!!");
                    Boolean deleted = csv.delete();
                    if(deleted.equals(true)) Toast.makeText( fragment.getContext(), "File " + filename + " deleted" ,Toast.LENGTH_SHORT).show();
                    ListView lv = savedFragment.getActivity().findViewById(R.id.ListView);

                    ArrayList<String> listaCSV = new ArrayList<>();
                    for (File f : directory.listFiles()) {
                        if (f.isFile()) {
                            listaCSV.add(f.getName());
                        }
                    }
                    String[] array = Arrays.copyOf(listaCSV.toArray(), listaCSV.toArray().length, String[].class);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(savedFragment.getActivity(), R.layout.listview_element);
                    adapter.addAll(array);

                    lv = savedFragment.getSavedView().findViewById(R.id.ListView);
                    lv.setAdapter(adapter);
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
