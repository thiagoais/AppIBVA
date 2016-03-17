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
}
