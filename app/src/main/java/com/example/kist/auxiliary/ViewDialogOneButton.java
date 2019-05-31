package com.example.kist.auxiliary;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.kist.R;
import com.example.kist.fragments.InputFragment;

public class ViewDialogOneButton {
    private InputFragment inputFragment;

    public ViewDialogOneButton(InputFragment inputFragment) {
        this.inputFragment = inputFragment;
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(inputFragment.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.input_done_dialog_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = inputFragment.getLayoutInflater();
        View v = inflater.inflate(R.layout.input_done_dialog_view, null);

        FrameLayout mDialogOk = dialog.findViewById(R.id.frame);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}

