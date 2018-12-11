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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InGameActivity extends AppCompatActivity {

    Retrofit retrofit2 = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create()) .build();

    Jogador jogador;
    KingMeAPI kingApi;
    SharedPreferences pref;

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

    //Botoes
    private Button escolher;
    private Button start;

    private List<String> cartas;
    private List<Setor> listDados;
    private List<String> persInSetores = new ArrayList<>();
    private String statusJogo = "X";
    private String statusIdJogador;

    //classes do atualizador
    private Handler refresher = null;
    private Runnable refresherRunner;

    //DE QUANTO EM QAUNTO TEMPO EXECUTARÁ O CODIGO
    private int taxaAtualizacaoEmSegundos = 3;

    private int contador = 0;
    private TextView statusText;
    private TextView vezText; boolean vez = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        this.jogador = new Jogador();
        setores = (RadioGroup) findViewById(R.id.setorGroup);
        personagens = (RadioGroup) findViewById(R.id.candidatosGroup);
        start = (Button) findViewById(R.id.startbuttonid);



        Retrofit retrofit = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create()) .build();

        //inicializa os atualizadores
        refresher = new Handler();
        refresherRunner = new Runnable() {
            @Override
            public void run() {
                final Runnable runThis = this;

                final SharedPreferences pref = getApplicationContext().getSharedPreferences("jogo", 0); // 0 - for private mode

                //AQUI VAI O CÓDIGO QUE SERA EXECUTADO DE TEMPO EM TEMPO

                String idJogo = pref.getString("idJogo","");
                Long id = Long.parseLong(idJogo);

                String idJogador = pref.getString("idJogador","");
                Long idPlayer = Long.parseLong(idJogador);



                KingMeAPI api = retrofit2.create(KingMeAPI.class);

                statusRodada(id);
                statusTablr(api, idPlayer);
                verificaStatusJogador(api, id, idPlayer);

                if(statusJogo.equals("J")){

                Intent intent = new Intent(InGameActivity.this, TabuleiroGameActivity.class);

                intent.putExtra("nomeJogador", idJogador);
                intent.putExtra("idJogo", idJogo);
                intent.putExtra("atividadeJogo", "com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities.TabuleiroGameActivity");
                startActivity(intent);

                }

                if (!vez){
                   setores.setClickable(false);
                   setores.setVisibility(View.INVISIBLE);
                   personagens.setClickable(false);
                   personagens.setVisibility(View.INVISIBLE);

                } else {
                    checaPersonagens(persInSetores);
                    setores.setClickable(true);
                    setores.setVisibility(View.VISIBLE);
                    personagens.setClickable(true);
                    personagens.setVisibility(View.VISIBLE);


                }


                //agenda a chamada da proxima atualização
                refresher.postDelayed(runThis,taxaAtualizacaoEmSegundos * 1000);
            }
        };

        //inicializa o atualizador
        pref = getApplicationContext().getSharedPreferences("jogo", 0); // 0 - for private mode
        statusText = (TextView) findViewById(R.id.statusTxt);
        vezText = (TextView) findViewById(R.id.vezJogadorid);
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


        final String idJogador = pref.getString("idJogador","");
        final String nomeJogador = pref.getString("nomeJogador","");
        final String senhaJogador = pref.getString("senhaJogador","");
        final String idJogo = pref.getString("idJogo","");
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


        jogador.setId(Long.parseLong(idJogador));
        jogador.setNome(nomeJogador);
        jogador.setSenha(senhaJogador);
        if (jogador.getPontuacao() == null) {
            Long pnt = Long.valueOf(00000);
            jogador.setPontuacao(pnt);
        }

        verificaFavCartas(kingApi, favCartas);





        escolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit3 = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create()) .build();

                kingApi = retrofit3.create(KingMeAPI.class);

                if (statusJogo.equals("S")) {
                    checaPersonagens(persInSetores);
                    String[] vec = new String[2];
                    if (amoedo.isChecked()) vec = checkAndPost(amoedo);
                    if (bolso.isChecked()) vec = checkAndPost(bolso);
                    if (ciraoGomes.isChecked()) vec = checkAndPost(ciraoGomes);
                    if (dilma.isChecked()) vec = checkAndPost(dilma);
                    if (eymael.isChecked()) vec = checkAndPost(eymael);
                    if (fHaddad.isChecked()) vec = checkAndPost(fHaddad);
                    if (gAlckmin.isChecked()) vec = checkAndPost(gAlckmin);
                    if (itamar.isChecked()) vec = checkAndPost(itamar);
                    if (lucHuck.isChecked())  vec = checkAndPost(lucHuck);
                    if (marina.isChecked()) vec = checkAndPost(marina);
                    if (neymar.isChecked())  vec = checkAndPost(neymar);
                    if (oresQuercia.isChecked())  vec = checkAndPost(oresQuercia);
                    if (pMaluf.isChecked()) vec = checkAndPost(pMaluf);

                    Call<List<Setor>> callPersonagem = kingApi.colocaPersonagem(Long.parseLong(vec[0]),
                            vec[1], jogador);

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
                    //checaPersonagens(persInSetores);
                    callPersonagem.enqueue(cbOrdemSetores);
                    statusRodada(Long.valueOf(idJogo));

                }
                else if (!statusJogo.equals("S")) {

                    showDialog("Aperte Start para começar a partida!", "ERRO");
                }
            }
        });


    } // ---- END onCreate ----

    private void showDialog(String val, String title) { AlertDialog.Builder builder = new
            AlertDialog.Builder(InGameActivity.this); builder.setMessage(val); builder.setTitle(title); builder.setCancelable(false); builder.setPositiveButton("OK", null); AlertDialog dialog = builder.create(); dialog.show();
    }

    private String[] checkAndPost(RadioButton candidato) {


        String letra = candidato.getText().toString().substring(0, 1).toUpperCase();
        String[] ret = new  String[2];
        ret[1] = letra;

        if (candidato.isChecked()) {
            if (senador.isChecked()) {
                ret[0] = "4";
                System.out.println( jogador.getNome() + " senador -----------p-p-p-p-p-p-p-p-p-  " + letra);


            } else if (governador.isChecked()) {
                ret[0] = "3";
                System.out.println( jogador.getNome() + " governador ----------p-p-p-p-p-p-p-p-p--  " + letra);


            } else if (prefeito.isChecked()) {
                ret[0] = "2";
                System.out.println( jogador.getNome() + " prefeito ---------p-p-p-p-p-p-p-p-p---  " + letra);


            } else if (vereador.isChecked()) {
                ret[0] = "1";
                System.out.println( jogador.getNome() + " vereador --------p-p-p-p-p-p-p-p-p----  " + letra);



            } else {
                showDialog("Selecione um Setor e um Candidato", "ERROR");
                return null;

            }
            return ret;
        }
        return ret;
    }

    private boolean isMyTurn(String meuID){

        return (statusIdJogador.equals(meuID));

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

    public void checaPersonagens(List<String> lista) {

        for (String letra : lista){
            if (letra.equals("A")){
                amoedo.setClickable(false);
                amoedo.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("B")){
                bolso.setClickable(false);
                bolso.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("C")){
                ciraoGomes.setClickable(false);
                ciraoGomes.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("D")){
                dilma.setClickable(false);
                dilma.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("E")){
                eymael.setClickable(false);
                eymael.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("F")){
                fHaddad.setClickable(false);
                fHaddad.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("G")){
                gAlckmin.setClickable(false);
                gAlckmin.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("I")){
                itamar.setClickable(false);
                itamar.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("L")){
                lucHuck.setClickable(false);
                lucHuck.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("M")){
                marina.setClickable(false);
                marina.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("N")){
                neymar.setClickable(false);
                neymar.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("O")){
                oresQuercia.setClickable(false);
                oresQuercia.setVisibility(View.INVISIBLE);
            }
            else if (letra.equals("P")){
                pMaluf.setClickable(false);
                pMaluf.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void statusTablr(KingMeAPI api, Long idPlayer){

        Call<List<Setor>> callList = api.statusTabuleiro(idPlayer);

        Callback<List<Setor>> callbackList = new Callback<List<Setor>>() {
            @Override
            public void onResponse(Call<List<Setor>> call, Response<List<Setor>> response) {

                listDados = response.body();
                persInSetores.clear();

                for ( Setor s : listDados){
                    System.out.println(s.getId() + " " + s.getPersonagens());

                    if (s.getId() >= 1 && s.getId() <= 4) {
                        if (s.getPersonagens().size() == 4) {
                            //TODO travar setores
                            if (s.getId() == 1){
                                vereador.setEnabled(false);
                                vereador.setVisibility(View.INVISIBLE);
                            }
                            if (s.getId() == 2){
                                prefeito.setEnabled(false);
                                prefeito.setVisibility(View.INVISIBLE);
                            }
                            if (s.getId() == 3){
                                governador.setEnabled(false);
                                governador.setVisibility(View.INVISIBLE);
                            }
                            if (s.getId() == 4){
                                senador.setEnabled(false);
                                senador.setVisibility(View.INVISIBLE);
                            }


                        }

                        for (int i = 0; i < s.getPersonagens().size(); i++) {
                            persInSetores.addAll(i, s.getPersonagens());
                        }
                    }
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

        Call<Jogador> verifica = api.verificaJogador(idPlayer);

        Callback<Jogador> callbackJogador= new Callback<Jogador>() {
            @Override
            public void onResponse(Call<Jogador> call, Response<Jogador> response) {

                Jogador dadosJogador = response.body();


                if(response.isSuccessful() && dadosJogador != null){

                    statusIdJogador = dadosJogador.getId().toString();
                    vez = isMyTurn(pref.getString("idJogador",""));

                    vezText.setText(statusIdJogador + " É Sua Vez: " + vez);

                }
            }

            @Override
            public void onFailure(Call<Jogador> call, Throwable t) {

                t.printStackTrace();
                showDialog("Falha ao obter o Resultado!", "ERRO");

            }
        };
        verifica.enqueue(callbackJogador);
    }

    public void statusRodada(Long idjg){
        KingMeAPI api = retrofit2.create(KingMeAPI.class);
        Call<Jogo>  callStatus = api.obtemStatusJogo(idjg);

        Callback<Jogo> callbackJogo= new Callback<Jogo>() {
            @Override
            public void onResponse(Call<Jogo> call, Response<Jogo> response) {

                Jogo dados = response.body();


                if(response.isSuccessful() && dados != null){

                    statusJogo = dados.getStatusRodado();
                    System.out.println("======= " + dados.getStatusRodado());
                    statusText.setText("Jogo executando - Status: " + dados.getStatusRodado());

                }


            }

            @Override
            public void onFailure(Call<Jogo> call, Throwable t) {

                t.printStackTrace();
                showDialog("Falha ao obter o Resultado!", "ERRO");

            }
        };

        callStatus.enqueue(callbackJogo);
    }

    public void verificaFavCartas(KingMeAPI api, final TextView favCartas){
        api = retrofit2.create(KingMeAPI.class);

        Call<List<String>> callCartas = api.obterCartas(jogador);

        Callback<List<String>> cbCartas = new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                cartas = response.body();

                for(String cards : cartas)
                    favCartas.setText(favCartas.getText().toString() + " " + cards);

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t){
                t.printStackTrace();
                showDialog("Falha ao obter o Resultado!", "ERRO");

            }
        };

        callCartas.enqueue(cbCartas);
    }

}
