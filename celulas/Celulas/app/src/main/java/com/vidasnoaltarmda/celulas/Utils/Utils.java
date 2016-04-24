package com.vidasnoaltarmda.celulas.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    // caso nao seja necessaria uma acao de click para o botao passar acao = null
    public static void mostraMensagemDialog(Context contexto, String mensagem, DialogInterface.OnClickListener acao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto)
                .setTitle("Aviso")
                .setMessage(mensagem)
                .setPositiveButton("Ok", acao);
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    //converte a data para enviar para o banco de dados
    public static String coverteDataBanco(String data) {
        String[] campos = data.split("/");
        return campos[2] + "-" + campos[1] + "-" + campos[0];
    }

    //converte a data do banco para poder ser usada no programa
    public static String coverteDataApp(String data) {
        String[] campos = data.split("-");
        return campos[2] + "/" + campos[1] + "/" + campos[0];
    }

    //converte a hora do banco para poder ser usada no programa
    public static String coverteHoraApp(String hora) {
        String[] campos = hora.split(":");
        return campos[0] + ":" + campos[1];
    }

    public static void downloadImagemBanco(String diretorioImagem, InputStream isImagem, String nomeImagem) throws IOException {
        File dirImagem = new File(diretorioImagem);
        if (!dirImagem.exists()) {
            dirImagem.mkdirs();
        }

        OutputStream outputStream = new FileOutputStream(new File(diretorioImagem + "/" + nomeImagem));
        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = isImagem.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        outputStream.close();
        isImagem.close();
    }

    //Salva string "data" nas Shared Preferences com a chave "variable"
    public static void salvaSharedPreference(Context con, String variable, String data)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        prefs.edit().putString(variable, data).commit();
    }

    //Retorna string nas Shared Preferences com a chave "variable"
    public static String retornaSharedPreference(Context con, String variable, String defaultValue)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        String data = prefs.getString(variable, defaultValue);
        return data;
    }

    //limpar dados armazenados nas Shared Preferences
    public static void limpaSharedPreferences(Context con) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        prefs.edit().clear().commit();
    }
}
