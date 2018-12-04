package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;

import br.com.senac.pdm.mepresidenta.lobby.EscolherJogoActivity;


public class EnterActivity extends AppCompatActivity {



    private EditText nick;
    private Button entrarButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        backButton = (Button) findViewById(R.id.Voltar2);
        entrarButton = (Button) findViewById(R.id.EnterButton2);

        nick = (EditText) findViewById(R.id.NickText2);


        entrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this,EscolherJogoActivity.class);
                intent.putExtra("nomeJogador",nick.getText().toString());
                intent.putExtra("atividadeJogo","com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities.InGameActivity");
                intent.putExtra("criar",false);
                startActivity(intent);
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
