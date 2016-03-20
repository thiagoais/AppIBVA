package com.vidasnoaltarmda.celulas.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.CelulaDAO;
import com.vidasnoaltarmda.celulas.Dao.UsuarioDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegistrarActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText nome;
    private EditText sobrenome;
    private EditText data_nascimento;
    private Spinner celulas;
    private EditText login;
    private EditText senha;
    private EditText confirma_senha;
    private Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        //String[] listaCelulas = {"Ágape","Amigas de Deus","Ganhando Vidas","Geração Eleita","Guerreiros de Deus 1", "Guerreiros de Deus 2",
        //"Intimidade com Deus", "Kairós", "Novas Vidas", "Vale da Benção", "Vitória de Deus"};
        new PopulaCelulasTask().execute();
        insereListeners();

    }

    private void insereListeners() {
        getRegistrar().setOnClickListener(this);
        getDataNascimento().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registrar:
                if (verificaDadosUsuario()) {
                    Usuario usuario = montaUsuario();
                    new InsereTask().execute(usuario);
                }

                //permissao
                //celula
                //insere
                break;
            case R.id.data_nascimento:
                mostraDatePickerDialog(getDataNascimento());
                break;
        }
    }

    private void mostraDatePickerDialog(final EditText campoTexto) {
        final Calendar newCalendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                campoTexto.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean verificaDadosUsuario() {
        boolean camposPreenchidos = true;
        if (getNome().getText().length() <= 0) {
            getNome().setError("Por favor, digite seu nome");
            camposPreenchidos = false;
        }
        if (getSobrenome().getText().length() <= 0) {
            getNome().setError("Por favor, digite seu sobrenome");
            camposPreenchidos = false;
        }
        if (getDataNascimento().getText().length() <= 0) {
            getDataNascimento().setError("Por favor, selecione sua data de nascimento");
        int dataAtual = new Date().getYear();
        if(2017 > dataAtual){
            Toast.makeText(RegistrarActivity.this, "Por favor, digite um ano de nascimento válido!", Toast.LENGTH_SHORT).show();
        }


            camposPreenchidos = false;
        }
        if (getLogin().getText().length() <= 0) {
            getLogin().setError("Por favor, digite um nome para seu login");
            camposPreenchidos = false;
        }
        if (getSenha().getText().length() <= 0) {
            getSenha().setError("Por favor, digite uma senha");
            camposPreenchidos = false;
        }


        return camposPreenchidos;
    }

    private Usuario montaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome(getNome().getText().toString());
        usuario.setSobrenome(getSobrenome().getText().toString());
        usuario.setDataNascimento(getDataNascimento().getText().toString());
        usuario.setLogin(getLogin().getText().toString());
        usuario.setSenha(getSenha().getText().toString());
        return usuario;
    }

    private class InsereTask extends AsyncTask<Usuario, Void, Integer> {
        ProgressDialog progressDialog;
        private final int INSERCAO_SUCESSO = 0; // sucesso na operacao de login
        private final int INSERCAO_FALHOU = 1; // ocorreu uma falha na operacao de login. Provavelmente causada por erro de digitacao do usuario ou inexistencia de cadastro
        private final int INSERCAO_FALHA_SQLEXCEPTION = 2; //provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(RegistrarActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            if (usuarios.length > 0) {
                try {
                    if (new UsuarioDAO().insereUsuario(usuarios[0])) {
                        return INSERCAO_SUCESSO;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return INSERCAO_FALHA_SQLEXCEPTION;
                    //TODO LOG ERRO
                }
            } else {
                return INSERCAO_FALHOU;
            }
            return INSERCAO_FALHOU;
        }

        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case INSERCAO_SUCESSO:
                    Toast.makeText(RegistrarActivity.this, "Inserido com sucesso.", Toast.LENGTH_LONG).show();
                    //TODO terminar tela e voltar pra tela de login
                    break;
                case INSERCAO_FALHA_SQLEXCEPTION:
                    Utils.mostraMensagemDialog(RegistrarActivity.this, "Não foi possível finalizar o cadastro. Verifique sua conexão com a internet e tente novamente.");
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }
    }

    private class PopulaCelulasTask extends AsyncTask<Usuario, Void, Integer> {
        ArrayList<Celula> celulas;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            celulas = new ArrayList<Celula>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(RegistrarActivity.this, "Carregando", "Verificando dados...", true);
        }

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            try {
                celulas = new CelulaDAO().retornaCelulas();
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //TODO LOG ERRO
            }
            return RETORNO_SUCESSO;
        }

        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case RETORNO_SUCESSO:
                    getCelulas().setAdapter(new ArrayAdapter<Celula>(RegistrarActivity.this, android.R.layout.simple_list_item_1, celulas));
                    break;
                case FALHA_SQLEXCEPTION:
                    Utils.mostraMensagemDialog(RegistrarActivity.this, "Não foi possível carregar as células.");
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }
    }

    private EditText getNome() {
        if (nome == null) {
            nome = (EditText) findViewById(R.id.nome);
        }
        return nome;
    }

    private EditText getSobrenome() {
        if (sobrenome == null) {
            sobrenome = (EditText) findViewById(R.id.sobrenome);
        }
        return sobrenome;
    }

    private EditText getDataNascimento() {
        if (data_nascimento == null) {
            data_nascimento = (EditText) findViewById(R.id.data_nascimento);
        }
        return data_nascimento;
    }


    private Spinner getCelulas() {
        if (celulas == null) {
            celulas = (Spinner) findViewById(R.id.celulas);
        }
        return celulas;
    }

    private EditText getLogin() {
        if (login == null) {
            login = (EditText) findViewById(R.id.login);
        }
        return login;
    }

    private EditText getSenha() {
        if (senha == null) {
            senha = (EditText) findViewById(R.id.senha);
        }
        return senha;
    }

    private EditText getConfirmaSenha() {
        if (confirma_senha == null) {
            confirma_senha = (EditText) findViewById(R.id.confirma_senha);
        }
        return confirma_senha;
    }

    private Button getRegistrar() {
        if (registrar == null) {
            registrar = (Button) findViewById(R.id.registrar);
        }
        return registrar;
    }

}
