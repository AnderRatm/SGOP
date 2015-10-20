package com.sgop.activity;

import java.util.ArrayList;
import java.util.Calendar;

import com.projeto.SGOP.R;
import com.sgop.activity.CadastroSaidasActivity.DatePickerFragment;
import com.sgop.dao.Cad_BoletoDAO;
import com.sgop.dao.Cad_SaidasDAO;
import com.sgop.fragment.*;
import com.sgop.principal.Controller;
import com.sgop.principal.ListarActivity;
import com.sgop.principal.Saida;
import com.sgop.principal.Sessao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class EditarBoleto extends Activity  implements
OnGestureListener{
	private GestureDetector detector = null;

	String cod, instituicao, dtsaida, dtCad, nome, valor_duplicado;
	Double valor;
	Saida reg;
	EditText editDesc;
	static EditText editDate;
	EditText editValor;
	TextView Codigo;
	Spinner sp_categoria, sp_status;
	private Controller usrController;
	private Context context;
	int categoria;
	protected int status;
	protected String desc;

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);

	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		editDate = (EditText) findViewById(R.id.tv_dt_boleto);
		editDate.setText(dtsaida);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_boletos);
		context = this;
		usrController = Controller.getInstance(context);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.detector = new GestureDetector(this);
		
		Button Cad_Boleto = (Button) findViewById(R.id.bt_Cadastrar_Boleto);
		editDate = (EditText) findViewById(R.id.tv_dt_boleto);
		editValor = (EditText) findViewById(R.id.tv_valor_boleto);
		sp_categoria = (Spinner) findViewById(R.id.sp_categoria_boleto);
		sp_status = (Spinner) findViewById(R.id.sp_status_boleto);
		Codigo = (TextView) findViewById(R.id.txv_Cod_boleto);

		Intent intent = getIntent();
		Bundle params = intent.getExtras();

		if (params != null) {
			cod = params.getString("cod");
			Codigo.setText("Cod Barras : " + cod);

			instituicao = params.getString("inst");

			dtsaida = params.getString("dt_venc");
			editDate.setText(dtsaida);

			valor = params.getDouble("valor");
			editValor.setText(valor.toString());

			dtCad = params.getString("DtCad");
		}

		Cad_Boleto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				dtsaida = editDate.getText().toString();
				valor = Double.parseDouble(editValor.getText().toString());

				CadastrarBoleto();

			}

			private void CadastrarBoleto() {
				// TODO Auto-generated method stub
				int codigo_usr = Sessao.codigo_usr;
				
				Categoria cat = new Categoria();
				cat.setId(categoria);
				
				
				final Boleto boleto = new Boleto(cod, instituicao, dtsaida,
						valor, dtCad, codigo_usr,categoria,status);
				
				if (VerificaExistente() == true) {				
				
				try {
					usrController.insertBoleto(boleto);
					Toast toast = Toast.makeText(getApplicationContext(),
							"Dados gravados com sucesso!", Toast.LENGTH_LONG);
					toast.show();
					finish();
				} catch (Exception e) {
					e.printStackTrace();
					Toast toast = Toast.makeText(getApplicationContext(),
							"Erro ao gravar dados!", Toast.LENGTH_LONG);
					toast.show();

				}
				}else{
					Toast toast = Toast.makeText(
							getApplicationContext(),
							"Boleto ja existe!",
							Toast.LENGTH_LONG);
					toast.show();								
				}
			}
		});

		try {
			MontaCategoriaSpinner();
		} catch (Exception e) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					EditarBoleto.this);
			dialog.setMessage("Não existe categoria  ");
			dialog.setNeutralButton("OK", null);
			dialog.setTitle("Campos vazio!");
			dialog.show();
		}
		try {
			MontaStatusSpinner();
		} catch (Exception e) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					EditarBoleto.this);
			dialog.setMessage("Não existe categoria  ");
			dialog.setNeutralButton("OK", null);
			dialog.setTitle("Campos vazio!");
			dialog.show();
		}
	}

	private void MontaCategoriaSpinner() throws Exception {

		ArrayList<String> categorias = (ArrayList<String>) usrController
				.findAllCategorias();

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, categorias);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_categoria.setAdapter(spinnerArrayAdapter);

		sp_categoria
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int posicao, long id) {
						// TODO Auto-generated method stub
						// pega nome pela posição
						categoria = Integer
								.parseInt(Cad_SaidasDAO.return_vetor[posicao]);
						nome = parent.getItemAtPosition(posicao).toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		if (categorias.size() == 0) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Nenhuma categoria cadastrada!");
			builder.setMessage("Cadastre uma categoria para continuar");

			builder.setPositiveButton("Cadastrar",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Do do my action here
							CadCategoria_FonteActivity.chamada = "Categoria";
							
							overridePendingTransition(
									R.animator.animation_direita_para_esquerda_aparece,
									R.animator.animation_direita_para_esquerda_some);
							Intent it = new Intent();
							it.setClass(EditarBoleto.this,
									CadCategoria_FonteActivity.class);
							startActivity(it);
							dialog.dismiss();
						}
						
					});
			builder.setNegativeButton("Cancelar",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// finish();
							dialog.dismiss();
						}
					});
			 AlertDialog alert = builder.create();
             alert.show();  
		}

	}
	
	private void MontaStatusSpinner() throws Exception {

		ArrayList<String> Status = (ArrayList<String>) usrController
				.findAllStatus();

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, Status);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_status.setAdapter(spinnerArrayAdapter);

		sp_status
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {					

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int posicao, long id) {
						// TODO Auto-generated method stub
						// pega nome pela posição
						status = Integer
								.parseInt(Cad_BoletoDAO.return_vetor[posicao]);
						desc = parent.getItemAtPosition(posicao).toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		

	}

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
			editDate.setText(String.valueOf(day) + " /"
					+ String.valueOf(month + 1) + " /" + String.valueOf(year));

		}

	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			MontaCategoriaSpinner();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			MontaStatusSpinner();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean VerificaExistente() {
		// TODO Auto-generated method stub
//******Busca no BD se existe um item ja cadastrado com o mesmo nome que o usuario está tentando inserir

		SQLiteDatabase db = openOrCreateDatabase(
				"CodBarras.db", Context.MODE_PRIVATE, null);

		Cursor cursor = db
				.rawQuery(
						"SELECT * FROM "
								+ "cad_boleto"
								+ " WHERE codigo_boleto = ? AND Codigo_usuario = "
								+ Sessao.codigo_usr,
						new String[] { cod });

		if (cursor.moveToFirst()) {
			valor_duplicado = (cursor.getString(1));
		}

		if (valor_duplicado == null) {
			return true;

		}
		valor_duplicado = null;
		return false;
	}public boolean onTouchEvent(MotionEvent event) {
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
