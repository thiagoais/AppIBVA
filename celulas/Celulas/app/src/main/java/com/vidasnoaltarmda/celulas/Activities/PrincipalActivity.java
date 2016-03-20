package com.vidasnoaltarmda.celulas.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.R;

/**
 * Created by barque on 14/03/2016.
 */


public class PrincipalActivity extends ActionBarActivity implements View.OnClickListener {

    private LinearLayout aviso;
    private LinearLayout escala;
    private LinearLayout roteiro;
    private LinearLayout programacao;
    private LinearLayout aniversariante;
    private LinearLayout ge;
    private LinearLayout celula;
    private LinearLayout site;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        getAviso().setOnClickListener(this);
        getSite().setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aviso:
                Toast.makeText(PrincipalActivity.this, "Funciona!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.site:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vidasnoaltarmda.com/"));
                startActivity(browserIntent);
                break;
        }
    }

    private LinearLayout getAviso() {
        if (aviso == null) {
            aviso = (LinearLayout) findViewById(R.id.aviso);
        }
        return aviso;
    }

    private LinearLayout getEscala() {
        if (escala == null) {
            escala = (LinearLayout) findViewById(R.id.escala);
        }
        return escala;
    }

    private LinearLayout getRoteiro() {
        if (roteiro == null) {
            roteiro = (LinearLayout) findViewById(R.id.roteiro);
        }
        return roteiro;
    }


    private LinearLayout getProgramacao() {
        if (programacao == null) {
            programacao = (LinearLayout) findViewById(R.id.programacao);
        }
        return programacao;
    }

    private LinearLayout getAniversariante() {
        if (aniversariante == null) {
            aniversariante = (LinearLayout) findViewById(R.id.aniversariante);
        }
        return aniversariante;
    }

    private LinearLayout getGe() {
        if (ge == null) {
            ge = (LinearLayout) findViewById(R.id.ge);
        }
        return ge;
    }

    private LinearLayout getCelula() {
        if (celula == null) {
            celula = (LinearLayout) findViewById(R.id.celula);
        }
        return celula;
    }

    private LinearLayout getSite() {
            if (site == null) {
                site = (LinearLayout) findViewById(R.id.site);
            }


        return site;
    }

}