package com.vidasnoaltarmda.celulas.Dados;

/**
 * Created by barque on 20/03/2016.
 */
public class GrupoEvangelistico {

    private int id_ge;
    private int id_celula;
    private String nome;
    private String dias;

    public int getId_ge() {
        return id_ge;
    }

    public void setId_ge(int id_ge) {
        this.id_ge = id_ge;
    }

    public int getId_celula() {
        return id_celula;
    }

    public void setId_celula(int id_celula) {
        this.id_celula = id_celula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }
}
