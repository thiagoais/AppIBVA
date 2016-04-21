package com.vidasnoaltarmda.celulas.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;

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
        //TODO implementar selector para efeito de click botoes tela principal
        getAviso().setOnClickListener(this);
        getEscala().setOnClickListener(this);
        getSite().setOnClickListener(this);
        getRoteiro().setOnClickListener(this);
        getProgramacao().setOnClickListener(this);
        getAniversariante().setOnClickListener(this);
        getGe().setOnClickListener(this);
        getCelula().setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aviso:
                Intent intentAviso = new Intent(this, AvisoActivity.class);
                startActivity(intentAviso);
                break;
            case R.id.escala:
                Intent intentEscala = new Intent(this, EscalaActivity.class);
                startActivity(intentEscala);
                break;
            case R.id.roteiro:
                Intent intentRoteiro = new Intent(this, RoteiroActivity.class);
                startActivity(intentRoteiro);
                break;

            case R.id.programacao:
                Intent intentProgramacao = new Intent(this, ProgramacaoActivity.class);
                startActivity(intentProgramacao);
                break;

            case R.id.aniversariante:
                Intent intentAniversariante = new Intent(this, AniversariantesActivity.class);
                startActivity(intentAniversariante);
                break;

            case R.id.ge:
                Intent intentGE = new Intent(this, GEActivity.class);
                startActivity(intentGE);
                break;

            case R.id.celula:
                Intent intentCelula = new Intent(this, CelulaActivity.class);
                startActivity(intentCelula);
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