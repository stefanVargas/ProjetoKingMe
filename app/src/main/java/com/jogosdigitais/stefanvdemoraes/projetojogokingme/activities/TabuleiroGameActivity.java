package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.API.KingMeAPI;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogador;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogo;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Setor;

import java.util.ArrayList;
import java.util.List;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OnClickListener;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class TabuleiroGameActivity extends AppCompatActivity {


    private ViewGroup setoresView;
    private Button promover;
    private Button votar;
    private String escolhido;
    private String presidente;

    private Jogo jogo;
    private Jogador jogador;
    private String statusJogo = "O";
    private String statusIdJogador;
    private List<Setor> setores;


    Retrofit retrofit = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create()) .build();

    SharedPreferences pref;

    //classes do atualizador
    private Handler refresher = null;
    private Runnable refresherRunner;

    //DE QUANTO EM QAUNTO TEMPO EXECUTARÁ O CODIGO
    private int taxaAtualizacaoEmSegundos = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablr_game);
        setoresView = (ViewGroup) findViewById(R.id.container);
        promover = (Button) findViewById(R.id.promobuttonid);
        votar = (Button) findViewById(R.id.votarbuttonid);

        this.jogador = new Jogador();
        this.jogo = new Jogo();
        pref = getApplicationContext().getSharedPreferences("jogo", 0); // 0 - for private mode
        startRefresher(0);

        final String idJogador = pref.getString("idJogador","");
        String nomeJogador = pref.getString("nomeJogador","");
        String senhaJogador = pref.getString("senhaJogador","");
        String idJogo = pref.getString("idJogo","");
        String nomeJogo = pref.getString("nomeJogo","");
        String senhaJogo = pref.getString("senhaJogo","");

        jogo.setId(Long.parseLong(idJogo));
        jogo.setNome(nomeJogo);
        jogo.setSenha(senhaJogo);

        jogador.setId(Long.parseLong(idJogador));
        jogador.setNome(nomeJogador);
        jogador.setSenha(senhaJogador);

        getRetroFitData(idJogador);

        //inicializa os atualizadores
        refresher = new Handler();
        refresherRunner = new Runnable() {
            @Override
            public void run() {
                final Runnable rThis = this;

                //AQUI VAI O CÓDIGO QUE SERA EXECUTADO DE TEMPO EM TEMPO




                //agenda a chamada da proxima atualização
                refresher.postDelayed(rThis,taxaAtualizacaoEmSegundos * 1000);
            }

        };

        //inicializa o atualizador
        startRefresher(0);
        promover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new OoOAlertDialog.Builder(TabuleiroGameActivity.this)
                        .setTitle("Promoção")
                        .setMessage("Deseja promover este Personagem?")
                        .setImage(R.drawable.faixa)
                        .setAnimation(Animation.POP)
                        .setPositiveButton("Cancelar", null)
                        .setNegativeButton("Promover", new OnClickListener() {
                            @Override
                            public void onClick() {
                                KingMeAPI api = retrofit.create(KingMeAPI.class);

                                Call<List<Setor>> callList = api.promovePersonagem(escolhido,jogador);

                                Toast.makeText(TabuleiroGameActivity.this, escolhido + " promovido!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();

            }
        });

        votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new OoOAlertDialog.Builder(TabuleiroGameActivity.this)
                        .setTitle("Votação")
                        .setMessage("Deseja votar neste Personagem para PRESIDENTE?")
                        .setImage(R.drawable.faixa)
                        .setAnimation(Animation.POP)
                        .setPositiveButton("Cancelar", null)
                        .setNegativeButton("Votar", new OnClickListener() {
                            @Override
                            public void onClick() {
                                KingMeAPI api = retrofit.create(KingMeAPI.class);

                                Call<List<Setor>> callList = api.votaKing(presidente, jogo);

                                Toast.makeText(TabuleiroGameActivity.this, escolhido + " promovido!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();

            }
        });


    }

    private void addItem(String textoDoTitulo, String candidato1, String candidato2, String candidato3,
                         String candidato4) {

        CardView cardView = (CardView) LayoutInflater.from(this)
                .inflate(R.layout.card, setoresView, false);
        cardView.setAlpha(0);
        TextView titulo = (TextView) cardView.findViewById(R.id.titulo);

        Button candb1 = (Button) cardView.findViewById(R.id.cand1);
        Button candb2 = (Button) cardView.findViewById(R.id.cand2);
        Button candb3 = (Button) cardView.findViewById(R.id.cand3);
        Button candb4 = (Button) cardView.findViewById(R.id.cand4);

        System.out.println("ÄAAAAAA");
        titulo.setText(textoDoTitulo);

        candb1.setText(candidato1);
        candb2.setText(candidato2);
        candb3.setText(candidato3);
        candb4.setText(candidato4);

        final String[] cndstr = {candidato1, candidato2, candidato3, candidato4};

        candb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               escolhido = cndstr[0];


            }
        });

        candb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolhido = cndstr[1];


            }
        });

        candb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolhido = cndstr[2];

            }
        });

        candb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolhido = cndstr[3];


            }
        });

        setoresView.addView(cardView);
    }

    private void getRetroFitData(String id){

        Long idKey = Long.valueOf(id);

        KingMeAPI apiLista = retrofit.create(KingMeAPI.class);

        statusTablr(apiLista,idKey);
        statusRodada(apiLista,jogo.getId());


    }

    public void startRefresher(long delay) {
        if (refresher != null)
            refresher.postDelayed(refresherRunner, delay);
    }

    public void stopRefresher () {
        if (refresher != null)
            refresher.removeCallbacks(refresherRunner);
    }

    public void statusTablr(KingMeAPI api, Long idPlayer){

        Call<List<Setor>> callList = api.statusTabuleiro(idPlayer);

        Callback<List<Setor>> callbackList = new Callback<List<Setor>>() {
            @Override
            public void onResponse(Call<List<Setor>> call, Response<List<Setor>> response) {

                setores = response.body();
                List<String> cands  = new ArrayList<>();
                cands.add("[ ]");
                cands.add("[ ]");
                cands.add("[ ]");
                cands.add("[ ]");

                for ( Setor str : setores){

                    if (str.getId() == 10 ) {

                        presidente = str.getPersonagens().get(0);

                    }

                    if(str.getId() == 1){
                        str.setNome("Vereador");
                    } else if(str.getId() == 2){
                        str.setNome("Prefeito");
                    } else if(str.getId() == 3){
                        str.setNome("Governador");
                    } else if(str.getId() == 4){
                        str.setNome("Senador");
                    } else if(str.getId() == 5) {
                        str.setNome("Ministro");
                    } else if(str.getId() == 10) {
                        str.setNome("PRESIDENTE");
                    }

                    for (String item : str.getPersonagens()){

                        cands.add(item);


                    }

                    addItem(str.getNome(), cands.get(0), cands.get(1), cands.get(3), cands.get(3));
                }

            }


            @Override
            public void onFailure(Call<List<Setor>> call, Throwable t) {
                System.out.println("Print Erro");
            }
        };

        callList.enqueue(callbackList);
    }

    public void verificaStatusJogador(KingMeAPI api, Long idjg, Long idPlayer){

        Call<Jogo>  callStatus = api.obtemStatusJogo(idjg);
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

            }
        };
        verifica.enqueue(callbackJogador);
    }

    public void statusRodada(KingMeAPI api, Long idjg){
        Call<Jogo>  callStatus = api.obtemStatusJogo(idjg);

        Callback<Jogo> callbackJogo= new Callback<Jogo>() {
            @Override
            public void onResponse(Call<Jogo> call, Response<Jogo> response) {

                Jogo dados = response.body();


                if(response.isSuccessful() && dados != null){

                    statusJogo = dados.getStatusRodado();
                    System.out.println("======= " + dados.getStatusRodado());

                }


            }

            @Override
            public void onFailure(Call<Jogo> call, Throwable t) {

                t.printStackTrace();

            }
        };

        callStatus.enqueue(callbackJogo);
    }

}



