package com.jogosdigitais.stefanvdemoraes.projetojogokingme.API;

import com.jogosdigitais.stefanvdemoraes.projetojogokingme.models.Jogo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KingMeAPI {


    @POST("/rest/v1/jogo")
    Call<Jogo> newGame(@Body Jogo jogo);

    //TODO
    @GET("/rest/v1/jogo")
    Call<Jogo> getJogo (
            @Path("cepNum") String cepNum);


}
