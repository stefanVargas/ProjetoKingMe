package com.jogosdigitais.stefanvdemoraes.projetojogokingme.models;

import java.util.ArrayList;
import java.util.List;

public class Setor {
    private Long id;
    private String nome;

    private List<String> personagens = new ArrayList<String> ();


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public List<String> getPersonagens() {
        return personagens;
    }
    public void setPersonagens(List<String> personagens) {
        this.personagens = personagens;
    }




}

