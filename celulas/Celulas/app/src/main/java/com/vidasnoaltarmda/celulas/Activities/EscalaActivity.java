package com.vidasnoaltarmda.celulas.Activities;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Escala;
import com.vidasnoaltarmda.celulas.Dados.Escalacao;
import com.vidasnoaltarmda.celulas.Dao.EscalaDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;

/**
 * Created by barque on 14/03/2016.
 */


public class EscalaActivity extends ActionBarActivity {

    private TextView nome;
    private TextView data;
    private TextView horario;
    private TextView local;
    private ListView listview_escala;
    private Toolbar mToolbar;

    private Celula celula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escala);

        //TODO verificar necessidade de entrar na tela caso não hajam escalas (ou mostrar mensagem que nao existem escalas)
        //TODO verificar se existe a necessidade de buscar a celula ao abrir cada tela ou se carregar ao abrir o programa é suficiente
        celula = Utils.retornaCelulaSharedPreferences(this);
        new MontaTelaEscalasTask().execute();

        mToolbar = (Toolbar) findViewById(R.id.th_escala);
        mToolbar.setTitle("Escala");
        setSupportActionBar(mToolbar);

    }

    private ListView getListViewEscala() {
        if (listview_escala == null) {
            listview_escala = (ListView) findViewById(R.id.listview_escala);
        }
        return listview_escala;
    }

    private class  MontaTelaEscalasTask extends AsyncTask<Void, Void, Integer> {
        Escala escala;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0;
        private final int FALHA_SQLEXCEPTION = 1;


        @Override
        protected Integer doInBackground(Void... params) {
            try {
                escala = new EscalaDAO().retornaEscala(celula);
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //TODO LOG ERRO
            }
            return RETORNO_SUCESSO;
        }



          @Override
        protected void onPreExecute() {
            super.onPreExecute();
            escala = new Escala();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(EscalaActivity.this, "Carregando escala", "Aguarde por favor...", true);
        }



        @Override
        protected void onPostExecute(Integer resultadoEscala) {
            progressDialog.dismiss();
            switch (resultadoEscala) {
                case RETORNO_SUCESSO:
                    if (celula != null && escala != null) {
                        //preenche dados padrao da escala
                        getNome().setText(celula.getNome());
                        getData().setText(escala.getData_celula() + " - " + escala.getHora_celula());
                        getLocal().setText(escala.getLocal_celula());
                        //--preenche dados padrao da escala
                        //TODO colocar mensagem quando não houverem avisos
                        getListViewEscala().setAdapter(new ArrayAdapter<Escalacao>(EscalaActivity.this, R.layout.custom_list_item_3, escala.getEscalacoes()));
                    }
                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar a escala, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                    Utils.mostraMensagemDialog(EscalaActivity.this, "Não foi possível carregar a escala. Verifique sua conexão e tente novamente.");
                    break;
            }
            super.onPostExecute(resultadoEscala);
        }


    }

    public TextView getData() {
        if (data == null) {
            data = (TextView) findViewById(R.id.data);
        }
        return data;
    }

    public TextView getNome() {
        if (nome == null) {
            nome = (TextView) findViewById(R.id.nome);
        }
        return nome;
    }

    public TextView getLocal() {
        if (local == null) {
            local = (TextView) findViewById(R.id.local);
        }
        return local;
    }



}