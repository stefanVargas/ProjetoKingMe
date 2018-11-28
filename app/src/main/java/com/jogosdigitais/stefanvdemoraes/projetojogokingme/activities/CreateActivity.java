package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;


public class CreateActivity extends AppCompatActivity {

    private EditText nomePartida;
    private EditText senhaPartida;
    private EditText nick;
    private Button criarButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        backButton = (Button) findViewById(R.id.Voltar1);
        criarButton = (Button) findViewById(R.id.Criarbutton2);
        nomePartida = (EditText) findViewById(R.id.PartidaText);
        senhaPartida = (EditText) findViewById(R.id.SenhaText);
        nick = (EditText) findViewById(R.id.NickText);



        criarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(CreateActivity.this, InGameActivity.class);

                startActivity(in);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inBack = new Intent(CreateActivity.this, MainActivity.class);

                startActivity(inBack);
            }
        });



    }
}
