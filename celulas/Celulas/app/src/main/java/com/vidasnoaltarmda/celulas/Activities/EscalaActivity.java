package com.vidasnoaltarmda.celulas.Activities;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Escala;
import com.vidasnoaltarmda.celulas.Dados.Escalacao;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.EscalaDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.AdapterDelete;
import com.vidasnoaltarmda.celulas.Utils.TipoMsg;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 14/03/2016.
 */


public class EscalaActivity extends ActionBarActivity {
    public static final int REQUEST_SALVAR = 1;
    private static final String STATE_ESCALA = "STATE_ESCALA";
    private TextView nome;
    private TextView data;
    private TextView horario;
    private TextView local;
    private ListView listview_escala;
    private Toolbar mToolbar;
    private Escala mEscalaAtual;

    private Celula celula;
    private ImageView imageViewListaVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escala);

        //TODO verificar necessidade de entrar na tela caso não hajam escalas (ou mostrar mensagem que nao existem escalas)
        celula = Utils.retornaCelulaSharedPreferences(this);

        if (savedInstanceState == null) {
            new MontaTelaEscalasTask().execute();
        } else {
            if (savedInstanceState.get(STATE_ESCALA) != null) {
                mEscalaAtual = (Escala) savedInstanceState.get(STATE_ESCALA);
                getListViewEscala().setAdapter(new AdapterDelete<Escalacao>(this, mEscalaAtual.getEscalacoes()));
            }
        }



        mToolbar = (Toolbar) findViewById(R.id.th_escala);
        mToolbar.setTitle("Escala");
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private Celula getSPCelula() {
        return Utils.retornaCelulaSharedPreferences(this);
    }

    @Override
    public void onSaveInstanceState(Bundle estadoDeSaida) { //TODO fazer tratamento de giro de tela nas outras telas
        super.onSaveInstanceState(estadoDeSaida);
        if (getListViewEscala().getAdapter() != null) {
            estadoDeSaida.putSerializable(STATE_ESCALA, mEscalaAtual);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SALVAR && resultCode == RESULT_OK) {
            new MontaTelaEscalasTask().execute();
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
            getMenuInflater().inflate(R.menu.menu_escalas, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_adicionar) {
            Intent intent = new Intent(this, FormEscalaActivity.class);
            startActivityForResult(intent, REQUEST_SALVAR);
            getListViewEscala().setChoiceMode(getListViewEscala().getChoiceMode());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insereListeners() {
        getListViewEscala().setOnItemClickListener((AdapterView.OnItemClickListener) this);
        getListViewEscala().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListViewEscala().setSelected(true);
        //TODO somente permitir caso o usuario tenha permissao
        getListViewEscala().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selectionCounter;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((AdapterDelete) getListViewEscala().getAdapter()).limpaItensSelecionados();
                selectionCounter = 0;
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.action_deletar:
                        new RemoveEscalaTask(
                                ((AdapterDelete<Escala>) getListViewEscala().getAdapter()).getItensSelecionados(),
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
                    ((AdapterDelete) getListViewEscala().getAdapter()).selectedItem(position, position);

                } else {
                    selectionCounter--;
                    ((AdapterDelete)getListViewEscala().getAdapter()).removeSelection(position);
                }
                if (selectionCounter > 1){
                    mode.setTitle(selectionCounter + " Selecionados");
                }else{
                    mode.setTitle(selectionCounter + " Selecionado");
                }

            }
        });
    }

    private ListView getListViewEscala() {
        if (listview_escala == null) {
            listview_escala = (ListView) findViewById(R.id.listview_escala);
        }
        return listview_escala;
    }

    private class  MontaTelaEscalasTask extends AsyncTask<Void, Void, Integer> {
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0;
        private final int FALHA_SQLEXCEPTION = 1;

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                mEscalaAtual = new EscalaDAO().retornaEscala(celula);
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
              mEscalaAtual = new Escala();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(EscalaActivity.this, "Carregando escala", "Aguarde por favor...", true);
        }

        @Override
        protected void onPostExecute(Integer resultadoEscala) {
            progressDialog.dismiss();
            switch (resultadoEscala) {
                case RETORNO_SUCESSO:
                    if (celula != null && mEscalaAtual != null) {
                        getImageViewListaVazia().setVisibility(View.GONE);
                        getListViewEscala().setVisibility(View.VISIBLE);

                        getNome().setText(celula.getNome());
                        getData().setText(mEscalaAtual.getData_celula() + " - " + mEscalaAtual.getHora_celula());
                        getLocal().setText(mEscalaAtual.getLocal_celula());
                        getListViewEscala().setAdapter(new ArrayAdapter<Escalacao>(EscalaActivity.this, R.layout.custom_list_item_3, mEscalaAtual.getEscalacoes()));
                    } else {
                        getImageViewListaVazia().setVisibility(View.VISIBLE);
                        getListViewEscala().setVisibility(View.GONE);
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

    private class RemoveEscalaTask extends AsyncTask<Void, Void, Integer> {
        ProgressDialog progressDialog;
        private final int DELETE_SUCESSO = 0;
        private final int DELETE_FALHOU = 1;
        private final int DELETE_FALHA_SQLEXCEPTION = 2;

        private ArrayList<Escala> escalasRemover;
        private Runnable tarefa;

        public RemoveEscalaTask(ArrayList<Escala> escalasRemover, Runnable tarefa) {
            this.escalasRemover = escalasRemover;
            this.tarefa = tarefa;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(EscalaActivity.this, "Aguarde por favor", "Removendo dados...", true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
       if (escalasRemover.size() > 0) {
                try {
                    if (new EscalaDAO().deletaEscalas(escalasRemover)) {
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
                    Utils.showMessageToast(EscalaActivity.this, "Escala removida com sucesso.");
                    ((AdapterDelete)getListViewEscala().getAdapter()).removeItem();
                    tarefa.run();
                    break;
                case DELETE_FALHA_SQLEXCEPTION:
                    Utils.showMsgAlertOK(EscalaActivity.this, "Erro", "Não foi possível finalizar a operação. Verifique sua conexão com a internet e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultadoInsercao);
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
    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.lista_vazia);
        }
        return imageViewListaVazia;
    }



}