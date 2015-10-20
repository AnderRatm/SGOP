package com.sgop.principal;

/**
 * Created by anderson on 25/11/13.
 */
public class Saida {
    public String descricao;
    public double valor;
    public String data_cadastro;
    public String data;
    public int categoria;
    public int codigo_usr;


    public Saida() {

    }
    public Saida(String descricao, double valor,String data_cadastro, String data, int categoria,int codigo_usr ) {
        this.descricao = descricao;
        this.valor = valor;
        this.data_cadastro = data_cadastro;
        this.data = data;
        this.categoria = categoria;
        this.codigo_usr = codigo_usr;

    }



    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public int getCodigo_usr() {
        return codigo_usr;
    }
}
