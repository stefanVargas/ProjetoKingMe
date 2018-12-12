package com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.API.KingMeAPI;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.R;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogador;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogo;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Setor;

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
    private String statusJogo = "X";
    private String statusRodada = "O";
    private String statusIdJogador;
    private List<Setor> setores;

    //Personagens
    private RadioGroup personagens;
    private RadioButton amoedo;
    private RadioButton bolso;
    private RadioButton ciro;
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

    private TextView vezTxt;
    private boolean ehVez;
    private boolean changed = false;

    Retrofit retrofit = new Retrofit.Builder() .baseUrl("https://mepresidenta.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create()) .build();


    SharedPreferences pref;

    //classes do atualizador
    private Handler refresher = null;
    private Runnable refresherRunner;

    //DE QUANTO EM QAUNTO TEMPO EXECUTARÁ O CODIGO
    private int taxaAtualizacaoEmSegundos = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablr_game);

        setoresView = (ViewGroup) findViewById(R.id.container);
        promover = (Button) findViewById(R.id.promobuttonid);
        votar = (Button) findViewById(R.id.votarbuttonid);
        vezTxt = (TextView) findViewById(R.id.vezid2);


        this.jogador = new Jogador();
        this.jogo = new Jogo();
        pref = getApplicationContext().getSharedPreferences("jogo", 0); // 0 - for private mode

        //RADIOS
        this.personagens = (RadioGroup) findViewById(R.id.candidatosGroup2);
        this.amoedo = (RadioButton) findViewById(R.id.amoedoRID2);
        this.bolso = (RadioButton) findViewById(R.id.bolsoRiD2);
        this.ciro = (RadioButton) findViewById(R.id.ciroRID2);
        this.dilma = (RadioButton) findViewById(R.id.dilmaRID2);
        this.eymael = (RadioButton) findViewById(R.id.eymaelRID2);
        this.fHaddad = (RadioButton) findViewById(R.id.ferHadRID2);
        this.gAlckmin = (RadioButton) findViewById(R.id.gerAlckRID2);
        this.itamar = (RadioButton) findViewById(R.id.itamarRID2);
        this.lucHuck = (RadioButton) findViewById(R.id.lucHuckRID2);
        this.marina = (RadioButton) findViewById(R.id.marinaRID2);
        this.neymar = (RadioButton) findViewById(R.id.neyRID2);
        this.oresQuercia = (RadioButton) findViewById(R.id.oresQuerRID2);
        this.pMaluf = (RadioButton) findViewById(R.id.pmalufRID2);

        promover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amoedo.isChecked()) promoverPersonagem(amoedo);
                if (bolso.isChecked()) promoverPersonagem(bolso);
                if (ciro.isChecked()) promoverPersonagem(ciro);
                if (dilma.isChecked()) promoverPersonagem(dilma);

                if (eymael.isChecked()) promoverPersonagem(eymael);
                if (fHaddad.isChecked()) promoverPersonagem(fHaddad);
                if (gAlckmin.isChecked()) promoverPersonagem(gAlckmin);
                if (itamar.isChecked()) promoverPersonagem(itamar);

                if (lucHuck.isChecked()) promoverPersonagem(lucHuck);
                if (marina.isChecked()) promoverPersonagem(marina);
                if (neymar.isChecked()) promoverPersonagem(neymar);
                if (oresQuercia.isChecked()) promoverPersonagem(oresQuercia);
                if (pMaluf.isChecked()) promoverPersonagem(pMaluf);


                verificaStatusJogador(jogo.getId(),jogador.getId());



            }
        });



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


        //inicializa os atualizadores
        refresher = new Handler();
        refresherRunner = new Runnable() {
            @Override
            public void run() {
                final Runnable rThis = this;

                //AQUI VAI O CÓDIGO QUE SERA EXECUTADO DE TEMPO EM TEMPO

                if (statusJogo.equals("E")){
                    Intent intent = new Intent(TabuleiroGameActivity.this, EndActivity.class);

                    stopRefresher();
                    intent.putExtra("atividadeJogo", "com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities.EndActivity");
                    startActivity(intent);
                    return;
                }
                KingMeAPI api2 = retrofit.create(KingMeAPI.class);

                verificaStatusJogador(jogo.getId(), jogador.getId());
                statusTablr(api2, jogador.getId());
                statusRodada(jogador.getId());

                if(statusRodada.equals("V")){
                    votar.setVisibility(View.VISIBLE);
                    promover.setVisibility(View.INVISIBLE);

                    if(ehVez ==false){
                        votar.setEnabled(false);
                        votar.setText("Espere...");

                    }else{
                        votar.setEnabled(true);
                        votar.setText("Votar");
                    }
                } else if (statusRodada.equals("S")){
                    votar.setVisibility(View.VISIBLE);
                    promover.setVisibility(View.INVISIBLE);
                    votar.setText("Recomeçar");

                }

                if(!ehVez) {
                    personagens.setEnabled(false);
                    promover.setEnabled(false);
                    promover.setText("Espere...");
                } else {
                    personagens.setEnabled(true);
                    promover.setEnabled(true);
                    promover.setText("Promove");
                }

                if(changed == false || ehVez == false) {
                    setCargoName(amoedo, jogador.getId());
                    setCargoName(bolso, jogador.getId());
                    setCargoName(ciro, jogador.getId());
                    setCargoName(dilma, jogador.getId());

                    setCargoName(eymael, jogador.getId());
                    setCargoName(fHaddad, jogador.getId());
                    setCargoName(gAlckmin, jogador.getId());
                    setCargoName(itamar, jogador.getId());

                    setCargoName(lucHuck, jogador.getId());
                    setCargoName(marina, jogador.getId());
                    setCargoName(neymar, jogador.getId());
                    setCargoName(oresQuercia, jogador.getId());
                    setCargoName(pMaluf, jogador.getId());
                    changed = true;
                }


                //agenda a chamada da proxima atualização
                refresher.postDelayed(rThis,taxaAtualizacaoEmSegundos * 1000);
            }

        };

        //inicializa o atualizador
        startRefresher(0);

        votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KingMeAPI api = retrofit.create(KingMeAPI.class);
                statusTablr(api, jogador.getId());

                if (statusRodada.equals("S")){

                    Intent intent = new Intent(TabuleiroGameActivity.this, InGameActivity.class);

                    intent.putExtra("nomeJogador", jogador.getId().toString());
                    intent.putExtra("idJogo", jogo.getId().toString());
                    intent.putExtra("atividadeJogo", "com.jogosdigitais.stefanvdemoraes.projetojogokingme.activities.InGameActivity");
                    stopRefresher();
                    startActivity(intent);

                } else {


                    new OoOAlertDialog.Builder(TabuleiroGameActivity.this)
                            .setTitle("Votação")
                            .setMessage("Deseja votar neste Personagem para PRESIDENTE?")
                            .setImage(R.drawable.faixa)
                            .setAnimation(Animation.POP)
                            .setPositiveButton("Cassar", new OnClickListener() {
                                @Override
                                public void onClick() {

                                    KingMeAPI api = retrofit.create(KingMeAPI.class);

                                    Call<List<Setor>> callList = api.votaKing("N", jogador);

                                    Callback<List<Setor>> cbRei = new Callback<List<Setor>>() {
                                        @Override
                                        public void onResponse(Call<List<Setor>> call, Response<List<Setor>> response) {
                                            setores = response.body();
                                        }

                                        @Override
                                        public void onFailure(Call<List<Setor>> call, Throwable t) {

                                        }
                                    };
                                    callList.enqueue(cbRei);

                                    Toast.makeText(TabuleiroGameActivity.this, presidente + " Cassado!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Votar", new OnClickListener() {
                                @Override
                                public void onClick() {
                                    KingMeAPI api = retrofit.create(KingMeAPI.class);

                                    Call<List<Setor>> callList = api.votaKing("S", jogador);

                                    Callback<List<Setor>> cbRei = new Callback<List<Setor>>() {
                                        @Override
                                        public void onResponse(Call<List<Setor>> call, Response<List<Setor>> response) {
                                            setores = response.body();
                                        }

                                        @Override
                                        public void onFailure(Call<List<Setor>> call, Throwable t) {

                                        }
                                    };
                                    callList.enqueue(cbRei);

                                    Toast.makeText(TabuleiroGameActivity.this, presidente + " ELEITO!!", Toast.LENGTH_SHORT).show();


                                }
                            })
                            .build();

                }

            }
        });


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

                for ( Setor str : setores){

                    if (str.getId() == 10 ) {

                        presidente = str.getPersonagens().get(0);

                    }
                    for ( Setor s : setores) {
                        System.out.println(s.getId() + " " + s.getPersonagens());
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

    public void verificaStatusJogador(Long idjg, Long idPlayer){

        KingMeAPI api = retrofit.create(KingMeAPI.class);

        Call<Jogador> verifica = api.verificaJogador(idPlayer);

        Callback<Jogador> callbackJogador= new Callback<Jogador>() {
            @Override
            public void onResponse(Call<Jogador> call, Response<Jogador> response) {

                Jogador dadosJogador = response.body();


                if(response.isSuccessful() && dadosJogador != null){

                    statusIdJogador = dadosJogador.getId().toString();

                    ehVez = isMyTurn(jogador.getId().toString());
                    System.out.println(statusIdJogador + " ----- " + jogador.getId().toString());
                    vezTxt.setText(" É Sua Vez: " + ehVez);

                }
            }

            @Override
            public void onFailure(Call<Jogador> call, Throwable t) {

                t.printStackTrace();

            }
        };
        verifica.enqueue(callbackJogador);
    }

    public void statusRodada(Long idjg){

        KingMeAPI api = retrofit.create(KingMeAPI.class);

        Call<Jogo>  callStatus = api.obtemStatusJogo(idjg);

        Callback<Jogo> callbackJogo= new Callback<Jogo>() {
            @Override
            public void onResponse(Call<Jogo> call, Response<Jogo> response) {

                Jogo dados = response.body();


                if(response.isSuccessful() && dados != null){

                    statusJogo = dados.getStatus();
                    statusRodada = dados.getStatusRodado();
                    System.out.println("========> " + dados.getStatusRodado());

                }


            }

            @Override
            public void onFailure(Call<Jogo> call, Throwable t) {

                t.printStackTrace();

            }
        };

        callStatus.enqueue(callbackJogo);
    }

    public void setCargoName(final RadioButton candidato, Long idPlayer) {

        KingMeAPI api = retrofit.create(KingMeAPI.class);

        final String letra = candidato.getText().toString().substring(0, 1).toUpperCase();

        Call<List<Setor>> callList = api.statusTabuleiro(idPlayer);

        Callback<List<Setor>> callbackList = new Callback<List<Setor>>() {
            @Override
            public void onResponse(Call<List<Setor>> call, Response<List<Setor>> response) {

                setores = response.body();

                for ( Setor str : setores){


                    for (String item : str.getPersonagens()){

                        if(str.getId() == 1 && item.equals(letra)){
                            candidato.setText(letra + " Cargo: Vereador");
                        } else if(str.getId() == 2 && item.equals(letra)){
                            candidato.setText(letra + " Cargo: Prefeito");
                        } else if(str.getId() == 3 && item.equals(letra)){
                            candidato.setText(letra + " Cargo: Governador");
                        } else if(str.getId() == 4 && item.equals(letra)){
                            candidato.setText(letra + " Cargo: Senador");
                        } else if(str.getId() == 5 && item.equals(letra)) {
                            candidato.setText(letra + " Cargo: Ministro");
                        } else if(str.getId() == 10 && item.equals(letra)) {
                            candidato.setText(letra + " PRESIDENTE");
                        } else if(str.getId() == 0 && item.equals(letra)) {
                            candidato.setText(letra + " Povão");
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

    public void promoverPersonagem(final RadioButton candidato){

        escolhido = candidato.getText().toString().substring(0, 1).toUpperCase();


        if(candidato.isChecked()){
            new OoOAlertDialog.Builder(TabuleiroGameActivity.this)
                    .setTitle("Promoção de " + escolhido)
                    .setMessage("Deseja promover este Personagem?")
                    .setImage(R.drawable.faixa)
                    .setAnimation(Animation.POP)
                    .setPositiveButton("Cancelar", null)
                    .setNegativeButton("Promover", new OnClickListener() {
                        @Override
                        public void onClick() {
                            KingMeAPI api = retrofit.create(KingMeAPI.class);

                            if (canPromote(escolhido)) {

                                Call<List<Setor>> callList = api.promovePersonagem(escolhido, jogador);

                                Callback<List<Setor>> callbackpromo = new Callback<List<Setor>>() {
                                    @Override
                                    public void onResponse(Call<List<Setor>> call, Response<List<Setor>> response) {
                                        setores = response.body();

                                    }

                                    @Override
                                    public void onFailure(Call<List<Setor>> call, Throwable t) {

                                    }
                                };

                                callList.enqueue(callbackpromo);

                                Toast.makeText(TabuleiroGameActivity.this, escolhido + " promovido!", Toast.LENGTH_SHORT).show();

                                statusRodada(jogador.getId());


                            } else {

                                Toast.makeText(TabuleiroGameActivity.this, escolhido + " não pode ser promovido!", Toast.LENGTH_SHORT).show();

                            }
                            statusTablr(api, jogador.getId());
                            changed = false;
                        }
                    })
                    .build();



        }

    }

    public boolean canPromote( String candLetter){

       for(Setor setor : setores){


           if(setor.getId() != 5 && setor.getId() != 10 && setor.getPersonagens().size() == 4) {
               if (setor.getPersonagens().contains(candLetter)){
                   return false;
               }

           }
           if (setor.getId() == 10 && setor.getPersonagens() != null){
               return false;
           }

       }
       return true;
    }

    private boolean isMyTurn(String meuID){

        return (statusIdJogador.equals(meuID));

    }

}



