package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dao.CelulaDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;

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

    private Celula celula;
    private Toolbar mToolbar;
    private ImageView imageViewListaVazia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celula);
        celula = Utils.retornaCelulaSharedPreferences(this);

        if (celula != null) {
            montaTelaCelula(celula);
            insereListeners();
        }

        mToolbar = (Toolbar) findViewById(R.id.th_celula);
        mToolbar.setTitle("Célula");
        setSupportActionBar(mToolbar);
    }

    private void insereListeners() {
        getFoto().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.foto:
                Intent intent = new Intent(this, ImagemAmpliadaActivity.class);
                intent.putExtra(ImagemAmpliadaActivity.EXTRA_CAMINHO_IMAGEM, getApplicationContext().getFilesDir().getAbsolutePath() + Celula.DIRETORIO_IMAGENS_CELULA + "/" + Celula.NOME_PADRAO_IMAGEM_CELULA);
                startActivity(intent);
                break;
        }
    }

    private void montaTelaCelula(Celula celula){
        getNome().setText(celula.getNome());
        getLider().setText(celula.getLider());
        getDia().setText(celula.converteDiaCelula());
        getHorario().setText(celula.getHorario());
        getLocal().setText(celula.getLocal_celula());
        getSemana().setText(celula.converteDiaJejum() + " - " + celula.getPeriodo());
        getVersiculo().setText("\"" + celula.getVersiculo() + "\"");
        new mostraImagemCelulaTask().execute();

    }


    private class mostraImagemCelulaTask extends AsyncTask<Void, Void,  Integer>{
        String caminhoImagem;
        ProgressDialog  progressDialog;
        private final int RETORNO_SUCESSO = 0;
        private final int FALHA_SQLEXCEPTION = 1;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            caminhoImagem = getApplicationContext().getFilesDir().getAbsolutePath() + Celula.DIRETORIO_IMAGENS_CELULA;
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(CelulaActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        @Override
        protected  Integer doInBackground(Void... params){
            try {
                if (celula != null)
                    new CelulaDAO().retornaCelulaImagem(celula, caminhoImagem, Celula.NOME_PADRAO_IMAGEM_CELULA);
            } catch(SQLException e){
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;

            }
            return RETORNO_SUCESSO;
        }


        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case RETORNO_SUCESSO:
                    getFoto().setImageBitmap(BitmapFactory.decodeFile(caminhoImagem + "/" + Celula.NOME_PADRAO_IMAGEM_CELULA));
                    break;
                case FALHA_SQLEXCEPTION:
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }

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
    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        }
        return imageViewListaVazia;
    }


}