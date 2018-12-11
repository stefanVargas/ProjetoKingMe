package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;

public class SobreActivity extends AppCompatActivity {

    private TextView title;
    private  TextView sobreApp;
    private TextView equipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        title = (TextView) findViewById(R.id.titletxtid);
        sobreApp = (TextView) findViewById(R.id.sobretxtid);


        title.setText(" SOBRE - KING ME/ Me Presidenta ");
        sobreApp.setLines(3);
        sobreApp.setText("Esse app foi desenvolvido como trabalho final da Aula de PROGRAMAÇÃO PARA DISPOSITIVOS MÓVEIS - Senac");

    }
}
