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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.GrupoEvangelistico;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.GrupoEvangelisticoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.AdapterDelete;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 24/03/2016.
 */
public class GEActivity extends ActionBarActivity{
    public static final int REQUEST_SALVAR = 1;
    private static final String STATE_LISTA_GE = "STATE_LISTA_GE";
    private ListView listview_ge;
    private Celula celula;
    private Toolbar mToolbar;
    private ArrayList<GrupoEvangelistico> mListaGE;
    private ImageView imageViewListaVazia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ge);


        celula = Utils.retornaCelulaSharedPreferences(this);

        if (savedInstanceState == null) {
            new PopulaGruposEvangelisticosTask().execute(getSPCelula());
        } else {
            if (savedInstanceState.get(STATE_LISTA_GE) != null) {

                //TODO arrumar problema quando existem itens selecionados e a tela gira (ActionMode)
                mListaGE = (ArrayList<GrupoEvangelistico>) savedInstanceState.get(STATE_LISTA_GE);
                getListViewGE().setAdapter(new AdapterDelete<GrupoEvangelistico>(this, mListaGE, R.layout.custom_list_item));
            }
        }
        insereListeners();
        mToolbar = (Toolbar) findViewById(R.id.th_ge);
        mToolbar.setTitle("Grupo Evangelístico");
        setSupportActionBar(mToolbar);
    }
    private Celula getSPCelula() {
        return Utils.retornaCelulaSharedPreferences(this);
    }

    @Override
    public void onSaveInstanceState(Bundle estadoDeSaida) {
        super.onSaveInstanceState(estadoDeSaida);
        if (getListViewGE().getAdapter() != null) {
            estadoDeSaida.putSerializable(STATE_LISTA_GE, mListaGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SALVAR && resultCode == RESULT_OK) {
            new PopulaGruposEvangelisticosTask().execute(celula);
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
            getMenuInflater().inflate(R.menu.menu_grupo_evangelistico, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int permissaoUsuario = 0;
        permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));
        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            if (item.getItemId() == R.id.action_adicionar) {
                android.content.Intent intent = new Intent(this, FormGEActivity.class);
                startActivityForResult(intent, REQUEST_SALVAR);
                getListViewGE().setChoiceMode(getListViewGE().getChoiceMode()); //Acerto para cancelar o modo de selecao da lista quando o usuario entra na insercao de ge
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void insereListeners() {
        int permissaoUsuario = 0;
        permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));
        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            getListViewGE().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            getListViewGE().setSelected(true);
        }

        getListViewGE().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selectionCounter;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((AdapterDelete) getListViewGE().getAdapter()).limpaItensSelecionados();
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

                        new RemoveGETask(
                                ((AdapterDelete<GrupoEvangelistico>) getListViewGE().getAdapter()).getItensSelecionados(),
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
                    ((AdapterDelete) getListViewGE().getAdapter()).selectedItem(position, position);

                } else {
                    selectionCounter--;
                    ((AdapterDelete)getListViewGE().getAdapter()).removeSelection(position);
                }
                if (selectionCounter > 1){
                    mode.setTitle(selectionCounter + " Selecionados");
                }else{
                    mode.setTitle(selectionCounter + " Selecionado");
                }

            }
        });
    }


    private ListView getListViewGE() {
        if (listview_ge == null) {
            listview_ge = (ListView) findViewById(R.id.listview_ge);
        }
        return listview_ge;
    }
    //responsavel pela remocao dos avisos selecionados do banco e atualizacao da tela
    private class RemoveGETask extends AsyncTask<Void, Void, Integer> {
        ProgressDialog progressDialog;
        private final int DELETE_SUCESSO = 0;
        private final int DELETE_FALHOU = 1;
        private final int DELETE_FALHA_SQLEXCEPTION = 2;

        private ArrayList<GrupoEvangelistico> geRemover;
        private Runnable tarefa;

        public RemoveGETask(ArrayList<GrupoEvangelistico> geRemover, Runnable tarefa) {
            this.geRemover = geRemover;
            this.tarefa = tarefa;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(GEActivity.this, "Aguarde por favor", "Removendo GE...", true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (geRemover.size() > 0) {
                try {
                    if (new GrupoEvangelisticoDAO().deletaGe(geRemover)) {
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
                    Toast.makeText(GEActivity.this, "GE(s) removido(s) com sucesso.", Toast.LENGTH_LONG).show();
                    ((AdapterDelete)getListViewGE().getAdapter()).removeItem();
                    tarefa.run();
                    break;
                case DELETE_FALHA_SQLEXCEPTION:
                    Utils.mostraMensagemDialog(GEActivity.this, "Não foi possível finalizar a operação. Verifique sua conexão com a internet e tente novamente.");
                    break;
            }
            super.onPostExecute(resultadoInsercao);
        }
    }

    private class PopulaGruposEvangelisticosTask extends AsyncTask<Celula, Void, Integer> {
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {
                if(celulas.length > 0){
                    mListaGE = new GrupoEvangelisticoDAO().retornaGruposEvangelisticos(celulas[0]);
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
            mListaGE = new ArrayList<GrupoEvangelistico>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(GEActivity.this, "Carregando Grupos Evangelísticos", "Aguarde por favor...", true);
        }

        //metodo executado apos finalizacao do metodo doInBackground. Sendo assim ja e possivel usar a lista de ges

        @Override
        protected void onPostExecute(Integer resultadoAviso) {
            progressDialog.dismiss();
            switch (resultadoAviso) {
                case RETORNO_SUCESSO:
                    if (mListaGE.size() > 0) {
                        getImageViewListaVazia().setVisibility(View.GONE);
                        getListViewGE().setVisibility(View.VISIBLE);
                    }else {
                        getImageViewListaVazia().setVisibility(View.VISIBLE);
                        getListViewGE().setVisibility(View.GONE);
                    }
                    getListViewGE().setAdapter(new AdapterDelete<GrupoEvangelistico>(GEActivity.this, mListaGE, R.layout.custom_list_item));
                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar os ges, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                    Utils.mostraMensagemDialog(GEActivity.this, "Não foi possível carregar os Grupos Evangelísticos. Verifique sua conexão e tente novamente.",
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
    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        }
        return imageViewListaVazia;
    }

}


