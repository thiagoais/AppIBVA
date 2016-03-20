package com.vidasnoaltarmda.celulas.Utils;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by thiago on 16/03/2016.
 */
public class Utils {
    public static void mostraMensagemDialog(Context contexto, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto)
                .setTitle("Aviso")
                .setMessage(mensagem)
                .setPositiveButton("Ok", null);
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    public static String coverteDataBanco(String data) {
        String[] campos = data.split("/");
        return campos[2] + "-" + campos[1] + "-" + campos[0];
    }
}
