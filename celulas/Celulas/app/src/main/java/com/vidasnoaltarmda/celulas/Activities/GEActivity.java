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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.GrupoEvangelistico;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.GrupoEvangelisticoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 24/03/2016.
 */
public class GEActivity extends ActionBarActivity{
    public static final int REQUEST_SALVAR = 1;
    private ListView listview_ge;
    private Celula celula;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ge);


        celula = Utils.retornaCelulaSharedPreferences(this);
        new PopulaGruposEvangelisticosTask().execute(celula);

        mToolbar = (Toolbar) findViewById(R.id.th_ge);
        mToolbar.setTitle("Grupo Evangelístico");
        setSupportActionBar(mToolbar);
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
        if (item.getItemId() == R.id.action_adicionar) {
            android.content.Intent intent = new Intent(this, FormGEActivity.class);
            startActivityForResult(intent, REQUEST_SALVAR);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private ListView getListViewGE() {
        if (listview_ge == null) {
            listview_ge = (ListView) findViewById(R.id.listview_ge);
        }
        return listview_ge;
    }

    private class PopulaGruposEvangelisticosTask extends AsyncTask<Celula, Void, Integer> {
        ArrayList<GrupoEvangelistico> grupoEvangelisticos;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {
                if(celulas.length > 0){
                    grupoEvangelisticos = new GrupoEvangelisticoDAO().retornaGruposEvangelisticos(celulas[0]);
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
            grupoEvangelisticos = new ArrayList<GrupoEvangelistico>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(GEActivity.this, "Carregando Grupos Evangelísticos", "Aguarde por favor...", true);
        }

        //metodo executado apos finalizacao do metodo doInBackground. Sendo assim ja e possivel usar a lista de ges

        @Override
        protected void onPostExecute(Integer resultadoAviso) {
            progressDialog.dismiss();
            switch (resultadoAviso) {
                case RETORNO_SUCESSO:
                    //TODO colocar mensagem quando não houverem avisos
                    getListViewGE().setAdapter(new ArrayAdapter<GrupoEvangelistico>(GEActivity.this, R.layout.custom_list_item, grupoEvangelisticos));
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

}


