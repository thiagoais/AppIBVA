package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.R;

/**
 * Created by barque on 14/03/2016.
 */


public class CelulaActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView nome;
    private TextView lider;
    private ImageView foto;
    private TextView dia;
    private TextView horario;
    private TextView local;
    private TextView semana;
    private TextView periodo;
    private TextView versiculo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celula);
        montaTelaCelula(celula);

    }

    private void montaTelaCelula(Celula celula){
        getNome().setText(celula.getNome());
        getLider().setText(celula.getLider());
        getFoto().setImageResource(celula.getImagem());//TODO tem que ver como vai trazer essa imagem
        getDia().setText(celula.getDia());
        getHorario().setText(celula.getHorario());
        getLocal().setText(celula.getLocal_celula());
        getSemana().setText(celula.getDia_jejum());
        getPeriodo().setText(celula.getPeriodo());
        getVersiculo().setText(celula.getVersiculo());
        new mostraImagemCelulaTask().execute(celula);
    }

    //método que vai buscar a imagem da célula
    //TODO descobrir como mostra essa imagem ABENÇOADA
    private class mostraImagemCelulaTask extends AsyncTask<Celula, Void,  Integer>{
    String caminhoImagem;
    ProgressDialog  progressDialog;
        private final int RETORNO_SUCESSO = 0;
        private final int FALHA_SQLEXCEPTION = 1;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            caminhoImagem = getApplicationContext().getFilesDir().getAbsolutePath() + Celula.DIRETORIO_IMAGENS_CELULA;//parei aqui depois continuo
        }

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

    private TextView getLider() {
        if (lider == null) {
            lider = (TextView) findViewById(R.id.lider);
        }
        return lider;
    }

    private TextView getPeriodo() {
        if (periodo == null) {
            periodo = (TextView) findViewById(R.id.periodo);
        }
        return periodo;
    }

    private TextView getNome() {
        if (nome == null) {
            nome = (TextView) findViewById(R.id.nome);
        }
        return nome;
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

    private TextView getSemana() {
        if (semana == null) {
            semana = (TextView) findViewById(R.id.semana);
        }
        return semana;
    }

    private TextView getVersiculo() {
        if (versiculo == null) {
            versiculo = (TextView) findViewById(R.id.versiculo);
        }
        return versiculo;
    }


}