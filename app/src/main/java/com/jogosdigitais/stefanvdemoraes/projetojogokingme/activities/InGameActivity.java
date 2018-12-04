package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;


public class InGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("jogo", 0); // 0 - for private mode

        String idJogador = pref.getString("idJogador","");
        String nomeJogador = pref.getString("nomeJogador","");
        String senhaJogador = pref.getString("senhaJogador","");
        String idJogo = pref.getString("idJogo","");
        String nomeJogo = pref.getString("nomeJogo","");
        String senhaJogo = pref.getString("senhaJogo","");

        TextView idJogadorTV = findViewById(R.id.idJogador);
        TextView nomeJogadorTV = findViewById(R.id.nomeJogador);
        TextView senhaJogadorTV = findViewById(R.id.senhaJogador);
        TextView idJogoTV = findViewById(R.id.idJogo);
        TextView nomeJogoTV = findViewById(R.id.nomeJogo);
        TextView senhaJogoTV = findViewById(R.id.senhaJogo);

        idJogadorTV.setText(idJogador);
        nomeJogadorTV.setText(nomeJogador);
        senhaJogadorTV.setText(senhaJogador);
        idJogoTV.setText(idJogo);
        nomeJogoTV.setText(nomeJogo);
        senhaJogoTV.setText(senhaJogo);
    }
}
