package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.UsuarioDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

public class AniversariantesActivity extends ActionBarActivity {

    private ListView listview_aniversariantes;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariantes);
        new PopulaAniversariantesTask().execute();

        mToolbar = (Toolbar) findViewById(R.id.th_aniversariante);
        mToolbar.setTitle("Aniversariantes");
        setSupportActionBar(mToolbar);
    }

    private class PopulaAniversariantesTask extends AsyncTask<Void, Void, Integer> {
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
        protected Integer doInBackground(Void... params) {
            try {
                aniversariantes = new UsuarioDAO().retornaAniversariantes();
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

                    getListViewAniversariantes().setAdapter(new ArrayAdapter<Usuario>(AniversariantesActivity.this, R.layout.custom_list_item_3, aniversariantes));

                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar os aniversariantes, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                    Utils.mostraMensagemDialog(AniversariantesActivity.this, "Não foi possível carregar os aniversariantes. Verifique sua conexão e tente novamente.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
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
}
