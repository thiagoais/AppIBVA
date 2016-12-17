package com.example.barque.estudos.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barque.estudos.R;

/**
 * Created by Barque on 24/11/2016.
 */

public class Util {
    public static void showMessageToast(Activity activity, String texto) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View lytToast = inflater.inflate(R.layout.meu_toast, (ViewGroup) activity.findViewById(R.id.layoutToast));

        TextView txtToast = (TextView) lytToast.findViewById(R.id.txtToast);
        txtToast.setText(texto);

        Toast toast = new Toast(activity);
        toast.setView(lytToast);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void showMsgAlertOK(Activity activity, String titulo, String texto){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(texto);
        alertDialog.setIcon(R.drawable.hamster);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.show();

    }


}
