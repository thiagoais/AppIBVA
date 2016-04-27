package com.vidasnoaltarmda.celulas.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

    private ListView listview_avisos;
    private Celula celula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);

        celula = Utils.retornaCelulaSharedPreferences(this);
        new PopulaAvisosTask().execute(celula);
        insereListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SALVAR && resultCode == RESULT_OK) {
            new PopulaAvisosTask().execute(celula);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insereListeners() {
        getListViewAviso().setOnItemClickListener(this);
        //getListViewAviso().setAdapter(adapter);
        getListViewAviso().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListViewAviso().setSelected(true);
        //TODO somente permitir caso o usuario tenha permissao
        getListViewAviso().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selectionCounter;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((AdapterDelete)getListViewAviso().getAdapter()).limpaItensSelecionados();
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
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.action_deletar:
                        selectionCounter = 0;
                        //TODO if (itensDeletadosComSucesso)
                        ((AdapterDelete)getListViewAviso().getAdapter()).removeItem();
                        mode.finish();
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
                    ((AdapterDelete)getListViewAviso().getAdapter()).selectedItem(position, position);
                    Toast.makeText(getApplicationContext(),"checked", Toast.LENGTH_LONG).show();

                } else {
                    selectionCounter--;
                    ((AdapterDelete)getListViewAviso().getAdapter()).removeSelection(position);
                    Toast.makeText(getApplicationContext(),"unchecked", Toast.LENGTH_LONG).show();
                }
                mode.setTitle(selectionCounter + " Selecionado(s)");

            }
        });
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
                if( celula!= null){
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
                getListViewAviso().setAdapter(new AdapterDelete<Aviso>(getApplicationContext(), avisos));
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


