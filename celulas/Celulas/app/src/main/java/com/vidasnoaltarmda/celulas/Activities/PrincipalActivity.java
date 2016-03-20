package com.vidasnoaltarmda.celulas.Activities;

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
    private ImageButton roteiro;
    private ImageButton programacao;
    private ImageButton aniversariante;
    private ImageButton ge;
    private ImageButton celula;
    private ImageButton site;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        getAviso().setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aviso:
                Toast.makeText(PrincipalActivity.this, "Funciona!", Toast.LENGTH_SHORT).show();
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

    private ImageButton getRoteiro() {
        if (roteiro == null) {
            roteiro = (ImageButton) findViewById(R.id.roteiro);
        }
        return roteiro;
    }


    private ImageButton getProgramacao() {
        if (programacao == null) {
            programacao = (ImageButton) findViewById(R.id.programacao);
        }
        return programacao;
    }

    private ImageButton getAniversariante() {
        if (aniversariante == null) {
            aniversariante = (ImageButton) findViewById(R.id.aniversariante);
        }
        return aniversariante;
    }

    private ImageButton getGe() {
        if (ge == null) {
            ge = (ImageButton) findViewById(R.id.ge);
        }
        return ge;
    }

    private ImageButton getCelula() {
        if (celula == null) {
            celula = (ImageButton) findViewById(R.id.celula);
        }
        return celula;
    }

    private ImageButton getSite() {
        if (site == null) {
            site = (ImageButton) findViewById(R.id.site);
        }
        return site;
    }

}