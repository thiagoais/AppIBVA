package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.UsuarioDAO;
import com.vidasnoaltarmda.celulas.R;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener{

    private EditText edittext_login;
    private EditText edittext_senha;
    private Button button_registrar;
    private Button button_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        insereListeners();

    }

    // insere listeners em todos os componentes da tela
    private void insereListeners() {
        getButtonEntrar().setOnClickListener(this);
    }

    //retorna referencia para o componente de tela
    private EditText getEditTextLogin() {
        if (edittext_login == null) {
            edittext_login = (EditText) findViewById(R.id.edittext_login);
        }
        return edittext_login;
    }

    //retorna referencia para o componente de tela
    private EditText getEditTextSenha() {
        if (edittext_senha == null) {
            edittext_senha = (EditText) findViewById(R.id.edittext_senha);
        }
        return edittext_senha;
    }

    //retorna referencia para o componente de tela
    private Button getButtonRegistrar() {
        if (button_registrar == null) {
            button_registrar = (Button) findViewById(R.id.button_registrar);
        }
        return button_registrar;
    }

    //retorna referencia para o componente de tela
    private Button getButtonEntrar() {
        //se não existe referência popula o campo button_entrar com a mesma, caso contrário retorna a referência do componente
        if (button_entrar == null) {
            button_entrar = (Button) findViewById(R.id.button_entrar);
        }
        return button_entrar;
    }

    //Cria o menu da actionbar (barra no topo da tela)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    //Método executado ao selecionar opção da actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Executa operações de click (usado para gerenciar cliques nos botôes da tela de login)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_entrar:
                //pegar os dados login e senha
                new LoginTask().execute(getEditTextLogin().getText().toString(), getEditTextSenha().getText().toString());
                //entrar no sistema ou nao
                break;
            case R.id.button_registrar:
                //TODO preciso fazer o metodo de registro
                break;
        }
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(LoginActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }

        @Override
        protected Boolean doInBackground(String... dadosUsuario) {
            if (dadosUsuario.length >= 2) {
                return verificaUsuario(dadosUsuario[0], dadosUsuario[1]) != null;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean resultadoLogin) {
            progressDialog.dismiss();
            if (resultadoLogin) {
                Toast.makeText(LoginActivity.this, "Login com sucesso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Login falhou", Toast.LENGTH_LONG).show();
            }
            //TODO se enviado com sucesso limpa campos
            super.onPostExecute(resultadoLogin);
        }
    }

    //Método usado para verificar se o usuário digitado é válido e se a senha do mesmo confere com a cadastrada
    private Usuario verificaUsuario(String login, String senha){
        return new UsuarioDAO().retornaUsuarioLogin(login, senha);
    }


}
