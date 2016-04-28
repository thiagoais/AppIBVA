package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Aviso;
import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Escala;
import com.vidasnoaltarmda.celulas.Dados.Escalacao;
import com.vidasnoaltarmda.celulas.Dao.AvisoDAO;
import com.vidasnoaltarmda.celulas.Dao.EscalaDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;

public class FormEscalaActivity extends ActionBarActivity implements View.OnClickListener{

    private Celula celula;

    private EditText editTextData;
    private EditText editTextHorario;
    private EditText editTextLocal;
    private EditText editTextDinamica;
    private EditText editTextOracao;
    private EditText editTextLouvor;
    private EditText editTextPalavra;
    private EditText editTextOferta;
    private EditText editTextLanche;
    private Button   buttonSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_escala);

        celula = Utils.retornaCelulaSharedPreferences(this);
        insereListener();
    }

    private void insereListener() {
        getButtonSalvar().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_salvar:
                if (verificaCampos()) {
                    Escala escala = new Escala();
                    Escalacao escalacao = new Escalacao();
                    escala.setId_celula(celula.getId_celula());
                    escala.setData_celula(getEditTextData().getText().toString());
                    escala.setHora_celula(getEditTextHorario().getText().toString());
                    escala.setLocal_celula(getEditTextLocal().getText().toString());
                  //  escalacao.setTarefa(get().getText().toString());

                    new InsereTask().execute(escala);
                }
                break;
        }
    }

    private class InsereTask extends AsyncTask<Escala, Void, Integer> {
        ProgressDialog progressDialog;
        private final int INSERCAO_SUCESSO = 0;
        private final int INSERCAO_FALHOU = 1;
        private final int INSERCAO_FALHA_SQLEXCEPTION = 2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(FormEscalaActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }

        @Override
        protected Integer doInBackground(Escala... escalas) {
            if (escalas.length > 0) {
                try {
                    if (new EscalaDAO().insereEscala(escalas[0])) {
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
        protected void onPostExecute(Integer resultadoInsercao) {
            progressDialog.dismiss();
            switch (resultadoInsercao) {
                case INSERCAO_SUCESSO:
                    Toast.makeText(FormEscalaActivity.this, "Inserido com sucesso.", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, getIntent());
                    finish();
                    break;
                case INSERCAO_FALHA_SQLEXCEPTION:
                    Utils.mostraMensagemDialog(FormEscalaActivity.this, "Não foi possível finalizar o cadastro. Verifique sua conexão com a internet e tente novamente.");
                    break;
            }
            super.onPostExecute(resultadoInsercao);
        }
    }

    private boolean verificaCampos() {
        boolean camposPreenchidos = true;
        if (getEditTextData().getText().length() <= 0) {
            getEditTextData().setError("Por favor, selecione uma data válida");
            camposPreenchidos = false;
        }

        if (getEditTextHorario().getText().length() <= 0) {
            getEditTextHorario().setError("Por favor, digite um horário");
            camposPreenchidos = false;
        }
            return camposPreenchidos;
        }

    /*if (getEditT().getText().length() <= 0) {
        getEditTextHorario().setError("Por favor, digite um horário");
        camposPreenchidos = false;
    }
    return camposPreenchidos;
}*/

    public EditText getEditTextData() {
        if (editTextData == null) {
            editTextData = (EditText) findViewById(R.id.edittext_data);
        }
        return editTextData;
    }

    public EditText getEditTextHorario() {
        if (editTextHorario == null) {
            editTextHorario = (EditText) findViewById(R.id.edittext_hora);
        }
        return editTextHorario;
    }

    public EditText getEditTextLocal() {
        if (editTextLocal == null) {
            editTextLocal = (EditText) findViewById(R.id.edittext_local);
        }
        return editTextLocal;
    }

    public EditText getEditTextLanche() {
        if (editTextLanche == null) {
            editTextLanche = (EditText) findViewById(R.id.edittext_lanche);
        }
        return editTextLanche;
    }


    public Button getButtonSalvar() {
        if (buttonSalvar == null) {
            buttonSalvar = (Button) findViewById(R.id.button_salvar);
        }
        return buttonSalvar;
    }

}
