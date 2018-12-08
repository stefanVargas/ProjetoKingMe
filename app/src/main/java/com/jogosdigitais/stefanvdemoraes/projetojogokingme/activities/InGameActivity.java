package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.API.KingMeAPI;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogador;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogo;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Setor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InGameActivity extends AppCompatActivity {

    Jogador jogador;
    KingMeAPI kingApi;

    //Groups
    private RadioGroup setores;
    private RadioGroup personagens;

    //Setores
    private RadioButton senador;
    private RadioButton governador;
    private RadioButton prefeito;
    private RadioButton vereador;

    //Personagens
    private RadioButton amoedo;
    private RadioButton bolso;
    private RadioButton ciraoGomes;
    private RadioButton dilma;
    private RadioButton eymael;
    private RadioButton fHaddad;
    private RadioButton gAlckmin;
    private RadioButton itamar;
    private RadioButton lucHuck;
    private RadioButton marina;
    private RadioButton neymar;
    private RadioButton oresQuercia;
    private RadioButton pMaluf;

    private Button escolher;

    private ArrayList cartas;
    private String statusJogo;
    private String statusIdJogador;

    //classes do atualizador
    private Handler refresher = null;
    private Runnable refresherRunner;

    //DE QUANTO EM QAUNTO TEMPO EXECUTARÁ O CODIGO
    private int taxaAtualizacaoEmSegundos = 5;

    private int contador = 0;
    private TextView statusText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        this.jogador = new Jogador();
        statusText = (TextView) findViewById(R.id.statusTxt);
        setores = (RadioGroup) findViewById(R.id.setorGroup);
        personagens = (RadioGroup) findViewById(R.id.candidatosGroup);

        //inicializa os atualizadores
        refresher = new Handler();
        refresherRunner = new Runnable() {
            @Override
            public void run() {
                final Runnable rThis = this;

                SharedPreferences pref = getApplicationContext().getSharedPreferences("jogo", 0); // 0 - for private mode

                //AQUI VAI O CÓDIGO QUE SERA EXECUTADO DE TEMPO EM TEMPO
                String idJogo = pref.getString("idJogo","");
                Long id = Long.valueOf(idJogo);

                String idJogador = pref.getString("idJogador","");
                Long idPlayer = Long.valueOf(idJogador);


                Retrofit retrofit = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create()) .build();

                KingMeAPI api = retrofit.create(KingMeAPI.class);

                Call<Jogo>  callStatus = api.obtemStatusJogo(id);
                Call<Jogador> verifica = api.verificaJogador(idPlayer);

                Callback<Jogador> callbackJogador= new Callback<Jogador>() {
                    @Override
                    public void onResponse(Call<Jogador> call, Response<Jogador> response) {

                        Jogador dadosJogador = response.body();


                        if(response.isSuccessful() && dadosJogador != null){

                            statusIdJogador = dadosJogador.getId().toString();
                            statusText.setText("Jogo executando - Status: " + statusJogo);



                        }


                    }

                    @Override
                    public void onFailure(Call<Jogo> call, Throwable t) {

                        t.printStackTrace();
                        showDialog("Falha ao obter o Resultado!", "ERRO");

                    }
                };
                verifica.enqueue(callbackJogador);

                Callback<Jogo> callbackJogo= new Callback<Jogo>() {
                    @Override
                    public void onResponse(Call<Jogo> call, Response<Jogo> response) {

                        Jogo dados = response.body();


                        if(response.isSuccessful() && dados != null){

                            statusJogo = dados.getStatus();
                            statusText.setText("Jogo executando - Status: " + statusJogo);



                        }


                    }

                    @Override
                    public void onFailure(Call<Jogo> call, Throwable t) {

                        t.printStackTrace();
                        showDialog("Falha ao obter o Resultado!", "ERRO");

                    }
                };

                callStatus.enqueue(callbackJogo);

                boolean vez = isMyTurn(idJogador);

                if (!vez){
                   setores.setActivated(false);
                } else {
                    setores.setActivated(true);
                }


                //agenda a chamada da proxima atualização
                refresher.postDelayed(rThis,taxaAtualizacaoEmSegundos * 1000);
            }
        };

        //inicializa o atualizador
        startRefresher(0);

        this.senador = (RadioButton) findViewById(R.id.senadorRID);
        this.governador = (RadioButton) findViewById(R.id.governadorRID);
        this.prefeito = (RadioButton) findViewById(R.id.prefeitoRID);
        this.vereador = (RadioButton) findViewById(R.id.vereadorRID);

        this.amoedo = (RadioButton) findViewById(R.id.amoedoRID);
        this.bolso = (RadioButton) findViewById(R.id.bolsoRiD);
        this.ciraoGomes = (RadioButton) findViewById(R.id.ciroRID);
        this.dilma = (RadioButton) findViewById(R.id.dilmaRID);
        this.eymael = (RadioButton) findViewById(R.id.eymaelRID);
        this.fHaddad = (RadioButton) findViewById(R.id.ferHadRID);
        this.gAlckmin = (RadioButton) findViewById(R.id.gerAlckRID);
        this.itamar = (RadioButton) findViewById(R.id.itamarRID);
        this.lucHuck = (RadioButton) findViewById(R.id.lucHuckRID);
        this.marina = (RadioButton) findViewById(R.id.marinaRID);
        this.neymar = (RadioButton) findViewById(R.id.neyRID);
        this.oresQuercia = (RadioButton) findViewById(R.id.oresQuerRID);
        this.pMaluf = (RadioButton) findViewById(R.id.pmalufRID);

        this.escolher = (Button) findViewById(R.id.escolherBtn);

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

        idJogadorTV.setText("ID do Jogador: " + idJogador);
        nomeJogadorTV.setText("Nome do Jogador: " + nomeJogador);
        senhaJogadorTV.setText("Senha do Jogador: " + senhaJogador);
        idJogoTV.setText("ID do Jogo: " + idJogo);
        nomeJogoTV.setText("Nome do Jogo: " + nomeJogo);
        senhaJogoTV.setText("Senha do Jogo: " + senhaJogo);


        jogador.setId(Long.valueOf(idJogador));
        jogador.setNome(nomeJogador);
        jogador.setSenha(senhaJogador);
        if (jogador.getPontuacao() == null) {
            Long pnt = Long.valueOf(00000);
            jogador.setPontuacao(pnt);
        }

        escolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create()) .build();

                kingApi = retrofit.create(KingMeAPI.class);

                checkAndPost(amoedo);
                checkAndPost(bolso);
                checkAndPost(ciraoGomes);
                checkAndPost(dilma);
                checkAndPost(eymael);
                checkAndPost(fHaddad);
                checkAndPost(gAlckmin);
                checkAndPost(itamar);
                checkAndPost(lucHuck);
                checkAndPost(marina);
                checkAndPost(neymar);
                checkAndPost(oresQuercia);
                checkAndPost(pMaluf);


            }

        });


    }

    private void showDialog(String val, String title) { AlertDialog.Builder builder = new
            AlertDialog.Builder(InGameActivity.this); builder.setMessage(val); builder.setTitle(title); builder.setCancelable(false); builder.setPositiveButton("OK", null); AlertDialog dialog = builder.create(); dialog.show();
    }

    private void checkAndPost(RadioButton candidato ) {

        Call<List<Setor>> callPersonagem;


        if (candidato.isChecked()) {
            if (senador.isChecked()) {

                callPersonagem = kingApi.colocaPersonagem(Long.valueOf(4),
                        candidato.getText().toString(), jogador);

            } else if (governador.isChecked()) {

                callPersonagem = kingApi.colocaPersonagem(Long.valueOf(3),
                        candidato.getText().toString(), jogador);

            } else if (prefeito.isChecked()) {
                callPersonagem = kingApi.colocaPersonagem(Long.valueOf(2),
                        candidato.getText().toString(), jogador);


            } else if (vereador.isChecked()) {
                callPersonagem = kingApi.colocaPersonagem(Long.valueOf(1),
                        candidato.getText().toString(), jogador);


            } else {
                showDialog("Selecione um Setor ", "ERROR");

            }

        }
    }

    private boolean isMyTurn(String meuID){

        return (statusJogo == "S" && statusIdJogador == meuID);

    }

    public void startRefresher(long delay) {
        if (refresher != null)
            refresher.postDelayed(refresherRunner, delay);
    }

    public void stopRefresher () {
        if (refresher != null)
            refresher.removeCallbacks(refresherRunner);
    }

}
