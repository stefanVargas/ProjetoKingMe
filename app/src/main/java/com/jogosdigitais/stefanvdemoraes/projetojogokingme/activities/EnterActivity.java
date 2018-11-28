package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;


public class EnterActivity extends AppCompatActivity {


    private EditText nomePartida;
    private EditText senhaPartida;
    private EditText nick;
    private Button entrarButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        backButton = (Button) findViewById(R.id.Voltar2);
        entrarButton = (Button) findViewById(R.id.EnterButton2);
        nomePartida = (EditText) findViewById(R.id.NomePartText);
        senhaPartida = (EditText) findViewById(R.id.PassText);
        nick = (EditText) findViewById(R.id.NickText2);


        entrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EnterActivity.this, InGameActivity.class);

                startActivity(in);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inBack = new Intent(EnterActivity.this, MainActivity.class);

                startActivity(inBack);
            }
        });

    }
}
