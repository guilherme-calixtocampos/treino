package com.example.treino;

public class Exercicio {
    private String nome;
    private int duracaoSegundos;

    public Exercicio(String nome, int duracaoSegundos) {
        this.nome = nome;
        this.duracaoSegundos = duracaoSegundos;
    }

    public String getNome() {
        return nome;
    }

    public int getDuracaoSegundos() {
        return duracaoSegundos;
    }
}

