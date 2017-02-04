package com.vidasnoaltarmda.celulas.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Barque on 01/02/2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();

    }
}
