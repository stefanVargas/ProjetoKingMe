package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogo;


public class NewGameActivity extends AppCompatActivity {

    private Jogo jogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);


    }
}
