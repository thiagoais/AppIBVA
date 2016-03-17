package com.vidasnoaltarmda.celulas.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vidasnoaltarmda.celulas.R;

/**
 * Created by barque on 14/03/2016.
 */


public class CelulaActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageView foto;
    private TextView dia;
    private TextView horario;
    private TextView local;
    private TextView jejum;
    private TextView versiculo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escala);


    }


    @Override
    public void onClick(View view) {

    }

    private ImageView getFoto() {
        if (foto == null) {
            foto = (ImageView) findViewById(R.id.foto);
        }
        return foto;
    }

    private TextView getDia() {
        if (dia == null) {
            dia = (TextView) findViewById(R.id.dia);
        }
        return dia;
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

    private TextView getJejum() {
        if (jejum == null) {
            jejum = (TextView) findViewById(R.id.jejum);
        }
        return jejum;
    }

    private TextView getVersiculo() {
        if (versiculo == null) {
            versiculo = (TextView) findViewById(R.id.versiculo);
        }
        return versiculo;
    }


}