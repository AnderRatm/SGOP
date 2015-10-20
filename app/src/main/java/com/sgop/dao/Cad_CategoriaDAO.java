package com.sgop.dao;

import java.util.ArrayList;
import java.util.List;

import com.sgop.dao.MySQLiteOpenHelper;
import com.sgop.fragment.Categoria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Cad_CategoriaDAO  extends MySQLiteOpenHelper{


	private static final String WHERE_ID_EQUALS = MySQLiteOpenHelper.ID_COLUMN
			+ " =?";
	
	 public void open()  throws SQLException {
	        database = getWritableDatabase();
	    }

	public Cad_CategoriaDAO(Context context) {
		super(context);
	}

	public long save(Categoria categoria) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteOpenHelper.NAME_COLUMN, categoria.getDescricao());

		return database.insert(MySQLiteOpenHelper.DEPARTMENT_TABLE, null, values);
	}

	public long update(Categoria categoria) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteOpenHelper.NAME_COLUMN, categoria.getDescricao());

		long result = database.update(MySQLiteOpenHelper.DEPARTMENT_TABLE, values,
				WHERE_ID_EQUALS,
				new String[] { String.valueOf(categoria.getId()) });
		Log.d("Update Result:", "=" + result);
		return result;

	}

	public int deleteDept(Categoria categoria) {
		return database.delete(MySQLiteOpenHelper.DEPARTMENT_TABLE,
				WHERE_ID_EQUALS, new String[] { categoria.getId() + "" });
	}

	public List<Categoria> getCategorias() {
		List<Categoria> categorias = new ArrayList<Categoria>();
		Cursor cursor = database.query(MySQLiteOpenHelper.DEPARTMENT_TABLE,
				new String[] { MySQLiteOpenHelper.ID_COLUMN,
						MySQLiteOpenHelper.NAME_COLUMN }, null, null, null, null,
				null);

		while (cursor.moveToNext()) {
			Categoria categoria = new Categoria();
			categoria.setId(cursor.getInt(0));
			categoria.setDescricao(cursor.getString(1));
			categorias.add(categoria);
		}
		return categorias;
	}

	/*public void loadCategorias() {
		Categoria categoria = new Categoria("Development");
		Categoria categoria1 = new Categoria("R and D");
		Categoria categoria2 = new Categoria("Human Resource");
		Categoria categoria3 = new Categoria("Financial");
		Categoria categoria4 = new Categoria("Marketing");
		Categoria categoria5 = new Categoria("Sales");

		List<Categoria> categorias = new ArrayList<Categoria>();
		categorias.add(categoria);
		categorias.add(categoria1);
		categorias.add(categoria2);
		categorias.add(categoria3);
		categorias.add(categoria4);
		categorias.add(categoria5);
		for (Categoria catg : categorias) {
			ContentValues values = new ContentValues();
			values.put(MySQLiteOpenHelper.NAME_COLUMN, catg.getDescricao());
			database.insert(MySQLiteOpenHelper.DEPARTMENT_TABLE, null, values);
		}
	}*/

}
