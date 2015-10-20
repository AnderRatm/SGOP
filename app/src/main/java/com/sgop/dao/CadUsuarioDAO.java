package com.sgop.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.sgop.principal.Registro;

/**
 * Created by anderson on 05/11/13.
 */
public class CadUsuarioDAO extends MySQLiteOpenHelper{

    private final String TABLE = "cad_usuario";

    public CadUsuarioDAO(Context context) {
        super(context);
    }

    public void insert(Registro usuario) throws Exception {
        ContentValues values = new ContentValues();


        values.put("Email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("Telefone", usuario.getTelefone());
        values.put("Nome", usuario.getNome());
        values.put("cod_email", usuario.getCod_email());

        getDatabase().insert(TABLE, null, values);
    }

    public void update(Registro usuario) throws Exception {
        ContentValues values = new ContentValues();


        values.put("Email", usuario.getEmail());
        values.put("Senha", usuario.getSenha());
        values.put("Telefone", usuario.getTelefone());
        values.put("Nome", usuario.getNome());


        getDatabase().update(TABLE, values, "codigo = ?", new String[]{"" + usuario.getEmail()});
    }
    public void delete(Registro usuario) throws Exception {
        ContentValues values = new ContentValues();

        getDatabase().delete(TABLE,"codigo=1", new String[]{""});
    }

    public Registro findById(String email) {

        String sql = "SELECT * FROM " + TABLE + " WHERE email = ?";
        String[] selectionArgs = new String[] { "" + email };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        return montaUsuario(cursor);
    }

    /******* Buscar o código do usuario logado*****************/

    public Registro findNumberUser(String email) {

        String sql = "SELECT * FROM " + TABLE + " WHERE email = ?";
        String[] selectionArgs = new String[] { "" + email };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        return montaUsuario(cursor);
    }

    public List<Registro> findAll() throws Exception {
        List<Registro> retorno = new ArrayList<Registro>();
        String sql = "SELECT * FROM " + TABLE;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaUsuario(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public Registro montaUsuario(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        Registro usuario = new Registro();
        int codigo =	cursor.getInt(cursor.getColumnIndex("codigo"));
        String email = cursor.getString(cursor.getColumnIndex("email"));
         String senha = cursor.getString(cursor.getColumnIndex("senha"));
        String telefone = cursor.getString(cursor.getColumnIndex("telefone"));
        String nome = cursor.getString(cursor.getColumnIndex("nome"));
        String cod_email = cursor.getString(cursor.getColumnIndex("cod_email"));

        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTelefone(telefone);
        usuario.setNome(nome);
        usuario.setCod_email(cod_email);

        Log.e("Nome", usuario.getNome());
        return new Registro(email,senha,telefone,nome,cod_email);

    }
    public Registro montaUsuario2(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        int codigo =	cursor.getInt(cursor.getColumnIndex("codigo"));      

        return new Registro(codigo);
    }
    /**
     * @param email
     * @param senha
     * @return
     */
    public Registro findByLogin(String email, String senha) {
        String sql = "SELECT * FROM " + TABLE + " WHERE email = ? AND senha = ?";
        String[] selectionArgs = new String[] { email, senha };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        return montaUsuario(cursor);
    }
    public Registro findByUsuario(String email) {
        String sql = "SELECT * FROM " + TABLE + " WHERE email = ?";
        String[] selectionArgs = new String[] { email};
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        return montaUsuario(cursor);
    }
    public Registro RetornaCodigoUsuario (String email){
    	String sql = "SELECT codigo FROM " + TABLE + " WHERE email = ?";
    	 String[] selectionArgs = new String[] { email};
         Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
         cursor.moveToFirst();
       
         return montaUsuario2(cursor);
    	
    	
    }



}
