package com.sgop.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;


import com.sgop.fragment.*;
import com.sgop.principal.FonteEntrada;
import com.sgop.principal.Registro;
import com.sgop.principal.Sessao;

/**
 * Created by anderson on 11/12/13.
 */
public class Cad_cadastroDAO extends MySQLiteOpenHelper {
	private final String TABLE = "categoria";
	private final String TABLE2 = "fonte_entrada";


	public Cad_cadastroDAO(Context context) {
		super(context);
	}

	public void open() throws SQLException {
		database = getWritableDatabase();
	}

	public void insert(Categoria categoria) throws Exception {
		ContentValues values = new ContentValues();
		values.put("descricao", categoria.getDescricao());
		values.put("Codigo_usuario", categoria.getCodigo_usr());

		getDatabase().insert(TABLE, null, values);
	}

	public void update(Categoria categoria) throws Exception {
		ContentValues values = new ContentValues();

		values.put("descricao", categoria.getDescricao());
		values.put("Codigo_usuario", categoria.getCodigo_usr());
		getDatabase().update(TABLE, values, "_id = ?",
				new String[] { "" });
	}

	/*
	 * public void delete(Boleto boleto) throws Exception { ContentValues values
	 * = new ContentValues();
	 * 
	 * getDatabase().delete(TABLE, "codigo=?", new String[]{""}); }
	 */public Categoria findByCat(String descricao) {
		// TODO Auto-generated method stub
		String desc = null;
		String sql = "SELECT * FROM " + TABLE
				+ " WHERE descricao = ? AND Codigo_usuario = '"
				+ Sessao.codigo_usr + "'";
		String[] selectionArgs = new String[] { descricao };
		Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			desc = cursor.getString(cursor.getColumnIndex("descricao"));
			cursor.moveToNext();
		}
		return new Categoria(desc);
		
	 }

	public void insert_Fnt(FonteEntrada fnt) throws Exception {
		ContentValues values = new ContentValues();
		values.put("descricao", fnt.getDescricao());
		values.put("Codigo_usuario", fnt.getCodigo_usr());

		getDatabase().insert(TABLE2, null, values);
	}

	public void update_Fnt(FonteEntrada fnt) throws Exception {
		ContentValues values = new ContentValues();

		values.put("descricao", fnt.getDescricao());
		values.put("Codigo_usuario", fnt.getCodigo_usr());
		getDatabase().update(TABLE2, values, "_id = ?",
				new String[] { "" });
	}

	public FonteEntrada findAllFnt(String descricao) throws Exception {
		String desc = null;
		String sql = "SELECT * FROM " + TABLE2
				+ " WHERE descricao = ? AND Codigo_usuario = '"
				+ Sessao.codigo_usr + "'";
		String[] selectionArgs = new String[] { descricao };
		Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			desc = cursor.getString(cursor.getColumnIndex("descricao"));
			cursor.moveToNext();
		}
		return new FonteEntrada(desc);
	}

}
