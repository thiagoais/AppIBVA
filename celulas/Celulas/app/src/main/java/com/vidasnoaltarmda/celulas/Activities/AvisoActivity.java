package com.vidasnoaltarmda.celulas.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Aviso;
import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.AvisoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.AdapterDelete;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 24/03/2016.
 */
public class AvisoActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    public static final int REQUEST_SALVAR = 1;

    private static final String STATE_LISTA_AVISOS = "STATE_LISTA_AVISOS";

    private ImageView imageViewListaVazia;
    private ListView listview_avisos;
    private Toolbar mToolbar;

    private ArrayList<Aviso> mListaAvisos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);

        if (savedInstanceState == null) {
            new PopulaAvisosTask().execute(getSPCelula());
        } else {
            if (savedInstanceState.get(STATE_LISTA_AVISOS) != null) {
                mListaAvisos = (ArrayList<Aviso>) savedInstanceState.get(STATE_LISTA_AVISOS);
                getListViewAviso().setAdapter(new AdapterDelete<Aviso>(this, mListaAvisos));
            }
        }
        insereListeners();
        mToolbar = (Toolbar) findViewById(R.id.th_aviso);
        mToolbar.setTitle("Avisos");
        setSupportActionBar(mToolbar);
    }

    private Celula getSPCelula() {
        return Utils.retornaCelulaSharedPreferences(this);
    }

    @Override
    public void onSaveInstanceState(Bundle estadoDeSaida) { //TODO fazer tratamento de giro de tela nas outras telas
        super.onSaveInstanceState(estadoDeSaida);
        if (getListViewAviso().getAdapter() != null) {
            estadoDeSaida.putSerializable(STATE_LISTA_AVISOS, mListaAvisos);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SALVAR && resultCode == RESULT_OK) {
            new PopulaAvisosTask().execute(getSPCelula());
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
            getMenuInflater().inflate(R.menu.menu_avisos, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_adicionar) {
            Intent intent = new Intent(this, FormAvisoActivity.class);
            startActivityForResult(intent, REQUEST_SALVAR);
            getListViewAviso().setChoiceMode(getListViewAviso().getChoiceMode()); //Acerto para cancelar o modo de selecao da lista quando o usuario entra na insercao de avisos
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insereListeners() {
        int permissaoUsuario = 0;
        permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));

        getListViewAviso().setOnItemClickListener(this);
        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
        getListViewAviso().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            getListViewAviso().setSelected(true);
        }



        getListViewAviso().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selectionCounter;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((AdapterDelete) getListViewAviso().getAdapter()).limpaItensSelecionados();
                selectionCounter = 0;


            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_deletar:
                        new RemoveAvisoTask(
                                ((AdapterDelete<Aviso>) getListViewAviso().getAdapter()).getItensSelecionados(),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        mode.finish();
                                    }
                                }
                        ).execute();
                        return true;
                    default:
                        return false;
                }

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                if (checked) {
                    selectionCounter++;
                    ((AdapterDelete) getListViewAviso().getAdapter()).selectedItem(position, position);

                } else {
                    selectionCounter--;
                    ((AdapterDelete)getListViewAviso().getAdapter()).removeSelection(position);
                }
                if (selectionCounter > 1){
                    mode.setTitle(selectionCounter + " Selecionados");
                }else{
                    mode.setTitle(selectionCounter + " Selecionado");
                }

            }
        });
    }

    private ListView getListViewAviso() {
        if (listview_avisos == null) {
            listview_avisos = (ListView) findViewById(R.id.avisoslist);
        }
        return listview_avisos;
    }

    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        }
        return imageViewListaVazia;
    }

    private class PopulaAvisosTask extends AsyncTask<Celula, Void, Integer> {//desisto kkkk ja fiz bastante arruma ai pra nois //kkkkk blz
        private ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        public PopulaAvisosTask() {
            mListaAvisos = new ArrayList<Aviso>();
        }

        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {
                if( getSPCelula() != null){
                    mListaAvisos = new AvisoDAO().retornaAvisos(celulas[0]);
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
                if (mListaAvisos.size() > 0) {
                    getImageViewListaVazia().setVisibility(View.GONE);
                    getListViewAviso().setVisibility(View.VISIBLE);
                } else {
                    getImageViewListaVazia().setVisibility(View.VISIBLE);
                    getListViewAviso().setVisibility(View.GONE);
                }
                getListViewAviso().setAdapter(new AdapterDelete<Aviso>(getApplicationContext(), mListaAvisos));
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

    //responsavel pela remocao dos avisos selecionados do banco e atualizacao da tela
    private class RemoveAvisoTask extends AsyncTask<Void, Void, Integer> {
        ProgressDialog progressDialog;
        private final int DELETE_SUCESSO = 0;
        private final int DELETE_FALHOU = 1;
        private final int DELETE_FALHA_SQLEXCEPTION = 2;

        private ArrayList<Aviso> avisosRemover;
        private Runnable tarefa;

        public RemoveAvisoTask(ArrayList<Aviso> avisosRemover, Runnable tarefa) {
            this.avisosRemover = avisosRemover;
            this.tarefa = tarefa;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(AvisoActivity.this, "Aguarde por favor", "Removendo dados...", true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (avisosRemover.size() > 0) {
                try {
                    if (new AvisoDAO().deletaAvisos(avisosRemover)) {
                        return DELETE_SUCESSO;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return DELETE_FALHA_SQLEXCEPTION;
                    //TODO LOG ERRO
                }
            } else {
                return DELETE_FALHOU;
            }
            return DELETE_FALHOU;
        }

        @Override
        protected void onPostExecute(Integer resultadoInsercao) {
            progressDialog.dismiss();
            switch (resultadoInsercao) {
                case DELETE_SUCESSO:
                    Toast.makeText(AvisoActivity.this, "Aviso(s) removido(s) com sucesso.", Toast.LENGTH_LONG).show();
                    ((AdapterDelete)getListViewAviso().getAdapter()).removeItem();
                    tarefa.run();
                    break;
                case DELETE_FALHA_SQLEXCEPTION:
                    Utils.mostraMensagemDialog(AvisoActivity.this, "Não foi possível finalizar a operação. Verifique sua conexão com a internet e tente novamente.");
                    break;
            }
            super.onPostExecute(resultadoInsercao);
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


