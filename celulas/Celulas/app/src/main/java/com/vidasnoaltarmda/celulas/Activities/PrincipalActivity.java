package com.vidasnoaltarmda.celulas.Activities;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.UsuarioDAO;
import com.vidasnoaltarmda.celulas.R;

import java.security.Principal;

/**
 * Created by barque on 14/03/2016.
 */


public class PrincipalActivity extends ActionBarActivity implements View.OnClickListener {

    private Button aviso;
    private Button escala;
    private Button roteiro;
    private Button programacao;
    private Button aniversariante;
    private Button ge;
    private Button celula;
    private Button site;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



    }


    @Override
    public void onClick(View view) {

    }


    private Button getAviso() {
        if (aviso == null) {
            aviso = (Button) findViewById(R.id.aviso);
        }
        return aviso;
    }

    private Button getEscala() {
        if (escala == null) {
            escala = (Button) findViewById(R.id.escala);
        }
        return escala;
    }

    private Button getRoteiro() {
        if (roteiro == null) {
            roteiro = (Button) findViewById(R.id.roteiro);
        }
        return roteiro;
    }


    private Button getProgramacao() {
        if (programacao == null) {
            programacao = (Button) findViewById(R.id.programacao);
        }
        return programacao;
    }

    private Button getAniversariante() {
        if (aniversariante == null) {
            aniversariante = (Button) findViewById(R.id.aniversariante);
        }
        return aniversariante;
    }

    private Button getGe() {
        if (ge == null) {
            ge = (Button) findViewById(R.id.ge);
        }
        return ge;
    }

    private Button getCelula() {
        if (celula == null) {
            celula = (Button) findViewById(R.id.celula);
        }
        return celula;
    }

    private Button getSite() {
        if (site == null) {
            site = (Button) findViewById(R.id.site);
        }
        return site;
    }

}