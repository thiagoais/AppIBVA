package com.vidasnoaltarmda.celulas.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vidasnoaltarmda.celulas.Dados.Programacao;
import com.vidasnoaltarmda.celulas.Dao.ProgramacaoDAO;
import com.vidasnoaltarmda.celulas.R;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.SQLException;

public class ProgramacaoSelecionadaActivity extends ActionBarActivity {

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    private TextView textview_nome_programacao;
    private TextView textview_data;
    private TextView textview_telefone;
    private TextView textview_horario;
    private TextView textview_valor;
    private TextView textview_mapa;
    private ImageView imageview_imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao_selecionada);

        if (getIntent().getSerializableExtra(ProgramacaoActivity.PROGRAMACAO_SELECIONADA) != null) {
            Programacao programacao = (Programacao) getIntent().getSerializableExtra(ProgramacaoActivity.PROGRAMACAO_SELECIONADA);
            montaTelaProgramacao(programacao);
        } else {
            Utils.mostraMensagemDialog(this, "Erro ao abrir programação.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
        }
    }

    private void montaTelaProgramacao(Programacao programacao) {
        getTextview_nome_programacao().setText(programacao.getNome());
        getTextview_data().setText(programacao.getData_prog());
        getTextview_horario().setText(programacao.getHorario());
        getTextview_telefone().setText(programacao.getTelefone());
        getTextview_valor().setText(programacao.getValor());
        getTextview_mapa().setText(programacao.getLocal_prog());
        new mostraImagemProgramacaoTask().execute(programacao);
    }

    //metodo responsável por buscar imagem da programacao
    //TODO problema mostrar imagem
    private class mostraImagemProgramacaoTask extends AsyncTask<Programacao, Void, Integer> {
        String caminhoImagem;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            caminhoImagem = getApplicationContext().getFilesDir().getAbsolutePath() + Programacao.DIRETORIO_IMAGENS_PROGRAMACAO;
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(ProgramacaoSelecionadaActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        @Override
        protected Integer doInBackground(Programacao... programacoes) {
            try {
                if (programacoes.length > 0) {
                    new ProgramacaoDAO().retornaProgramacaoImagem(programacoes[0], caminhoImagem, Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO);
                }
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
                    final View thumb1View = findViewById(R.id.thumb_button_1);
                    ((ImageButton)thumb1View).setImageBitmap(BitmapFactory.decodeFile(caminhoImagem + "/" + Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO));
                    thumb1View.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            zoomImageFromThumb(thumb1View, BitmapFactory.decodeFile(caminhoImagem + "/" + Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO));
                        }
                    });

                    // Retrieve and cache the system's default "short" animation time.
                    mShortAnimationDuration = getResources().getInteger(
                            android.R.integer.config_shortAnimTime);

                    //getImageview_imagem().setImageBitmap(BitmapFactory.decodeFile(caminhoImagem + "/" + Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO));
                    break;
                case FALHA_SQLEXCEPTION:
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }
    }

    public TextView getTextview_nome_programacao() {
        if (textview_nome_programacao == null) {
            textview_nome_programacao = (TextView) findViewById(R.id.textview_nome_programacao);
        }
        return textview_nome_programacao;
    }

    public TextView getTextview_data() {
        if (textview_data == null) {
            textview_data = (TextView) findViewById(R.id.textview_data);
        }
        return textview_data;
    }

    public TextView getTextview_telefone() {
        if (textview_telefone == null) {
            textview_telefone = (TextView) findViewById(R.id.textview_telefone);
        }
        return textview_telefone;
    }

    public TextView getTextview_horario() {
        if (textview_horario == null) {
            textview_horario = (TextView) findViewById(R.id.textview_horario);
        }
        return textview_horario;
    }

    public TextView getTextview_valor() {
        if (textview_valor == null) {
            textview_valor = (TextView) findViewById(R.id.textview_valor);
        }
        return textview_valor;
    }

    public TextView getTextview_mapa() {
        if (textview_mapa == null) {
            textview_mapa = (TextView) findViewById(R.id.textview_mapa);
        }
        return textview_mapa;
    }



    private void zoomImageFromThumb(final View thumbView, Bitmap imageBmp) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageBitmap(imageBmp);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
