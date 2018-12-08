package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.API.KingMeAPI;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogo;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Setor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class TabuleiroGameActivity extends AppCompatActivity {


    private ViewGroup setoresView;

    private Jogo jogo;
    private List<Setor> setores;

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

        SharedPreferences pref = getApplicationContext().getSharedPreferences("jogo", 0); // 0 - for private mode

        final String idJogador = pref.getString("idJogador","");
        final String nomeJogador = pref.getString("nomeJogador","");
        final String senhaJogador = pref.getString("senhaJogador","");

        //inicializa os atualizadores
        refresher = new Handler();
        refresherRunner = new Runnable() {
            @Override
            public void run() {
                final Runnable rThis = this;

                //AQUI VAI O CÓDIGO QUE SERA EXECUTADO DE TEMPO EM TEMPO
                getRetroFitData(idJogador);



                //agenda a chamada da proxima atualização
                refresher.postDelayed(rThis,taxaAtualizacaoEmSegundos * 1000);
            }
        };

        //inicializa o atualizador
        startRefresher(0);


    }

    private void addItem(String textoDoTitulo, String candidato1, String candidato2, String candidato3,
                         String candidato4) {
        CardView cardView = (CardView) LayoutInflater.from(this)
                .inflate(R.layout.card, setoresView, false);
        TextView titulo = (TextView) cardView.findViewById(R.id.titulo);

        TextView cand1 = (TextView) cardView.findViewById(R.id.cand1);
        TextView cand2 = (TextView) cardView.findViewById(R.id.cand2);
        TextView cand3 = (TextView) cardView.findViewById(R.id.cand3);
        TextView cand4 = (TextView) cardView.findViewById(R.id.cand4);

        titulo.setText(textoDoTitulo);

        cand1.setText(candidato1);
        cand2.setText(candidato2);
        cand3.setText(candidato3);
        cand4.setText(candidato4);

        setoresView.addView(cardView);
    }

    private void getRetroFitData(String id){

        Long idKey = Long.valueOf(id);

        try {


            System.out.println("Go!!");


        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println(" --------- ERROU!!! -------");; return;
        }

        Retrofit retrofit = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create()) .build();

        KingMeAPI apiLista = retrofit.create(KingMeAPI.class);

        Call<List<Setor>> callList = apiLista.statusTabuleiro(idKey);

        Callback<List<Setor>> callbackList = new Callback<List<Setor>>() {
            @Override
            public void onResponse(Call<List<Setor>> call, Response<List<Setor>> response) {

                List<Setor> dadosLista = response.body();


                if(response.isSuccessful() && dadosLista != null){

                    for(Setor str : dadosLista){

                        String nome = str.getNome();
                        String cand1 = str.getPersonagens().get(0);
                        String cand2 = str.getPersonagens().get(1);
                        String cand3 = str.getPersonagens().get(2);
                        String cand4 = str.getPersonagens().get(3);


                        addItem(nome, cand1, cand2, cand3, cand4);
                    }

                }


            }

            @Override
            public void onFailure(Call<List<Setor>> call, Throwable t) {

                System.out.println("Faio!");

            }


        };

        callList.enqueue(callbackList);

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



