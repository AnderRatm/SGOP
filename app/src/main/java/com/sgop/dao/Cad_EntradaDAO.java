package com.sgop.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sgop.fragment.*;
import com.sgop.principal.Entrada;
import com.sgop.principal.Sessao;

/**
 * Created by anderson on 25/11/13.
 */
public class Cad_EntradaDAO extends MySQLiteOpenHelper {
	private final String TABLE = "cad_entradas";
	private final String TABLE2 = "fonte_entrada";
	public static String[] return_vetor;

	public Cad_EntradaDAO(Context context) {
		super(context);
	}

	public void insert(Entrada entrada) throws Exception {
		ContentValues values = new ContentValues();

		values.put("descricao", entrada.getDescricao());
		values.put("valor", entrada.getValor());
		values.put("data_cadastro", entrada.getDtCadastro());
		values.put("Codigo_fonte", entrada.getFonteEntrada());
		values.put("Codigo_usuario", entrada.getCodigo_usr());

		getDatabase().insert(TABLE, null, values);
	}

	public void update(Entrada entrada) throws Exception {
		ContentValues values = new ContentValues();

		values.put("descricao", entrada.getDescricao());
		values.put("valor", entrada.getValor());
		values.put("data_cadastro", entrada.getDtCadastro());
		values.put("_id", entrada.getFonteEntrada());
		values.put("Codigo_usuario", entrada.getCodigo_usr());

		getDatabase().update(TABLE, values, "codigo = ?", new String[] { "" });
	}

	public void delete(Entrada entrada) throws Exception {
		ContentValues values = new ContentValues();

		getDatabase().delete(TABLE, "codigo=?", new String[] { "" });
	}

	public List<Entrada> findByDAta(Date dt_cadastro) {
		List<Entrada> retorno = new ArrayList<Entrada>();
		String sql = "SELECT * FROM " + TABLE + " WHERE dt_cadastro = ?";
		String[] selectionArgs = new String[] { "" + dt_cadastro };
		Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			retorno.add(montaEntrada(cursor));
			cursor.moveToNext();
		}
		return retorno;

	}

	public List<Entrada> findByCodigo(int codigo) {
		List<Entrada> retorno = new ArrayList<Entrada>();
		String sql = "SELECT * FROM " + TABLE + " WHERE codigo = ?";
		String[] selectionArgs = new String[] { "" + codigo };
		Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			retorno.add(montaEntrada(cursor));
			cursor.moveToNext();
		}
		return retorno;
	}

	public List<Entrada> findAll() throws Exception {
		List<Entrada> retorno = new ArrayList<Entrada>();
		String sql = "SELECT * FROM " + TABLE;
		Cursor cursor = getDatabase().rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			retorno.add(montaEntrada(cursor));
			cursor.moveToNext();
		}
		return retorno;
	}

	public Entrada montaEntrada(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return null;
		}

		// int ID = cursor.getInt(cursor.getColumnIndex("_id"));
		String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
		Double valor = cursor.getDouble(cursor.getColumnIndex("valor"));
		String dt_cad = cursor
				.getString(cursor.getColumnIndex("data_cadastro"));
		Integer fonte = cursor.getInt(cursor.getColumnIndex("Codigo_fonte"));
		int Codigo_usr = cursor.getInt(cursor.getColumnIndex("Codigo_usuario"));

		return new Entrada(descricao, valor, dt_cad, fonte, Codigo_usr);

	}

	public List<String> findAllFontes() throws Exception {
		ArrayList<String> retorno = new ArrayList<String>();
		ArrayList<String> Return_cod = new ArrayList<String>();
		String sql = "SELECT _id, descricao from " + TABLE2
				+ " where Codigo_usuario = '" + Sessao.codigo_usr
				+ "'ORDER BY descricao";
		// ("select ID, NOME from " + NOME_TABELA_RAMOATIVIDADE +
		// " where ID_REPRES = '" + idRepres + "' order by NOME", null);

		Cursor cursor = getDatabase().rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			retorno.add(cursor.getString(cursor
					.getColumnIndexOrThrow("descricao")));
			Return_cod
					.add(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
			cursor.moveToNext();
		}
		return_vetor = (String[]) Return_cod.toArray(new String[Return_cod
				.size()]);
		return retorno;
	}

	public List<String> findByFilter() {

		ArrayList<String> TotalBoletos = new ArrayList<String>();
		String sql = "SELECT valor from " + TABLE + " where Codigo_usuario = "
				+ Sessao.codigo_usr;

		Cursor cursor = getDatabase().rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TotalBoletos.add(cursor.getString(cursor
					.getColumnIndexOrThrow("valor")));
			cursor.moveToNext();
		}
		// return_vetor=(String[])Return_cod.toArray(new
		// String[Return_cod.size()]);
		return TotalBoletos;
	}

}
