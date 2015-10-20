package com.sgop.activity;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.projeto.SGOP.R;
import com.sgop.dao.Cad_BoletoDAO;
import com.sgop.fragment.Boleto;
import com.sgop.fragment.Categoria;

import java.util.ArrayList;

/**
 * Created by anderson on 25/11/13.
 */
@SuppressLint("NewApi")
public class ListaBoletoActivity extends Activity implements
		OnItemClickListener {
	
	private SimpleCursorAdapter dataSource;
	private final String TABLE = "cad_boleto";
	ArrayList<Boleto> lista;
	private static final String columns[] = { "_id", "codigo_boleto",
			"emissor", "data_cadastro", "data_vencimento", "valor",
			"Cod_status", "Codigo_categoria", "Cod_status" };

	private SQLiteDatabase database;	
	ListView listView;
	Cad_BoletoDAO dao;
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.bt_filter) {

		} else if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_filtro, menu);
		return true;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		 setContentView(R.layout.main);
		 getActionBar().setDisplayHomeAsUpEnabled(true);
		 listView= (ListView)findViewById(R.id.listView1);

		 dao = new Cad_BoletoDAO(this);
		 dao.open();
		database = dao.getWritableDatabase();

		try {
			createListView();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	
	 private void createListView() { 		
		
			
			SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			queryBuilder.setTables("categoria" + " INNER JOIN " + "cad_boleto"
					+ " ON " + "_id" + " = " + ("cad_boleto.Codigo_categoria"));
			
			//queryBuilder.setTables("_id, descricao FROM categoria INNER JOIN " + "cad_boleto"
				//	+ " ON " + "_id" + " = " + ("cad_boleto.Codigo_categoria"));
			
			
			// Get cursor
			Cursor cursor = queryBuilder.query(database, new String[] {

			/*
			 * public static final String EMPLOYEE_ID_WITH_PREFIX = "emp.id"; public
			 * static final String EMPLOYEE_NAME_WITH_PREFIX = "emp.name"; public
			 * static final String DEPT_NAME_WITH_PREFIX = "dept.name"; private
			 * static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN +
			 * " =?";
			 */

			"cad_boleto._id", "cad_boleto" + "." + "_id", "codigo_boleto",
					"emissor", "data_cadastro", "data_vencimento", "valor",
					"Cod_status", "categoria.descricao" }, null, null, null, null,
					null);
			/*
			 * DataBaseHelper.EMPLOYEE_DOB, DataBaseHelper.EMPLOYEE_SALARY,
			 * DataBaseHelper.EMPLOYEE_DEPARTMENT_ID,
			 * DataBaseHelper.DEPARTMENT_TABLE + "." + DataBaseHelper.NAME_COLUMN },
			 * null, null, null, null, null);
			 */
			while (cursor.moveToNext()) {
				Boleto boleto = new Boleto();

				boleto.setId(cursor.getInt(0));
				boleto.setCod_boleto(cursor.getString(1));
				boleto.setInstituicao(cursor.getString(2));
				boleto.setDt_cad(cursor.getString(3));
				boleto.setDt_venc(cursor.getString(4));
				boleto.setValor_calc(cursor.getDouble(5));
				boleto.setCodigo_status(cursor.getInt(6));

				Categoria categoria = new Categoria();
				categoria.setId(cursor.getInt(7));
				categoria.setDescricao(cursor.getString(8));

				boleto.setCategoria(categoria);
				
			}
			
		
		
	//executa consulta geral de todos os registros cadastrados no banco de dados 		
	 // Cursor boletos = database.query(TABLE, columns, null, null, null, null, null,null);	  
	  
	  if (cursor.getCount() > 0){ 
		  //cria cursor que será exibido na tela, nele serão exibidos todos os Boletos cadastrados 	
		  
	 dataSource = new SimpleCursorAdapter(this, R.layout.list_boleto,
			 cursor, columns, 
			 new int[] { R.id.codigo, R.id.emissor, R.id.cadastro,
	  R.id.vencimento,R.id.valor,R.id.categoria,R.id.Status });
	  //Ordem:"_id","codigo_boleto"
	  //"emissor","data_cadastro","data_vencimento","valor"  
	  
	 
	  //relaciona o dataSource ao próprio listview
	  listView.setAdapter(dataSource); 
	
	  }else{ Toast.makeText(this, "Nenhum registro encontrado",
	  Toast.LENGTH_SHORT).show(); 
	  }
	}

	 
	

	@Override
	public void onPause() {
		dao.close();
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Boleto boleto = (Boleto) listView.getItemAtPosition(i);

		if (boleto != null) {
			Bundle arguments = new Bundle();
			arguments.putParcelable("selectedBoleto", boleto);
		
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
