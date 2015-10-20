package com.sgop.principal;

/**
 * Created by anderson on 25/11/13.
 */
public class Entrada {
	
	public int id;   
    public String descricao;   
    public double valor;
    public String dtCadastro;
    public Integer fonteEntrada;
    public int cod_usr;


    public Entrada(String descricao, Double valor, String dt_cad, int fonte,int cod_usr) {      
    	this.descricao = descricao;
        this.valor = valor;  
        this.dtCadastro=dt_cad;
        this.fonteEntrada = fonte;
        this.cod_usr = cod_usr;
    }

    public Entrada() {
		// TODO Auto-generated constructor stub
	}

	public String getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(String dtCadastro) {
        this.dtCadastro = dtCadastro;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Integer getFonteEntrada() {
        return fonteEntrada;
    }

    public void setFonteEntrada(Integer fonteEntrada) {
        this.fonteEntrada = fonteEntrada;
    }


	public int getCodigo_usr() {
		  return cod_usr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}