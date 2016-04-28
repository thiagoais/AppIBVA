package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vidasnoaltarmda.celulas.Dados.Roteiro;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.RoteiroDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

public class RoteiroActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView listview_roteiros;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roteiro);

        new PopulaRoteirosTask().execute();
        insereListeners();

        mToolbar = (Toolbar) findViewById(R.id.th_roteiro);
        mToolbar.setTitle("Roteiros");
        setSupportActionBar(mToolbar);
    }

    //metodo responsável por buscar os dados dos Roteiros no banco (acesso remoto) e popular a lista de roteiros.
    //seguindo a boa pratica de separar a interacao externa da thread principal
    private class PopulaRoteirosTask extends AsyncTask<Usuario, Void, Integer> {
        ArrayList<Roteiro> roteiros;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        //metodo executado pela thread principal antes de qualquer outro processamento. Nesse caso utilizado para
        // inicializar a lista de roteiros e mostrar o dialog de progresso para o usuario
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            roteiros = new ArrayList<Roteiro>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(RoteiroActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        //metodo que executa as tarefas de acesso a banco e retorno dos roteiros em uma thread separada.
        // Como o proprio nome do metodo diz em background (ou em segundo plano)
        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            try {
                roteiros = new RoteiroDAO().retornaRoteiros();
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
                    getListViewRoteiros().setAdapter(new ArrayAdapter<Roteiro>(RoteiroActivity.this, R.layout.custom_list_item_3, roteiros));
                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar os roteiros, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                    Utils.mostraMensagemDialog(RoteiroActivity.this, "Não foi possível carregar os roteiros. Verifique sua conexão e tente novamente.",
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

    //metodo responsável por buscar imagem do roteiro no banco
    //seguindo a boa pratica de separar a interacao externa da thread principal
    private class mostraImagemRoteiro extends AsyncTask<Roteiro, Void, Integer> {
        String caminhoImagem = null;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        //metodo executado pela thread principal antes de qualquer outro processamento. Nesse caso utilizado para
        // inicializar o caminho onde a imagem sera salva e mostrar o dialog de progresso
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            caminhoImagem = Environment.getExternalStorageDirectory().getAbsolutePath();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(RoteiroActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        //metodo que executa as tarefas de acesso a banco e retorna a imagem salvando no dispositivo.
        @Override
        protected Integer doInBackground(Roteiro... roteiros) {
            try {
                if (roteiros.length > 0) {
                    new RoteiroDAO().retornaRoteiroImagem(roteiros[0], caminhoImagem);
                }
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
        protected void onPostExecute(Integer resultado) {
            progressDialog.dismiss();
            switch (resultado) {
                case RETORNO_SUCESSO:
                    Intent intent = new Intent(RoteiroActivity.this, ImagemAmpliadaActivity.class);
                    intent.putExtra(ImagemAmpliadaActivity.EXTRA_CAMINHO_IMAGEM, caminhoImagem + "/teste.jpg");
                    startActivity(intent);
                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar a imagem do roteiro, sendo assim uma mensagem de erro eh exibida
                    Utils.mostraMensagemDialog(RoteiroActivity.this, "Não foi possível carregar a imagem. Verifique sua conexão e tente novamente.");
                    break;
            }
            super.onPostExecute(resultado);
        }
    }

    private void insereListeners() {
        getListViewRoteiros().setOnItemClickListener(this);
    }

    private ListView getListViewRoteiros() {
        if (listview_roteiros == null) {
            listview_roteiros = (ListView) findViewById(R.id.listview_roteiros);
        }
        return listview_roteiros;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        switch (adapterView.getId()) {
            case R.id.listview_roteiros:
                new mostraImagemRoteiro().execute((Roteiro) getListViewRoteiros().getItemAtPosition(pos));
                break;
        }
    }
}
