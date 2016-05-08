package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Programacao;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.ProgramacaoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProgramacaoActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    public static final int REQUEST_SALVAR = 1;
    public static final String PROGRAMACAO_SELECIONADA = "programacao_selecionada";
    private ListView listview_programacoes;

    private Celula celula;
    private Toolbar mToolbar;

    private ImageView imageViewListaVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao);

        celula = Utils.retornaCelulaSharedPreferences(this);
        new PopulaProgramacoesTask().execute();
        insereListeners();

        mToolbar = (Toolbar) findViewById(R.id.th_programacao);
        mToolbar.setTitle("Programações");
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SALVAR && resultCode == RESULT_OK) {
            new PopulaProgramacoesTask().execute((Runnable) celula);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int permissaoUsuario = 0;
        try {
            permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            getMenuInflater().inflate(R.menu.menu_programacao, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_adicionar) {
            Intent intent = new Intent(this, FormProgramacaoActivity.class);
            startActivityForResult(intent, REQUEST_SALVAR);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                programacoes = new ProgramacaoDAO().retornaProgramacoes(celula);
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //NaoExiste.setVisibility(View.VISIBLE);//TODO Fazer aparecer :( (Não consegui)
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
                    if (programacoes.size() > 0) {
                        getImageViewListaVazia().setVisibility(View.GONE);
                        getListViewProgramacao().setVisibility(View.VISIBLE);
                    }else {
                        getImageViewListaVazia().setVisibility(View.VISIBLE);
                        getListViewProgramacao().setVisibility(View.GONE);
                    }
                    getListViewProgramacao().setAdapter(new ArrayAdapter<Programacao>(ProgramacaoActivity.this, R.layout.custom_list_item_3, programacoes));
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
    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        }
        return imageViewListaVazia;
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
