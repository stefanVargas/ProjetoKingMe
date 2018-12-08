package com.jogosdigitais.stefanvdemoraes.projetojogokingme.API;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogador;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogo;
import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Setor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface KingMeAPI {


    //Cria uma nova partida
    @POST("kingme/rest/v1/jogo")
    Call<Jogo> criarJogo(@Body Jogo jogo);

    //Lista todas as partidas Abertas ou Em Andamento.Não exibe partidas encerradas
    @GET("kingme/rest/v1/jogo")
    Call<List<Jogo>> getJogos();

    //Cria um jogador e o coloca em uma partida
    @POST("kingme/rest/v1/jogador/{idJogo}/{senhaJogo}")
    Call<Jogador> criarJogador(@Path("idJogo") Long idJogo, @Path("senhaJogo") String senhaJogo, @Body Jogador jogador);


    //Lista todos jogadores que estão em uma partida
    @GET ("kingme/rest/v1/jogador/{idJogo}")
    Call<List<Jogador>> obterJogadores (@Path("idJogo") Long idJogo);


    //Envia um comando iniciando uma determinada partida. Muda o Status da mesma de A para J
    @PUT("kingme/rest/v1/jogo")
    Call<Jogador> iniciarJogo (@Body Jogo jogo);


    //Retorno o status do jogo e o status da rodada
    @GET("kingme/rest/v1/jogo/{idJogo}")
    Call<Jogo> obtemStatusJogo(@Path("idJogo") Long idJogo);


    //Retorna a carta que contém a lista de personagens favoritos sorteada para este jogador.
    @POST("kingme/rest/v1/personagem")
    Call<List<String>> obterCartas(@Body Jogo jogo);

    //Exibe uma lista com id e nome de todos os setores.
    @GET ("kingme/rest/v1/setor")
    Call<List<Setor>> obterJogadores();

    //Coloca um personagem no tabuleiro, na fase de Setup de uma rodada.
    @POST("kingme/rest/v1/personagem/{idSetor}/{persona}")
    Call<List<Setor>> colocaPersonagem(@Path("idSetor") Long idSetor, @Path("persona") String persona, @Body Jogador jogador);

    //Verifica qual é o jogador da "vez"
    @GET ("kingme/rest/v1/jogador/vez/{idJogador}")
    Call<Jogador> verificaJogador(@Path("idJogador") Long idJogador);

    //Promove um personagem para o setor acima durante a fase de promoção de uma rodada
    @PUT("kingme/rest/v1/personagem/{letraPersonagem}")
    Call<List<Setor>> promovePersonagem (@Path("letraPersonagem") String letra, @Body Jogo jogo);

    //Vota S ou N em um personagem que está na presidencia. Caso todos jogadores votem S, a rodada é finalizada.
    @POST("kingme/rest/v1/voto/{letraVoto}")
    Call<List<Setor>> votaKing(@Path("letraVoto") String letraVoto, @Body Jogo jogo);

    //Obtem lista de personagens
    @GET ("kingme/rest/v1/personagem")
    Call<List<String>> obtemPersonagem();

    //Retorno o status do jogo e o status da rodada
    @GET ("kingme/rest/v1/jogo/{idJogo}")
    Call<Jogo> statusJogo(@Path("idJogo") Long idJogo);

    //Exibe como foi o voto de cada jogador na última votação já finalizada desta partida.
    @GET ("kingme/rest/v1/voto/{idJogador}/{senhaJog}")
    Call<Jogo> exibeVoto(@Path("idJogador") Long idJogador, @Path("senhaJog") String senhaJog);

    //Exibe o histórico de jogadas, desde o último movimento do jogador que está fazendo a solicitação.
    @GET ("kingme/rest/v1/jogador/historico/{idJogador}/{senhaJog}")
    Call<List<String>> exibeHistorico(@Path("idJogador") Long idJogador, @Path("senhaJog") String senhaJog);

    //Retorna o status do tabuleiro
    @GET ("kingme/rest/v1/jogo/{idJogador}")
    Call<List<Setor>> statusTabuleiro(@Path("idJogador") Long idJogador);



}
