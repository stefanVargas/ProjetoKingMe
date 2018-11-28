package com.jogosdigitais.stefanvdemoraes.projetojogokingme.models;

public class Jogador {
    private String nome;
    private String senha;
    private Long id;
    private String erro;
    private Long pontuacao;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getErro() {
        return erro;
    }
    public void setErro(String erro) {
        this.erro = erro;
    }
    public Long getPontuacao() {
        return pontuacao;
    }
    public void setPontuacao(Long pontuacao) {
        this.pontuacao = pontuacao;
    }





}
