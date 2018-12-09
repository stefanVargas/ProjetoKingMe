package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.Intent;
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

    private List<String> cartas;
    private List<Setor> listDados;
    private String statusJogo;
    private String statusIdJogador;

    //classes do atualizador
    private Handler refresher = null;
    private Runnable refresherRunner;

    //DE QUANTO EM QAUNTO TEMPO EXECUTARÁ O CODIGO
    private int taxaAtualizacaoEmSegundos = 3;

    private int contador = 0;
    private TextView statusText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        this.jogador = new Jogador();
        setores = (RadioGroup) findViewById(R.id.setorGroup);
        personagens = (RadioGroup) findViewById(R.id.candidatosGroup);

        Retrofit retrofit = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create()) .build();

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

                Retrofit retrofit2 = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create()) .build();

                KingMeAPI api = retrofit2.create(KingMeAPI.class);

                Call<Jogo>  callStatus = api.obtemStatusJogo(id);
                Call<Jogador> verifica = api.verificaJogador(idPlayer);

                Callback<Jogador> callbackJogador= new Callback<Jogador>() {
                    @Override
                    public void onResponse(Call<Jogador> call, Response<Jogador> response) {

                        Jogador dadosJogador = response.body();


                        if(response.isSuccessful() && dadosJogador != null){

                            statusIdJogador = dadosJogador.getId().toString();

                        }


                    }

                    @Override
                    public void onFailure(Call<Jogador> call, Throwable t) {

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

                            statusJogo = dados.getStatusRodado();
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
                   personagens.setActivated(false);
                } else {
                    setores.setActivated(true);
                    personagens.setActivated(true);

                }


                //agenda a chamada da proxima atualização
                refresher.postDelayed(rThis,taxaAtualizacaoEmSegundos * 1000);
            }
        };

        //inicializa o atualizador
        statusText = (TextView) findViewById(R.id.statusTxt);
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

        final String idJogador = pref.getString("idJogador","");
        final String nomeJogador = pref.getString("nomeJogador","");
        final String senhaJogador = pref.getString("senhaJogador","");
        String idJogo = pref.getString("idJogo","");
        final String nomeJogo = pref.getString("nomeJogo","");
        final String senhaJogo = pref.getString("senhaJogo","");

        final TextView favCartas = findViewById(R.id.idFavs);
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

        KingMeAPI api = retrofit.create(KingMeAPI.class);

        Call<List<String>> callCartas = api.obterCartas(jogador);

        Callback<List<String>> cbCartas = new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                cartas = response.body();

                for(String cards : cartas){

                    favCartas.setText(favCartas.getText().toString() + " " + cards);
                }

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t){
                t.printStackTrace();
                showDialog("Falha ao obter o Resultado!", "ERRO");

            }
        };

        callCartas.enqueue(cbCartas);



        escolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit3 = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create()) .build();

                kingApi = retrofit3.create(KingMeAPI.class);

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

                if (!statusJogo.equals("S")) {

                    Intent intent = new Intent(InGameActivity.this, TabuleiroGameActivity.class);

                    intent.putExtra("nomeJogador", nomeJogador);
                    intent.putExtra("senhaJogador", senhaJogador);
                    intent.putExtra("idJogador", idJogador);
                    intent.putExtra("nomeJogo", nomeJogo);
                    intent.putExtra("senhaJogo", senhaJogo);
                    intent.putExtra("idJogo", idJogador);
                    intent.putExtra("atividadeJogo", "com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities.TabuleiroGameActivity");
                    startActivity(intent);
                }


            }

        });


    }

    private void showDialog(String val, String title) { AlertDialog.Builder builder = new
            AlertDialog.Builder(InGameActivity.this); builder.setMessage(val); builder.setTitle(title); builder.setCancelable(false); builder.setPositiveButton("OK", null); AlertDialog dialog = builder.create(); dialog.show();
    }

    private void checkAndPost(RadioButton candidato ) {

        Call<List<Setor>> callPersonagem = kingApi.colocaPersonagem(Long.valueOf(0),
                candidato.getText().toString(), jogador);
        String letra = candidato.getText().toString().substring(0, 1);


        if (candidato.isChecked()) {
            if (senador.isChecked()) {

                callPersonagem = kingApi.colocaPersonagem(Long.valueOf(4),
                        letra, jogador);

            } else if (governador.isChecked()) {

                callPersonagem = kingApi.colocaPersonagem(Long.valueOf(3),
                        letra, jogador);

            } else if (prefeito.isChecked()) {
                callPersonagem = kingApi.colocaPersonagem(Long.valueOf(2),
                        letra, jogador);


            } else if (vereador.isChecked()) {
                callPersonagem = kingApi.colocaPersonagem(Long.valueOf(1),
                        letra, jogador);


            } else {
                showDialog("Selecione um Setor ", "ERROR");

            }

            Callback<List<Setor>> cbOrdemSetores = new Callback<List<Setor>>() {
                @Override
                public void onResponse(Call<List<Setor>> call, Response<List<Setor>> response) {

                    listDados = response.body();

                }

                @Override
                public void onFailure(Call<List<Setor>> call, Throwable t) {

                    t.printStackTrace();
                    showDialog("Falha ao obter o Resultado!", "ERRO");

                }
            };

            callPersonagem.enqueue(cbOrdemSetores);


        }
    }

    private boolean isMyTurn(String meuID){

        return (statusIdJogador == meuID);


    }

    public void startRefresher(long delay) {
        if (refresher != null)
            refresher.postDelayed(refresherRunner, delay);
    }

    public void stopRefresher () {
        if (refresher != null)
            refresher.removeCallbacks(refresherRunner);
    }

    public List<Setor> getListDados() {
        return listDados;
    }
}
