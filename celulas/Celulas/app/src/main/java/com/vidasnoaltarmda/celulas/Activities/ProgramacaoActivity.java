package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vidasnoaltarmda.celulas.Dados.Programacao;
import com.vidasnoaltarmda.celulas.Dao.ProgramacaoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProgramacaoActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public static final String PROGRAMACAO_SELECIONADA = "programacao_selecionada";
    private ListView listview_programacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao);

        new PopulaProgramacoesTask().execute();
        insereListeners();
    }

    //metodo responsável por buscar os dados das programacoes no banco (acesso remoto) e popular a lista de programacoes.
    private class PopulaProgramacoesTask extends AsyncTask<Void, Void, Integer> {
        ArrayList<Programacao> programacoes;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            programacoes = new ArrayList<Programacao>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(ProgramacaoActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                programacoes = new ProgramacaoDAO().retornaProgramacoes();
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //TODO LOG ERRO
            }
            return RETORNO_SUCESSO;
        }

        //metodo executado apos finalizacao do metodo doInBackground. Sendo assim ja e possivel usar a lista de roteiros
        // retornada
        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case RETORNO_SUCESSO:
                    //TODO colocar mensagem quando não houverem programacoes
                    getListViewProgramacao().setAdapter(new ArrayAdapter<Programacao>(ProgramacaoActivity.this, android.R.layout.simple_list_item_1, programacoes));
                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar as programacoes, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                    Utils.mostraMensagemDialog(ProgramacaoActivity.this, "Não foi possível carregar as programações. Verifique sua conexão e tente novamente.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }
    }

    private void insereListeners() {
        getListViewProgramacao().setOnItemClickListener(this);
    }

    private ListView getListViewProgramacao() {
        if (listview_programacoes == null) {
            listview_programacoes = (ListView) findViewById(R.id.listview_programacoes);
        }
        return listview_programacoes;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        switch (adapterView.getId()) {
            case R.id.listview_programacoes:
                Programacao programacao = (Programacao) adapterView.getItemAtPosition(pos);
                Intent intent = new Intent(this, ProgramacaoSelecionadaActivity.class);
                intent.putExtra(PROGRAMACAO_SELECIONADA, programacao);
                startActivity(intent);
                break;
        }
    }

}