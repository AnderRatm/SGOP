package com.sgop.principal;



import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.projeto.SGOP.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListarActivity extends Activity implements OnGestureListener {
	// variavel statica que controla a classe que chama o método
	public static String controle;
	private String var, var1, var2, var3, var4, var5;
	public TextView tv;
	private GestureDetector detector = null;
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.bt_pesquisar) {
	

		} else if (item.getItemId() == R.id.bt_novo) {
		

		} else if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		this.detector = new GestureDetector(this);
	}

	public void onResume() {
		super.onResume();
		
		
		
		this.detector = new GestureDetector(this);
		if (controle.equals("cad_saidas") || controle.equals("cad_entradas")) {
			ListaEntradaSaida();
		} else {

			ListaCategoriaFntEntrada();
		}

	}

	public void ListaEntradaSaida() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
				Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT * FROM " + controle
				+ " WHERE Codigo_usuario =" + Sessao.codigo_usr, null);
		// pega os nomes das colunas da tabela selecionada
		var1 = cursor.getColumnName(0);
		var2 = cursor.getColumnName(1);
		var3 = cursor.getColumnName(2);
		var4 = cursor.getColumnName(3);
		var5 = cursor.getColumnName(4);

		String[] from = { var1, var2, var3, var4, var5 };
		int[] to = { R.id.txvID, R.id.txvDescricao, R.id.txv_adapter1,
				R.id.txv_adapter2, R.id.txv_adapter3 };

		@SuppressWarnings("deprecation")
		android.widget.SimpleCursorAdapter ad = new android.widget.SimpleCursorAdapter(
				getBaseContext(), R.layout.list_entrada_saida, cursor, from, to);

		ListView ltwDados = (ListView) findViewById(R.id.listView1);

		ltwDados.setAdapter(ad);

		ltwDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapter, View view,
					int position, long id) {

				SQLiteCursor c = (SQLiteCursor) adapter.getAdapter().getItem(
						position);

				Intent it = new Intent(getBaseContext(), Editar_ent_saidas.class);
				it.putExtra("id", c.getInt(0));
				startActivity(it);
				overridePendingTransition(
						R.animator.animation_direita_para_esquerda_aparece,
						R.animator.animation_direita_para_esquerda_some);
			}
		});

		db.close();
	}

	public void ListaCategoriaFntEntrada() {
		SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
				Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT * FROM " + controle
				+ " WHERE Codigo_usuario =" + Sessao.codigo_usr, null);

		var = cursor.getColumnName(2);
		String[] from = { "_id", "descricao" };
		int[] to = { R.id.txvID_cat, R.id.txvDescricao_cat };

		android.widget.SimpleCursorAdapter ad = new android.widget.SimpleCursorAdapter(
				getBaseContext(), R.layout.list_fnt_categoria, cursor, from, to);

		ListView ltwDados = (ListView) findViewById(R.id.listView1);

		ltwDados.setAdapter(ad);

		ltwDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapter, View view,
					int position, long id) {

				SQLiteCursor c = (SQLiteCursor) adapter.getAdapter().getItem(
						position);

				Intent it = new Intent(getBaseContext(), Editar.class);
				it.putExtra("id", c.getInt(0));
				startActivity(it);
				overridePendingTransition(
						R.animator.animation_direita_para_esquerda_aparece,
						R.animator.animation_direita_para_esquerda_some);
			}
		});

		db.close();
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(
				R.animator.animation_esquerda_para_direita_aparece,
				R.animator.animation_esquerda_para_direita_some);
		finish();

	}
	public boolean onTouchEvent(MotionEvent event) {
		if (this.detector.onTouchEvent(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		if (Math.abs(e1.getY() - e2.getY()) > 250) {
			return false;
		}

		// Movimento da direita para esquerda
		if (e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 200) {
			// Faz algo...

			// Movimento da esquerda para direita
		} else if (e2.getX() - e1.getX() > 100 && Math.abs(velocityX) > 200) {
			// Faz outro algo...
			super.onBackPressed();
			overridePendingTransition(
					R.animator.animation_esquerda_para_direita_aparece,
					R.animator.animation_esquerda_para_direita_some);
			finish();
		}

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}