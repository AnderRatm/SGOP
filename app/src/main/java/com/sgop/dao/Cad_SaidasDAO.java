package com.sgop.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sgop.fragment.*;
import com.sgop.principal.Saida;
import com.sgop.principal.Sessao;

/**
 * Created by anderson on 25/11/13.
 */
public class Cad_SaidasDAO  extends MySQLiteOpenHelper{
    private final String TABLE = "cad_saidas";
    private final String TABLE2 = "categoria";
    public static String[] return_vetor;

    public Cad_SaidasDAO(Context context) {
        super(context);
    }

    public void insert(Saida saida) throws Exception {
        ContentValues values = new ContentValues();


        values.put("Codigo_categoria",saida.getCategoria());
        values.put("data_vencimento", saida.getData()) ;
        values.put("data_cadastro",saida.getData_cadastro());
        values.put("descricao", saida.getDescricao());
        values.put("Codigo_usuario",saida.getCodigo_usr());
        values.put("valor", saida.getValor());     
        
  
        getDatabase().insert(TABLE, null, values);


    }

    public void update(Saida saida) throws Exception {
        ContentValues values = new ContentValues();
        
        values.put("Codigo_categoria",saida.getCategoria());
        values.put("data_cadastro",saida.getData_cadastro());
        values.put("valor", saida.getValor());
        values.put("descricao", saida.getDescricao());
        values.put("Codigo_usuario",saida.getCodigo_usr());
        values.put("data_vencimento",  saida.getData());
           
       

        getDatabase().update(TABLE, values, "codigo = ?", new String[]{""});
    }
    public void delete(Saida saida) throws Exception {
        ContentValues values = new ContentValues();

        getDatabase().delete(TABLE, "codigo=?", new String[]{""});
    }

    public List<Saida> findByDAta(Date dt_cadastro) {
        List<Saida> retorno = new ArrayList<Saida>();
        String sql = "SELECT * FROM " + TABLE + " WHERE dt_cadastro = ?";
        String[] selectionArgs = new String[] { "" + dt_cadastro};
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaSaida(cursor));
            cursor.moveToNext();
        }
        return retorno;

    }
    public List<Saida> findByCodigo(int codigo) {
        List<Saida> retorno = new ArrayList<Saida>();
        String sql = "SELECT * FROM " + TABLE + " WHERE codigo = ?";
        String[] selectionArgs = new String[] { "" + codigo };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaSaida(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public List<Saida> findAll() throws Exception {
        ArrayList<Saida> retorno = new ArrayList<Saida>();
        String sql = "SELECT * FROM " + TABLE;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaSaida(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public Saida montaSaida(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }

        String descricao =	cursor.getString(cursor.getColumnIndex("descricao"));
        Double valor = cursor.getDouble(cursor.getColumnIndex("valor"));
        String dt_cad = cursor.getString(cursor.getColumnIndex("data_cadastro"));
        String dt_venc = cursor.getString(cursor.getColumnIndex("data_vencimento"));
        int categoria = cursor.getInt(cursor.getColumnIndex("Codigo_categoria"));
        int codigo = cursor.getInt(cursor.getColumnIndex("Codigo_usuario"));


        return new Saida(descricao,valor,dt_cad,dt_venc,categoria,codigo);

    }
    
    public List<String> findByFilter() {	
        
        ArrayList<String>TotalSaidas = new ArrayList<String>();
        String sql = "SELECT valor from " + TABLE  +
        		" where Codigo_usuario = " + Sessao.codigo_usr ;
        
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {           
        	TotalSaidas.add(cursor.getString(cursor.getColumnIndexOrThrow("valor")));            
            cursor.moveToNext();
        }
       // return_vetor=(String[])Return_cod.toArray(new String[Return_cod.size()]);          
        return TotalSaidas;
    }


    
    public List<String> findAllCategoria() throws Exception {
    	ArrayList<String>Return_cod = new ArrayList<String>();
        ArrayList<String> retorno = new ArrayList<String>();
        String sql = "SELECT _id, descricao from " + TABLE2  +
        		" where Codigo_usuario = '" + Sessao.codigo_usr + "'ORDER BY descricao";
        //("select ID, NOME from " + NOME_TABELA_RAMOATIVIDADE + " where ID_REPRES = '" + idRepres + "' order by NOME", null);
       

        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            Return_cod.add(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            cursor.moveToNext();
        }
        return_vetor=(String[])Return_cod.toArray(new String[Return_cod.size()]);  
            return retorno;
        
    }
    public List<Categoria> getCategorias() {
		List<Categoria> categoria = new ArrayList<Categoria>();
		Cursor cursor = database.query("categoria",
				new String[] {"_id","descricao"}, null, null, null, null,null);

		while (cursor.moveToNext()) {
			Categoria cat = new Categoria();
			cat.setId(cursor.getInt(0));
			cat.setDescricao(cursor.getString(1));
			categoria.add(cat);
		}
		return categoria;
	}
}
