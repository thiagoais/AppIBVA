package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Escala;
import com.vidasnoaltarmda.celulas.Dados.Escalacao;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Dao.EscalaDAO;
import com.vidasnoaltarmda.celulas.Dao.UsuarioDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

public class FormEscalaActivity extends ActionBarActivity implements View.OnClickListener{

    private Celula celula;

    private EditText editTextData;
    private EditText editTextHorario;
    private EditText editTextLocal;
    private Button   buttonSalvar;
    private Spinner SpinDinamica;
    private Spinner SpinOracao;
    private Spinner SpinLouvor;
    private Spinner SpinPalavra;
    private Spinner SpinOferta;
    private Spinner SpinLanche;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_escala);

        celula = Utils.retornaCelulaSharedPreferences(this);
        new PopulaEscalasTask(celula).execute();
        insereListener();
    }

    private void insereListener() {
        getButtonSalvar().setOnClickListener(this);
        getEditTextData().setOnClickListener(this);
        getEditTextHorario().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_salvar:
                if (verificaCampos()) {
                    Escala escala = new Escala();
                    escala.setId_celula(celula.getId_celula());
                    escala.setData_celula(getEditTextData().getText().toString());
                    escala.setHora_celula(getEditTextHorario().getText().toString());
                    escala.setLocal_celula(getEditTextLocal().getText().toString());

                    Escalacao escalacaoDinamica = new Escalacao();
                    escalacaoDinamica.setTarefa("Dinâmica");
                    escalacaoDinamica.setMembro((getSpinDinamica().getSelectedItem() != null) ? ((Usuario) getSpinDinamica().getSelectedItem()).getNome() : null);
                    Escalacao escalacaoOracao = new Escalacao();
                    escalacaoOracao.setTarefa("Oração");
                    escalacaoOracao.setMembro(((getSpinOracao().getSelectedItem() != null) ? ((Usuario) getSpinOracao().getSelectedItem()).getNome() : null));
                    Escalacao escalacaoLouvor = new Escalacao();
                    escalacaoDinamica.setTarefa("Louvor");
                    escalacaoDinamica.setMembro(((getSpinLouvor().getSelectedItem() != null) ? ((Usuario) getSpinLouvor().getSelectedItem()).getNome() : null));
                    Escalacao escalacaoPalavra = new Escalacao();
                    escalacaoDinamica.setTarefa("Palavra");
                    escalacaoDinamica.setMembro(((getSpinPalavra().getSelectedItem() != null) ? ((Usuario) getSpinPalavra().getSelectedItem()).getNome() : null));
                    Escalacao escalacaoOferta = new Escalacao();
                    escalacaoDinamica.setTarefa("Oferta");
                    escalacaoDinamica.setMembro(((getSpinOferta().getSelectedItem() != null) ? ((Usuario) getSpinOferta().getSelectedItem()).getNome() : null));
                    Escalacao escalacaoLanche = new Escalacao();
                    escalacaoDinamica.setTarefa("Lanche");
                    escalacaoDinamica.setMembro(((getSpinLanche().getSelectedItem() != null) ? ((Usuario) getSpinLanche().getSelectedItem()).getNome() : null));

                    escala.getEscalacoes().add(escalacaoDinamica);
                    escala.getEscalacoes().add(escalacaoOracao);
                    escala.getEscalacoes().add(escalacaoLouvor);
                    escala.getEscalacoes().add(escalacaoPalavra);
                    escala.getEscalacoes().add(escalacaoOferta);
                    escala.getEscalacoes().add(escalacaoLanche);

                    new InsereTask().execute(escala);
                }
                break;
            case R.id.data_escala:
                Utils.mostraDatePickerDialog(this, getEditTextData());
                break;
            case R.id.horario_escala:
                Utils.mostraTimePickerDialog(this, getEditTextHorario());
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

        if (getEditTextLocal().getText().length() <= 0) {
            getEditTextLocal().setError("Por favor, digite um local");
            camposPreenchidos = false;
        }


            return camposPreenchidos;
        }

   private class PopulaEscalasTask extends AsyncTask<Void, Void, Integer> {
       ArrayList<Usuario> usuarios;
       ProgressDialog progressDialog;
       private final int RETORNO_SUCESSO = 0; //
       private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao
       private Celula celula;

       public PopulaEscalasTask(Celula celula) {
           this.celula = celula;
       }

       //metodo executado pela thread principal antes de qualquer outro processamento. Nesse caso utilizado para
        // inicializar a lista de celulas e mostrar o dialog de progresso para o usuario
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            usuarios = new ArrayList<Usuario>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(FormEscalaActivity.this, "Carregando", "Verificando dados...", true);
        }

        //metodo que executa as tarefas de acesso a banco e retorno das celulas em uma thread separada.
        // Como o proprio nome do metodo diz em background (ou em segundo plano)
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                usuarios = new UsuarioDAO().retornaUsuarios(celula);
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //TODO LOG ERRO
            }
            return RETORNO_SUCESSO;
        }

        //metodo executado apos finalizacao do metodo doInBackground. Sendo assim ja e possivel usar a lista de celulas
        // retornada
        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case RETORNO_SUCESSO:
                    getSpinDinamica().setAdapter(new ArrayAdapter<Usuario>(FormEscalaActivity.this, android.R.layout.simple_list_item_1, usuarios));
                    getSpinOracao().setAdapter(new ArrayAdapter<Usuario>(FormEscalaActivity.this, android.R.layout.simple_list_item_1, usuarios));
                    getSpinLouvor().setAdapter(new ArrayAdapter<Usuario>(FormEscalaActivity.this, android.R.layout.simple_list_item_1, usuarios));
                    getSpinPalavra().setAdapter(new ArrayAdapter<Usuario>(FormEscalaActivity.this, android.R.layout.simple_list_item_1, usuarios));
                    getSpinOferta().setAdapter(new ArrayAdapter<Usuario>(FormEscalaActivity.this, android.R.layout.simple_list_item_1, usuarios));
                    getSpinLanche().setAdapter(new ArrayAdapter<Usuario>(FormEscalaActivity.this, android.R.layout.simple_list_item_1, usuarios));

                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar as celulas, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                    Utils.mostraMensagemDialog(FormEscalaActivity.this, "Não foi possível carregar os membros. Verifique sua conexão e tente novamente.",
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

    public EditText getEditTextData() {
        if (editTextData == null) {
            editTextData = (EditText) findViewById(R.id.data_escala);
        }
        return editTextData;
    }

    public EditText getEditTextHorario() {
        if (editTextHorario == null) {
            editTextHorario = (EditText) findViewById(R.id.horario_escala);
        }
        return editTextHorario;
    }

    public EditText getEditTextLocal() {
        if (editTextLocal == null) {
            editTextLocal = (EditText) findViewById(R.id.edittext_local);
        }
        return editTextLocal;
    }

    private Spinner getSpinDinamica() {
        if (SpinDinamica == null) {
            SpinDinamica = (Spinner) findViewById(R.id.dinamica);
        }
        return SpinDinamica;
    }

    private Spinner getSpinOracao() {
        if (SpinOracao == null) {
            SpinOracao = (Spinner) findViewById(R.id.oracao);
        }
        return SpinOracao;
    }

    private Spinner getSpinLouvor() {
        if (SpinLouvor == null) {
            SpinLouvor = (Spinner) findViewById(R.id.louvor);
        }
        return SpinLouvor;
    }

    private Spinner getSpinPalavra() {
        if (SpinPalavra == null) {
            SpinPalavra = (Spinner) findViewById(R.id.palavra);
        }
        return SpinPalavra;
    }

    private Spinner getSpinOferta() {
        if (SpinOferta == null) {
            SpinOferta = (Spinner) findViewById(R.id.oferta);
        }
        return SpinOferta;
    }
    private Spinner getSpinLanche() {
        if (SpinLanche == null) {
            SpinLanche = (Spinner) findViewById(R.id.lanche);
        }
        return SpinLanche;
    }


    public Button getButtonSalvar() {
        if (buttonSalvar == null) {
            buttonSalvar = (Button) findViewById(R.id.button_salvar);
        }
        return buttonSalvar;
    }

}
