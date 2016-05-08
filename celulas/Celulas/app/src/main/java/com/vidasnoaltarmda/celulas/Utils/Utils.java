package com.vidasnoaltarmda.celulas.Utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Usuario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    //converte data aniversário, prevalecendo somente o dia
    public static String coverteDataNiver(String dia) {
        String[] campo = dia.split("-");
        return campo[2];
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

    //Salva dados celula nas Shared Preferences
    public static void salvaCelulaSharedPreference(Context con, Celula celula)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        prefs.edit().putInt   (Celula.ID_CELULA_SP            , celula.getId_celula())
                    .putString(Celula.NOME_CELULA_SP          , celula.getNome())
                    .putString(Celula.LIDER_CELULA_SP, celula.getLider())
                    .putString(Celula.DIA_CELULA_SP, celula.getDia())
                    .putString(Celula.HORARIO_CELULA_SP       , celula.getHorario())
                    .putString(Celula.LOCAL_CELULA_SP         , celula.getLocal_celula())
                    .putString(Celula.DIA_JEJUM_CELULA_SP     , celula.getDia_jejum())
                    .putString(Celula.PERIODO_CELULA_SP, celula.getPeriodo())
                    .putString(Celula.VERSICULO_CELULA_SP, celula.getVersiculo()).commit();
                    //.putString(Celula.CAMINHO_IMAGEM_CELULA_SP, data).commit(); TODO salvar caminho do cache da imagem
    }

    //Salva dados usuario nas Shared Preferences
    public static void salvaUsuarioSharedPreference(Context con, Usuario usuario)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        prefs.edit().putInt(Usuario.ID_USUARIO_SP, usuario.getId())
                    .putString(Usuario.NOME_SP                 , usuario.getNome())
                    .putString(Usuario.SOBRENOME_SP            , usuario.getSobrenome())
                    .putString(Usuario.DATA_NASCIMENTO_SP      , usuario.getDataNascimento())
                    .putString(Usuario.LOGIN_SP                , usuario.getLogin())
                    .putInt(Usuario.PERMISSAO_SP, usuario.getPermissao())
                    .commit();
    }

    //Retorna dados celula nas Shared Preferences
    public static Celula retornaCelulaSharedPreferences(Context con) {
        Celula celula = new Celula();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        celula.setId_celula(prefs.getInt(Celula.ID_CELULA_SP, -1));
        celula.setNome(prefs.getString(Celula.NOME_CELULA_SP, null));
        celula.setLider(prefs.getString(Celula.LIDER_CELULA_SP, null));
        celula.setDia(prefs.getString(Celula.DIA_CELULA_SP, null));
        celula.setHorario(prefs.getString(Celula.HORARIO_CELULA_SP, null));
        celula.setLocal_celula(prefs.getString(Celula.LOCAL_CELULA_SP, null));
        celula.setDia_jejum(prefs.getString(Celula.DIA_JEJUM_CELULA_SP, null));
        celula.setPeriodo(prefs.getString(Celula.PERIODO_CELULA_SP, null));
        celula.setVersiculo(prefs.getString(Celula.VERSICULO_CELULA_SP, null));
        //.putString(Celula.CAMINHO_IMAGEM_CELULA_SP, data).commit(); TODO salvar caminho do cache da imagem

        return celula;
    }

    //Retorna dados usuario nas Shared Preferences
    public static Usuario retornaUsuarioSharedPreference(Context con)
    {
        Usuario usuario = null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        if (prefs.getString(Usuario.LOGIN_SP, null) != null) {
            usuario = new Usuario();
            usuario.setId(prefs.getInt(Usuario.ID_USUARIO_SP, -1));
            usuario.setNome(prefs.getString(Usuario.NOME_SP, null));
            usuario.setSobrenome(prefs.getString(Usuario.SOBRENOME_SP, null));
            usuario.setDataNascimento(prefs.getString(Usuario.DATA_NASCIMENTO_SP, null));
            usuario.setLogin(prefs.getString(Usuario.LOGIN_SP, null));
            usuario.setPermissao(prefs.getInt(Usuario.PERMISSAO_SP, -1));
        }

        return usuario;
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

    public static void mostraDatePickerDialog(Context context, final EditText campoTexto) {
        final Calendar calendar;
        //Prepara data anterior caso ja tenha sido selecionada
        if (campoTexto.getTag() != null) {
            calendar = ((Calendar) campoTexto.getTag());
        } else {
            calendar = Calendar.getInstance();
        }
        //----

        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                campoTexto.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
                campoTexto.setTag(newDate);
            }

        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void mostraTimePickerDialog(Context context, final EditText campoTexto) {
        final Calendar calendar;
        //Prepara hora anterior caso ja tenha sido selecionada
        if (campoTexto.getTag() != null) {
            calendar = ((Calendar) campoTexto.getTag());
        } else {
            calendar = Calendar.getInstance();
        }
        //----

        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(Calendar.HOUR_OF_DAY, hour);
                newDate.set(Calendar.MINUTE, minute);
                campoTexto.setText(new SimpleDateFormat("HH:mm").format(newDate.getTime()));
                campoTexto.setTag(newDate);
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show();
    }
}
