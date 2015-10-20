package com.sgop.principal;

/**
 * Created by anderson on 11/12/13.
 */
public class ItemListView {
    private String ID;
    private String Codigo;
    private String Data_cadastro;
    private String Data_vencimento;
    private String Valor;
    private String Emissor;

    public ItemListView() {
    }

    public ItemListView(String ID,String codigo, String data_cadastro, String data_vencimento, String valor, String emissor) {
        this.ID = ID;
        Codigo = codigo;
        Data_cadastro = data_cadastro;
        Data_vencimento = data_vencimento;
        Valor = valor;
        Emissor = emissor;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getData_cadastro() {
        return Data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        Data_cadastro = data_cadastro;
    }

    public String getData_vencimento() {
        return Data_vencimento;
    }

    public void setData_vencimento(String data_vencimento) {
        Data_vencimento = data_vencimento;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getEmissor() {
        return Emissor;
    }

    public void setEmissor(String emissor) {
        Emissor = emissor;
    }
}
