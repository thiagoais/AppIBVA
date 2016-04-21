package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.GrupoEvangelistico;
import com.vidasnoaltarmda.celulas.Dao.GrupoEvangelisticoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 24/03/2016.
 */
public class GEActivity extends ActionBarActivity{
    private ListView listview_ge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ge);

        //TODO receber a celula como parametro, verificar possibilidade de ser guardada na sessao
        //teste
        Celula celula = new Celula();
        celula.setId_celula(9);

        //teste
        new PopulaGruposEvangelisticosTask().execute(celula);
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
        // retornada
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


