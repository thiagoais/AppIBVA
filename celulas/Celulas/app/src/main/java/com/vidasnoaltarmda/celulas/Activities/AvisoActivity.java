package com.vidasnoaltarmda.celulas.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vidasnoaltarmda.celulas.Dados.Aviso;
import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dao.AvisoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 24/03/2016.
 */
public class AvisoActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private ListView listview_avisos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);

        //TODO receber a celula como parametro, verificar possibilidade de ser guardada na sessao
        //teste
        Celula celula = new Celula();
        celula.setId_celula(8);

        //teste
        new PopulaAvisosTask().execute(celula);
        insereListeners();
    }

    private void insereListeners() {
        getListViewAviso().setOnItemClickListener(this);
    }

    private ListView getListViewAviso() {
        if (listview_avisos == null) {
            listview_avisos = (ListView) findViewById(R.id.avisoslist);
        }
        return listview_avisos;
    }

    private class PopulaAvisosTask extends AsyncTask<Celula, Void, Integer> {//desisto kkkk ja fiz bastante arruma ai pra nois //kkkkk blz
        ArrayList<Aviso> avisos;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {
                if(celulas.length > 0){
                    avisos = new AvisoDAO().retornaAvisos(celulas[0]);
                }
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
            avisos = new ArrayList<Aviso>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(AvisoActivity.this, "Carregando avisos", "Aguarde por favor...", true);
        }

        //metodo executado apos finalizacao do metodo doInBackground. Sendo assim ja e possivel usar a lista de avisos
        // retornada
        @Override
        protected void onPostExecute(Integer resultadoAviso) {
            progressDialog.dismiss();
        switch (resultadoAviso) {
            case RETORNO_SUCESSO:
                //TODO colocar mensagem quando não houverem avisos
                getListViewAviso().setAdapter(new ArrayAdapter<Aviso>(AvisoActivity.this, R.layout.custom_list_item_3, avisos));
                break;
            case FALHA_SQLEXCEPTION:
                //nao foi possivel carregar os avisos, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                Utils.mostraMensagemDialog(AvisoActivity.this, "Não foi possível carregar os avisos. Verifique sua conexão e tente novamente.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                break;
        }
        super.onPostExecute(resultadoAviso);
    }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        Aviso avisoSelecionado = (Aviso) adapterView.getItemAtPosition(pos);
        switch (adapterView.getId()) {
            case R.id.avisoslist:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(avisoSelecionado.getTitulo())
                        .setMessage(avisoSelecionado.getConteudo())
                        .setPositiveButton("OK", null);
                AlertDialog alerta = builder.create();
                alerta.show();
                break;
        }
    }



}


