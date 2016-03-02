package br.com.vidasnoaltarmda.postcelula;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements View.OnClickListener{
    public static final String MINHAS_PREFERENCIAS = "MyPrefs" ;
    public static String LOGIN_SP = "LOGIN";

    private SharedPreferences sharedPreferences;

    private EditText edittext_login;
    private EditText edittext_senha;
    private Button   button_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setListeners();
        //verifica se já foi realizado login anteriormente
        sharedPreferences = getSharedPreferences(MINHAS_PREFERENCIAS, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(LOGIN_SP, null) != null) {
            //em caso positivo entra na tela de post sem necessidade de login
            Intent intent = new Intent(LoginActivity.this, CadastroPostActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void setListeners() {
        getButtonEntrar().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_entrar:
                //verificaLoginSenha
                //se ok guarda na sessão o login do usuario e demais informações necessárias
                sharedPreferences = getSharedPreferences(MINHAS_PREFERENCIAS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LOGIN_SP, getEditTextLogin().getText().toString());
                editor.commit();

                //inicia a tela de post
                Intent intent = new Intent(LoginActivity.this, CadastroPostActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    //Retorna componente EditText usado no campo login
    public EditText getEditTextLogin() {
        if (edittext_login == null) {
            edittext_login = (EditText) findViewById(R.id.edittext_login);
        }
        return edittext_login;
    }

    //Retorna componente EditText usado no campo senha
    public EditText getEditTextSenha() {
        if (edittext_senha == null) {
            edittext_senha = (EditText) findViewById(R.id.edittext_senha);
        }
        return edittext_senha;
    }

    //Retorna componente Button usado para entrar no sistema
    public Button getButtonEntrar() {
        if (button_entrar == null) {
            button_entrar = (Button) findViewById(R.id.button_entrar);
        }
        return button_entrar;
    }
}
