package com.sgop.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.projeto.SGOP.R;
import com.sgop.dao.Cad_EntradaDAO;
import com.sgop.principal.Controller;
import com.sgop.principal.Entrada;
import com.sgop.principal.ListarActivity;
import com.sgop.principal.Sessao;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by anderson on 25/11/13.
 */

public class CadEntradasActivity extends FragmentActivity  implements
OnGestureListener{
	private GestureDetector detector = null;

	String descricao, vlr, data;
	Double valor;
	Entrada reg;
	EditText editDesc;
	static EditText editDate;
	EditText editValor;
	Spinner spinnerFntEntrada;

	private Controller usrController;
	private Context context;
	protected int fnt_entrada;
	protected String nome;

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.bt_pesquisar) {
			
			ListarActivity.controle = "cad_entradas";

			Intent it = new Intent(getBaseContext(),
					com.sgop.principal.ListarActivity.class);
			startActivity(it);
			overridePendingTransition(
					R.animator.animation_direita_para_esquerda_aparece,
					R.animator.animation_direita_para_esquerda_some);
			
		//} else if (item.getItemId() == R.id.bt_editar) {

		} else if (item.getItemId() == R.id.bt_novo) {
			CadCategoria_FonteActivity.chamada = "Fonte";
			Intent it = new Intent();
			overridePendingTransition(
					R.animator.animation_direita_para_esquerda_aparece,
					R.animator.animation_direita_para_esquerda_some);
			it.setClass(this, CadCategoria_FonteActivity.class);
			startActivity(it);

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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		editDate = (EditText) findViewById(R.id.dt_entrada_txt);
		
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entradas);
		context = this;
		usrController = Controller.getInstance(context);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		this.detector = new GestureDetector(this);

		Button Cad_entrada = (Button) findViewById(R.id.bt_CadastrarEntrada);
		editDesc = (EditText) findViewById(R.id.descricao_txt);
		// editDate = (EditText)findViewById(R.id.dt_entrada_txt);
		editValor = (EditText) findViewById(R.id.valor_txt);
		spinnerFntEntrada = (Spinner) findViewById(R.id.SpinnerCat);

	
		

		Cad_entrada.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					CadastrarEntrada();
				} catch (SQLiteException e) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Erro gravar dados!", Toast.LENGTH_LONG);
					toast.show();
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	public void MontaFonte_Entrada_Spinner() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> fontes = (ArrayList<String>) usrController
				.findAllFnt_Entradas();

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, fontes);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFntEntrada.setAdapter(spinnerArrayAdapter);

		spinnerFntEntrada
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int posicao, long id) {
						// pega nome pela posição
	// ***************************** BUSCAR CODIGO DA CATEGORIA e GRAVAR NO CADSASTRO***********************************//
						fnt_entrada = Integer
								.parseInt(Cad_EntradaDAO.return_vetor[posicao]);
						nome = parent.getItemAtPosition(posicao).toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		if (fontes.size() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Nenhuma categoria cadastrada!");
			builder.setMessage("Cadastre uma categoria para continuar");

			builder.setPositiveButton("Cadastrar",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Do do my action here
							CadCategoria_FonteActivity.chamada = "Fonte";
							
							overridePendingTransition(
									R.animator.animation_direita_para_esquerda_aparece,
									R.animator.animation_direita_para_esquerda_some);
							Intent it = new Intent();
							it.setClass(CadEntradasActivity.this,
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

	private void CadastrarEntrada() throws Exception {
		String temp = editValor.getText().toString(); // recebe o valor digitado

		try {
			reg = new Entrada();

			reg.descricao = editDesc.getText().toString();
			reg.dtCadastro = editDate.getText().toString();
			reg.valor = Double.parseDouble(temp);
			reg.fonteEntrada = fnt_entrada;
			reg.cod_usr = Sessao.codigo_usr;

		} catch (Exception e) {

		}
		if (reg.descricao.equals("") || reg.dtCadastro.equals("") || reg.valor == 0
				|| reg.fonteEntrada == 0) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					CadEntradasActivity.this);
			dialog.setMessage("Favor preencher todos os campos  ");
			dialog.setNeutralButton("OK", null);
			dialog.setTitle("Campos vazio!");
			dialog.show();
		}

		else {
			try {
				Entrada entrada = new Entrada(reg.descricao, reg.valor,
						reg.dtCadastro, reg.fonteEntrada, reg.cod_usr);

				usrController.insertEntradas(entrada);
				reg.descricao = "";
				editDesc.setText("");
				editDate.setText("");
				editValor.setText("");
				// editFntEntrada.setText("");

				Toast toast = Toast.makeText(getApplicationContext(),
						"Dados gravados com sucesso!", Toast.LENGTH_LONG);
				toast.show();

			} catch (Exception e) {
				e.printStackTrace();
				Toast toast = Toast.makeText(getApplicationContext(),
						"Erro gravar dados!", Toast.LENGTH_LONG);
				toast.show();
			}

		}
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
			editDate.setText(String.valueOf(day) + " /"
					+ String.valueOf(month + 1) + " /" + String.valueOf(year));

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			MontaFonte_Entrada_Spinner();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(
				R.animator.animation_esquerda_para_direita_aparece,
				R.animator.animation_esquerda_para_direita_some);
		finish();

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
