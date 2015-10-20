package com.sgop.principal;

/**
 * Created by anderson on 05/11/13.
 */
public class Registro {
    int codigo;
    public String email;
    public String senha;
    public String telefone;
    public String nome;
    public String cod_email;


    

    public Registro() {

    }
    public int getcodigo() {
        return codigo;
    }
    public String getCod_email() {
        return cod_email;
    }

    public void setCod_email(String cod_email) {
        this.cod_email = cod_email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Registro( int codigo) {
        this.codigo = codigo;
    }
    public Registro( String email, String senha, String telefone, String nome,String cod_email) {
        //this.codigo = codigo;
        this.email = email;
        this.senha = senha;
        this.telefone =telefone;
        this.nome = nome;
        this.cod_email = cod_email;

    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getSenha() {
        return senha;
    }



}

