package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.UsuarioDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.TipoMsg;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

public class AniversariantesActivity extends ActionBarActivity {

    private ListView listview_aniversariantes;
    private Toolbar mToolbar;
    private ImageView imageViewListaVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariantes);
        new PopulaAniversariantesTask().execute(getSPCelula());

        mToolbar = (Toolbar) findViewById(R.id.th_aniversariante);
        mToolbar.setTitle("Aniversariantes");
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

    private class PopulaAniversariantesTask extends AsyncTask<Celula, Void, Integer> {
        ArrayList<Usuario> aniversariantes;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            aniversariantes = new ArrayList<Usuario>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(AniversariantesActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {if(celulas.length > 0){
                aniversariantes = new UsuarioDAO().retornaAniversariantes(celulas[0]);
            }
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //TODO LOG ERRO
            }
            return RETORNO_SUCESSO;
        }

        @Override
        protected void onPostExecute(Integer resultado) {
            progressDialog.dismiss();
            switch (resultado) {
                case RETORNO_SUCESSO:
                    if (aniversariantes.size() > 0) {
                        getImageViewListaVazia().setVisibility(View.GONE);
                        getListViewAniversariantes().setVisibility(View.VISIBLE);
                    }else {
                        getImageViewListaVazia().setVisibility(View.VISIBLE);
                        getListViewAniversariantes().setVisibility(View.GONE);
                    }
                    getListViewAniversariantes().setAdapter(new ArrayAdapter<Usuario>(AniversariantesActivity.this, R.layout.custom_list_item_3, aniversariantes));

                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar os aniversariantes, sendo assim uma
                    // mensagem de erro eh exibida e a tela eh encerrada
                    Utils.showMsgAlertOK(AniversariantesActivity.this,"ERRO", "Não foi possível carregar os aniversariantes. Verifique sua conexão e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultado);
        }
    }

    private ListView getListViewAniversariantes() {
        if (listview_aniversariantes == null) {
            listview_aniversariantes = (ListView) findViewById(R.id.listview_aniversariantes);
        }
        return listview_aniversariantes;
    }
    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        }
        return imageViewListaVazia;
    }
}
