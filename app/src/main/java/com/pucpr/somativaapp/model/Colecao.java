package com.pucpr.somativaapp.model;

public class Colecao {
    private long id;
    private String titulo;
    private String caminhoImg;
    private Integer completo;
    private int ultimoLido;
    private int rate;

    public Colecao(){}

    public Colecao(long id, String titulo, String caminhoImg, Integer completo, int ultimoLido, int rate) {
        this.id = id;
        this.titulo = titulo;
        this.caminhoImg = caminhoImg;
        this.completo = completo;
        this.ultimoLido = ultimoLido;
        this.rate = rate;
    }

    public Colecao(String titulo, String caminhoImg, Integer completo, int ultimoLido, int rate) {
        this.titulo = titulo;
        this.completo = completo;
        this.ultimoLido = ultimoLido;
        this.caminhoImg = caminhoImg;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCaminhoImg() {
        return caminhoImg;
    }

    public void setCaminhoImg(String caminhoImg) {
        this.caminhoImg = caminhoImg;
    }

    public Integer getCompleto() {
        return completo;
    }

    public void setCompleto(Integer completo) {
        this.completo = completo;
    }

    public int getUltimoLido() {
        return ultimoLido;
    }

    public void setUltimoLido(int ultimoLido) {
        this.ultimoLido = ultimoLido;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
