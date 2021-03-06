package com.vidasnoaltarmda.celulas.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.vidasnoaltarmda.celulas.R;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagemAmpliadaActivity extends ActionBarActivity {
    public static final String EXTRA_CAMINHO_IMAGEM = "EXTRA_CAMINHO_IMAGEM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem_ampliada);

        String caminhoImagem = getIntent().getStringExtra(EXTRA_CAMINHO_IMAGEM);
        if((caminhoImagem != null) && (new File(caminhoImagem).exists())) {
            ImageView mImageView = (ImageView) findViewById(R.id.imageview_imagem);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoImagem, options);
            mImageView.setImageBitmap(bitmap);

            PhotoViewAttacher mAttacher;
            mAttacher = new PhotoViewAttacher(mImageView);
        }
    }

}
