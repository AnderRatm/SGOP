package com.sgop.principal;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projeto.SGOP.R;
import com.sgop.dao.Cad_EntradaDAO;
import com.sgop.dao.Cad_SaidasDAO;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Editar_ent_saidas extends Activity implements
		OnGestureListener {
	/** Called when the activity is first created. */

	private Controller usrController;
	EditText txtNome;
	String dado_duplicado;
	private String txt;
	private Spinner spinner;
	private Context context;
	private int retorno;
	private String nome;
	private String var;
	private String cat_fnt;
	private String txtVlr;
	private String txtDt;
	static EditText txtdate;
	private GestureDetector detector = null;

	public boolean onCreateOptionsMenu(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.

		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return false;

	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		txtdate = (EditText) findViewById(R.id.txtDate);

	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_entrada_saida);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		this.detector = new GestureDetector(this);

		context = this;
		usrController = Controller.getInstance(context);

		Intent it = getIntent();

		int id = it.getIntExtra("id", 0);

		SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
				Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT * FROM " + ListarActivity.controle
				+ " WHERE _id = ?", new String[] { String.valueOf(id) });

		if (cursor.moveToFirst()) {
			TextView cod = (TextView) findViewById(R.id.TxvCod);
			EditText txtdesc = (EditText) findViewById(R.id.txtDesc);
			EditText txtvalor = (EditText) findViewById(R.id.txtValor);
			txtdate = (EditText) findViewById(R.id.txtDate);

			cod.setText(cursor.getString(0));
			txtdesc.setText(cursor.getString(1));
			txtvalor.setText(cursor.getString(2));
			txtdate.setText(cursor.getString(3));
		}
		spinner = (Spinner) findViewById(R.id.SpinnerCat);
		try {
			Monta_Spinner();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AtualizarClick(View v) {
		EditText txtDesc = (EditText) findViewById(R.id.txtDesc);
		txt = txtDesc.getText().toString();
		EditText txtValor = (EditText) findViewById(R.id.txtValor);
		txtVlr = txtValor.getText().toString();
		EditText txtDate = (EditText) findViewById(R.id.txtDate);
		txtDt = txtDate.getText().toString();

		if (txtDesc.getText().toString().length() <= 0) {
			txtDesc.setError("Preencha o campo descricao.");
			txtDesc.requestFocus();
		} else if (txtValor.getText().toString().length() <= 0) {
			txtValor.setError("Preencha o campo valor.");
			txtValor.requestFocus();
		} else {
			try {
				SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
						Context.MODE_PRIVATE, null);

				ContentValues ctv = new ContentValues();
				ctv.put("descricao", txtDesc.getText().toString());
				ctv.put("valor", txtValor.getText().toString());
				ctv.put(var, txtDate.getText().toString());
				ctv.put(cat_fnt, retorno);

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
				+ " WHERE descricao = ? AND valor = ? AND " + var
				+ " = ? AND Codigo_usuario = " + Sessao.codigo_usr,
				new String[] { txt, txtVlr, txtDt });

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

			Builder msg = new Builder(Editar_ent_saidas.this);
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

	public void Monta_Spinner() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> fontes;
		if (ListarActivity.controle == "cad_entradas") {
			fontes = (ArrayList<String>) usrController.findAllFnt_Entradas();
			var = "data_cadastro";
			cat_fnt = "Codigo_fonte";
		} else {
			fontes = (ArrayList<String>) usrController.findAllCategorias();
			var = "data_vencimento";
			cat_fnt = "Codigo_categoria";
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, fontes);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int posicao, long id) {
				// pega nome pela posição
				// ***************************** BUSCAR CODIGO DA CATEGORIA e
				// GRAVAR NO CADASTRO***********************************//
				if (ListarActivity.controle == "cad_entradas") {
					retorno = Integer
							.parseInt(Cad_EntradaDAO.return_vetor[posicao]);
					nome = parent.getItemAtPosition(posicao).toString();
				} else {
					retorno = Integer
							.parseInt(Cad_SaidasDAO.return_vetor[posicao]);
					nome = parent.getItemAtPosition(posicao).toString();
				}
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

	}

	@SuppressLint("NewApi")
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@SuppressLint("NewApi")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int ano = c.get(Calendar.YEAR);
			int mes = c.get(Calendar.MONTH);
			int dia = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, ano, mes, dia);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			txtdate.setText(String.valueOf(day) + " /"
					+ String.valueOf(month + 1) + " /" + String.valueOf(year));

		}

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