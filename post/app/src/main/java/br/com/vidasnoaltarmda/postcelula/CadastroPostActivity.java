package br.com.vidasnoaltarmda.postcelula;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CadastroPostActivity extends ActionBarActivity implements View.OnClickListener {
    public static int RESULT_IMAGEM = 1;

    private ImageView imageview_imagemdestacada;
    private EditText edittext_titulo;
    private EditText edittext_conteudo;
    private TextView textview_texto_imagem;
    private Button button_adicionar_imagem;
    private Button button_enviar;

    private Bitmap imagemDestacada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_post);
        setListeners();
        //Cria um título para a action bar com o nome da célula do usuário
        setTitle("Célula");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_post, menu);
        return true;
    }

    private void setListeners() {
        getButtonAdicionarImagem().setOnClickListener(this);
        getButtonEnviar().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_adicionar_imagem:
                selecionarImagem();
                break;
            case R.id.button_enviar:
                if (verificaCamposObrigatorios()) {
                    enviaPost();
                }
                break;
        }
    }

    //TODO documentar metodo
    private void selecionarImagem() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,RESULT_IMAGEM);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO documentar metodo
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if (requestCode == RESULT_IMAGEM && resultCode == RESULT_OK) {
            try {
                if (imagemDestacada != null) {
                    imagemDestacada.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                imagemDestacada = BitmapFactory.decodeStream(stream);
                getImageViewImagemDestacada().setImageBitmap(imagemDestacada);
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

    //verifica se os campos obrigatórios foram preenchidos
    private boolean verificaCamposObrigatorios() {
        boolean camposObrigatoriosPreenchidos = true;
        if (getEditTextTitulo().getText().length() <= 0) {
            camposObrigatoriosPreenchidos = false;
            //caso o campo não tenha sido preenchido coloca mensagem de erro de preenchimento no campo
            getEditTextTitulo().setError("Preencha o campo título antes de enviar.");
        } else {
            getEditTextTitulo().setError(null);
        }
        if (getEditTextConteudo().getText().length() <= 0) {
            camposObrigatoriosPreenchidos = false;
            getEditTextConteudo().setError("Preencha o campo conteúdo antes de enviar.");
        } else {
            getEditTextConteudo().setError(null);
        }
        return camposObrigatoriosPreenchidos;
    }

    private void enviaPost() {
        new EnviaTask().execute();
    }

    private class EnviaTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        AlertDialog dialogSucesso;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(CadastroPostActivity.this, "Aguarde por favor", "Enviando Post...", true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //teste
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //dispensa janela de progresso
            progressDialog.dismiss();
            //Mostra dialog de conclusão da ação de envio
            //TODO checar o envio antes de mostrar sucesso
            new AlertDialog.Builder(CadastroPostActivity.this)
                    .setMessage("Enviado com sucesso!")
                    .setPositiveButton("OK!", null).show();
            //TODO se enviado com sucesso limpa campos
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //se o menu creditos foi selecionado na action bar
        if (id == R.id.action_creditos) {
            Toast.makeText(this, "Créditos", Toast.LENGTH_LONG).show();
            return true;
        }

        //se o menu sair foi selecionado na action bar
        if (id == R.id.action_sair) {
            SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MINHAS_PREFERENCIAS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Retorna componente EditText usado para entrada de dados referente ao título do Post
    public EditText getEditTextTitulo() {
        //Caso a variável edittext_titulo esteja vazia popula com a referencia ao objeto do componente
        if (edittext_titulo == null) {
            edittext_titulo = (EditText) findViewById(R.id.edittext_titulo);
        }
        return edittext_titulo;
    }

    //Retorna componente EditText usado para entrada de dados referente ao conteúdo do Post
    public EditText getEditTextConteudo() {
        if (edittext_conteudo == null) {
            edittext_conteudo = (EditText) findViewById(R.id.edittext_conteudo);
        }
        return edittext_conteudo;
    }

    //Retorna componente TextView usado para mostrar o nome da imagem destacada do post selecionada
    public TextView getTextViewTextoImagem() {
        if (textview_texto_imagem == null) {
            textview_texto_imagem = (TextView) findViewById(R.id.textview_texto_imagem);
        }
        return textview_texto_imagem;
    }

    //Retorna componente Button usado para entrada de dados referente a imagem destacada do post
    public Button getButtonAdicionarImagem() {
        if (button_adicionar_imagem == null) {
            button_adicionar_imagem = (Button) findViewById(R.id.button_adicionar_imagem);
        }
        return button_adicionar_imagem;
    }

    //Retorna componente Button usado para enviar os dados do post
    public Button getButtonEnviar() {
        if (button_enviar == null) {
            button_enviar = (Button) findViewById(R.id.button_enviar);
        }
        return button_enviar;
    }

    //Retorna componente ImageView usado para mostrar imagem destacada selecionada
    public ImageView getImageViewImagemDestacada() {
        if (imageview_imagemdestacada == null) {
            imageview_imagemdestacada = (ImageView) findViewById(R.id.imageview_imagemdestacada);
        }
        return imageview_imagemdestacada;
    }

}
