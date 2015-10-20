package com.sgop.principal;

import com.projeto.SGOP.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Editar extends Activity implements
OnGestureListener{
	private GestureDetector detector = null;
	/** Called when the activity is first created. */

	private Controller usrController;
	EditText txtNome;
	String dado_duplicado;
	private String txt;
	
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar);		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		this.detector = new GestureDetector(this);
		
		Intent it = getIntent();
		int id = it.getIntExtra("id", 0);

		SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
				Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT * FROM " + ListarActivity.controle
				+ " WHERE _id = ?", new String[] { String.valueOf(id) });

		if (cursor.moveToFirst()) {
			EditText txtNome = (EditText) findViewById(R.id.txtDesc);
			EditText txtEmail = (EditText) findViewById(R.id.txtValor);

			txtNome.setText(cursor.getString(1));
			txtEmail.setText(cursor.getString(2));
		}
	}

	public void AtualizarClick(View v) {
		EditText txtNome = (EditText) findViewById(R.id.txtDesc);
		txt = txtNome.getText().toString();
		EditText txtEmail = (EditText) findViewById(R.id.txtValor);

		if (txtNome.getText().toString().length() <= 0) {
			txtNome.setError("Preencha o campo nome.");
			txtNome.requestFocus();
		} else if (txtEmail.getText().toString().length() <= 0) {
			txtEmail.setError("Preencha o campo e-mail.");
			txtEmail.requestFocus();
		} else {
			try {
				SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
						Context.MODE_PRIVATE, null);

				ContentValues ctv = new ContentValues();
				ctv.put("descricao", txtNome.getText().toString());
				// ctv.put("email", txtEmail.getText().toString());

				Intent it = getIntent();

				int id = it.getIntExtra("id", 0);

				if (VerificaExixtente() == true) {

					if (db.update(ListarActivity.controle, ctv, "_id=?",
							new String[] { String.valueOf(id) }) > 0) {
						Toast.makeText(getBaseContext(),
								"Sucesso ao atualizar o registro.",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(getBaseContext(),
								"Erro ao atualizar o registro.",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getBaseContext(),
							"Erro ao atualizar o registro, valor duplicado.",
							Toast.LENGTH_SHORT).show();
					txtNome.setError(ListarActivity.controle + " já existe");
					txtNome.requestFocus();
				}

			} catch (Exception ex) {
				Toast.makeText(getBaseContext(), ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean VerificaExixtente() throws Exception {
		// TODO Auto-generated method stub
		// ******Busca no BD se existe um item ja cadastrado com o mesmo nome
		// que o usuario está tentando inserir

		SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
				Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT * FROM " + ListarActivity.controle
				+ " WHERE descricao = ? AND Codigo_usuario = "
				+ Sessao.codigo_usr, new String[] { txt });

		if (cursor.moveToFirst()) {
			dado_duplicado = (cursor.getString(1));
		}

		if (dado_duplicado == null) {
			return true;

		}
		dado_duplicado = null;
		return false;
	}

	public void ApagarClick(View v) {
		try {
			final SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
					Context.MODE_PRIVATE, null);

			Intent it = getIntent();

			final int id = it.getIntExtra("id", 0);

			Builder msg = new Builder(Editar.this);
			msg.setMessage("Deseja apagar este registro?");
			msg.setNegativeButton("Não", null);
			msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					if (db.delete(ListarActivity.controle, "_id=?",
							new String[] { String.valueOf(id) }) > 0) {
						Toast.makeText(getBaseContext(),
								"Sucesso ao apagar o registro.",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(getBaseContext(),
								"Erro ao apagar o cliente.", Toast.LENGTH_SHORT)
								.show();
					}

				}
			});

			msg.show();

		} catch (Exception ex) {
			Toast.makeText(getBaseContext(), ex.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

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