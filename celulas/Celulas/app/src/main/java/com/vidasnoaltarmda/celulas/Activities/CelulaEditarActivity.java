package com.vidasnoaltarmda.celulas.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

/**
 * Created by thiago on 20/08/2016.
 */
public class CelulaEditarActivity  extends AppCompatActivity {

    private Celula celula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_celula);

        celula = Utils.retornaCelulaSharedPreferences(this);

        ((EditText) findViewById(R.id.edittext_nome_lider)).setText(celula.getLider());
        //((Spinner) findViewById(R.id.data_celula))
        ((EditText) findViewById(R.id.horario_celula)).setText(celula.getHorario());
        ((EditText) findViewById(R.id.edittext_local)).setText(celula.getLocal_celula());
        //((Spinner) findViewById(R.id.edittext_dia_jejum))
        //((Spinner) findViewById(R.id.edittext_dia_semana))
        ((EditText) findViewById(R.id.edittext_versiculo)).setText(celula.getVersiculo());
    }
}
