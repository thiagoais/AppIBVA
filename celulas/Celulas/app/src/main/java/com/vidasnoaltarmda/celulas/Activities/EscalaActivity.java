package com.vidasnoaltarmda.celulas.Activities;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.UsuarioDAO;
import com.vidasnoaltarmda.celulas.R;

import java.security.Principal;

/**
 * Created by barque on 14/03/2016.
 */


public class EscalaActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView nome;
    private TextView data;
    private TextView horario;
    private TextView local;
    private ListView escala;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escala);



    }


    @Override
    public void onClick(View view) {

    }

    private TextView getNome() {
        if (nome == null) {
            nome = (TextView) findViewById(R.id.nome);
        }
        return nome;
    }

    private TextView getData() {
        if (data == null) {
            data = (TextView) findViewById(R.id.data);
        }
        return data;
    }

    private TextView getHorario() {
        if (horario == null) {
            horario = (TextView) findViewById(R.id.horario);
        }
        return horario;
    }

    private TextView getLocal() {
        if (local == null) {
            local = (TextView) findViewById(R.id.local);
        }
        return local;
    }

    private ListView getEscala() {
        if (escala == null) {
            escala = (ListView) findViewById(R.id.escala);
        }
        return escala;
    }


}