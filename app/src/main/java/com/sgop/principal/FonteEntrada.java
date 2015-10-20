package com.sgop.principal;

/**
 * Created by anderson on 11/12/13.
 */
public class FonteEntrada {

    public String descricao;
    public int cod_usr;

    public FonteEntrada( String descricao, int cod_usr) {
        this.descricao = descricao;
        this.cod_usr = cod_usr;
    }

    public FonteEntrada(String desc) {
    	 this.descricao = desc;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCodigo_usr() {
        return cod_usr;
    }

    public void setCodigo_usr(int cod_usr) {
        this.cod_usr = cod_usr;
    }
}
