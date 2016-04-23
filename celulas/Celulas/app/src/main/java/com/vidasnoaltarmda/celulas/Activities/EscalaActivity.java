package com.vidasnoaltarmda.celulas.Activities;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Escala;
import com.vidasnoaltarmda.celulas.Dao.EscalaDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 14/03/2016.
 */


public class EscalaActivity extends ActionBarActivity {

    private TextView data;
    private TextView horario;
    private TextView local;
    private ListView listview_escala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escala);

        //TODO receber a celula como parametro, verificar possibilidade de ser guardada na sessao
        //teste
        Celula celula = new Celula();
        celula.setId_celula(8);
        new MontaTelaEscalasTask().execute(celula);

    }

    private ListView getListViewEscala() {
        if (listview_escala == null) {
            listview_escala = (ListView) findViewById(R.id.listview_escala);
        }
        return listview_escala;
    }

    private class  MontaTelaEscalasTask extends AsyncTask<Celula, Void, Integer> {
        ArrayList<Escala> escalas;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0;
        private final int FALHA_SQLEXCEPTION = 1;


        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {
                escalas = new EscalaDAO().retornaEscalas(celulas[0]);
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
            escalas = new ArrayList<Escala>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(EscalaActivity.this, "Carregando escala", "Aguarde por favor...", true);
        }



        @Override
        protected void onPostExecute(Integer resultadoEscala) {
            progressDialog.dismiss();
            switch (resultadoEscala) {
                case RETORNO_SUCESSO:
                    if (escalas.size() > 0) {
                        //preenche dados padrao da escala
                        Escala escala = escalas.get(0);
                        getData().setText(escala.getData_celula());
                        getHorario().setText(escala.getHora_celula());
                        getLocal().setText(escala.getLocal_celula());
                        //--preenche dados padrao da escala
                        //TODO colocar mensagem quando não houverem avisos
                        getListViewEscala().setAdapter(new ArrayAdapter<Escala>(EscalaActivity.this, R.layout.custom_list_item_2, escalas));
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

    public TextView getLocal() {
        if (local == null) {
            local = (TextView) findViewById(R.id.local);
        }
        return local;
    }

    public TextView getHorario() {
        if (horario == null) {
            horario = (TextView) findViewById(R.id.horario);
        }
        return horario;
    }



}