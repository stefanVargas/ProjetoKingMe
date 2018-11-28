package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;

public class MainActivity extends AppCompatActivity {

    private Button createButton;
    private Button enterButton;
    private Button tutorButton;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createButton = (Button) findViewById(R.id.Criarbutton);
        enterButton = (Button) findViewById(R.id.Enterbutton);
        tutorButton = (Button) findViewById(R.id.Tutorbutton);
        logo = (ImageView) findViewById(R.id.imgLogo);



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, CreateActivity.class);

                startActivity(in);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(MainActivity.this, EnterActivity.class);

                startActivity(in2);
            }
        });

        tutorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in3 = new Intent(MainActivity.this, Onboarding.class);

                startActivity(in3);
            }
        });





    }
}
