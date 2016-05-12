package com.vidasnoaltarmda.celulas.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Programacao;
import com.vidasnoaltarmda.celulas.Dao.ProgramacaoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class FormProgramacaoActivity extends ActionBarActivity implements View.OnClickListener{

    private Celula celula;
    private String RESULT_IMAGEM;
    private EditText editTextNome;
    private EditText editTextData;
    private EditText editTextHorario;
    private EditText editTextEndereco;
    private EditText editTexTelefone;
    private EditText editTextValor;
   // private EditText editTextImagem;//TODO Receber Imagem
    private Button   buttonSalvar;
    private ImageView ImagemProgramacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_programacao);

        celula = Utils.retornaCelulaSharedPreferences(this);
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
                    Programacao programacao = new Programacao();
                    programacao.setId_celula(celula.getId_celula());
                    programacao.setNome(getEditTextNome().getText().toString());
                    programacao.setData_prog(getEditTextData().getText().toString());
                    programacao.setHorario(getEditTextHorario().getText().toString());
                    programacao.setLocal_prog(getEditTextEndereco().getText().toString());
                    programacao.setTelefone(getEditTextTelefone().getText().toString());
                    programacao.setValor(getEditTextValor().getText().toString());
               //     programacao.setImagem(getBlobImagem());//TODO IMAGEM

                    new InsereTask().execute(programacao);
                }
                break;
            case R.id.data_programacao:
                Utils.mostraDatePickerDialog(this, getEditTextData());
                break;
            case R.id.horario_programacao:
                Utils.mostraTimePickerDialog(this, getEditTextHorario());
                break;
        }
    }

    private class InsereTask extends AsyncTask<Programacao, Void, Integer> {
        ProgressDialog progressDialog;
        private final int INSERCAO_SUCESSO = 0;
        private final int INSERCAO_FALHOU = 1;
        private final int INSERCAO_FALHA_SQLEXCEPTION = 2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(FormProgramacaoActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }

        @Override
        protected Integer doInBackground(Programacao... programacaos) {
            if (programacaos.length > 0) {
                try {
                    if (new ProgramacaoDAO().insereProgramacao(programacaos[0])) {
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
        private void selecionarImagem() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, Integer.parseInt(RESULT_IMAGEM));
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
            InputStream stream = null;
            if (requestCode == Integer.parseInt(RESULT_IMAGEM) && resultCode == RESULT_OK) {
                try {
                    if (ImagemProgramacao != null) {
                        ImagemProgramacao.recycle();
                    }
                    stream = getContentResolver().openInputStream(data.getData());
                    ImagemProgramacao = BitmapFactory.decodeStream(stream);
                    getImagemProgramacao().setImageBitmap(ImagemProgramacao);
                }
                catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
                finally {
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }

            }
        }
        @Override
        protected void onPostExecute(Integer resultadoInsercao) {
            progressDialog.dismiss();
            switch (resultadoInsercao) {
                case INSERCAO_SUCESSO:
                    Toast.makeText(FormProgramacaoActivity.this, "Inserido com sucesso.", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, getIntent());
                    finish();
                    //TODO terminar tela e voltar pra tela de login
                    break;
                case INSERCAO_FALHA_SQLEXCEPTION:
                    Utils.mostraMensagemDialog(FormProgramacaoActivity.this, "Não foi possível finalizar o cadastro. Verifique sua conexão com a internet e tente novamente.");
                    break;
            }
            super.onPostExecute(resultadoInsercao);
        }
    }

    private boolean verificaCampos() {
        boolean camposPreenchidos = true;
        if (getEditTextNome().getText().length() <= 0) {
            getEditTextNome().setError("Por favor, digite o Nome da Programação");
            camposPreenchidos = false;
        }

        if (getEditTextData().getText().length() <= 0) {
            getEditTextData().setError("Por favor, digite a data da programação");
            camposPreenchidos = false;
        }

        if (getEditTextHorario().getText().length() <= 0) {
            getEditTextHorario().setError("Por favor, digite o horário da programação");
            camposPreenchidos = false;
        }

        if (getEditTextEndereco().getText().length() <= 0) {
            getEditTextEndereco().setError("Por favor, digite o endereço");
            camposPreenchidos = false;
        }

        if (getEditTextTelefone().getText().length() <= 0) {
            getEditTextTelefone().setError("Por favor, digite o telefone");
            camposPreenchidos = false;
        }

        if (getEditTextValor().getText().length() <= 0) {
            getEditTextValor().setError("Por favor, digite o valor em R$ que será gasto aproximadamente");
            camposPreenchidos = false;
        }

        return camposPreenchidos;
    }

    public EditText getEditTextNome() {
        if (editTextNome == null) {
            editTextNome = (EditText) findViewById(R.id.edittext_nome);
        }
        return editTextNome;
    }

    public EditText getEditTextData() {
        if (editTextData == null) {
            editTextData = (EditText) findViewById(R.id.data_programacao);
        }
        return editTextData;
    }

    public EditText getEditTextHorario() {
        if (editTextHorario == null) {
            editTextHorario = (EditText) findViewById(R.id.horario_programacao);
        }
        return editTextHorario;
    }

    public EditText getEditTextEndereco() {
        if (editTextEndereco == null) {
            editTextEndereco = (EditText) findViewById(R.id.edittext_endereco);
        }
        return editTextEndereco;
    }

    public EditText getEditTextTelefone() {
        if (editTexTelefone == null) {
            editTexTelefone = (EditText) findViewById(R.id.edittext_telefone);
        }
        return editTexTelefone;
    }

    public EditText getEditTextValor() {
        if (editTextValor == null) {
            editTextValor = (EditText) findViewById(R.id.edittext_valor);
        }
        return editTextValor;
    }

    public ImageView getImagemProgramacao() {
        if (ImagemProgramacao == null) {
            ImagemProgramacao = (ImageView) findViewById(R.id.imageview_programacao);
        }
        return ImagemProgramacao;
    }

    public Button getButtonSalvar() {
        if (buttonSalvar == null) {
            buttonSalvar = (Button) findViewById(R.id.button_salvar);
        }
        return buttonSalvar;
    }

}
