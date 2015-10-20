package com.sgop.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
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
import com.sgop.dao.Cad_SaidasDAO;
import com.sgop.principal.Controller;
import com.sgop.principal.ListarActivity;
import com.sgop.principal.Saida;
import com.sgop.principal.Sessao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by anderson on 25/11/13.
 */
public class CadastroSaidasActivity extends Activity implements
OnGestureListener{
	private GestureDetector detector = null;

	private static final String TAG = "";
	String descricao, dtsaida, dtcadastro, email_usr, vlr, nome;
	Double valor;
	Saida reg;
	EditText editDesc;
	static EditText editDate;
	EditText editValor;
	Spinner sp_categoria;
	private Controller usrController;
	private Context context;
	int categoria;

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.bt_pesquisar) {
			// pega o nome da classe para usar nas conultas do BD
			ListarActivity.controle = "cad_saidas";

			Intent it = new Intent(getBaseContext(),
					com.sgop.principal.ListarActivity.class);
			startActivity(it);
			overridePendingTransition(
					R.animator.animation_direita_para_esquerda_aparece,
					R.animator.animation_direita_para_esquerda_some);
		
		} else if (item.getItemId() == R.id.bt_editar) {

		} else if (item.getItemId() == R.id.bt_novo) {
			CadCategoria_FonteActivity.chamada = "Categoria";
			Intent it = new Intent();
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
		editDate = (EditText) findViewById(R.id.tv_dt_saida);
		editDate.setText(dtsaida);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saidas);
		context = this;
		usrController = Controller.getInstance(context);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.detector = new GestureDetector(this);

		Button Cad_saida = (Button) findViewById(R.id.bt_Cadastrar_Saida);
		editDesc = (EditText) findViewById(R.id.tv_desc_saida);
		// editDate = (EditText)findViewById(R.id.dt_saida_txt);
		editValor = (EditText) findViewById(R.id.valor_saida_txt);
		sp_categoria = (Spinner) findViewById(R.id.sp_categoria_saida);

		Intent intent = getIntent();
		Bundle params = intent.getExtras();

		if (params != null) {
			dtsaida = params.getString("dt_venc");
			editDate.setText(dtsaida);

			vlr = params.getString("valor");
			editValor.setText(vlr.toString());

		}

		Cad_saida.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CadastrarSaida();

			}
		});


	}

	private void CadastrarSaida() {
		CapturarDataAtual();
		String temp = editValor.getText().toString();
		try {
			reg = new Saida();

			reg.descricao = editDesc.getText().toString();
			reg.valor = Double.parseDouble(temp);
			reg.data_cadastro = dtsaida;
			reg.data = editDate.getText().toString();
			reg.categoria = categoria;
			reg.codigo_usr = Sessao.codigo_usr;

		} catch (Exception e) {
		}
		if (reg.descricao.equals("") || reg.data.equals("") || reg.valor == 0
				|| reg.categoria == 0) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					CadastroSaidasActivity.this);
			dialog.setMessage("Favor preencher todos os campos  ");
			dialog.setNeutralButton("OK", null);
			dialog.setTitle("Campos vazio!");
			dialog.show();
		} else {
			try {
				Saida saida = new Saida(reg.descricao, reg.valor,
						reg.data_cadastro, reg.data, reg.categoria,
						reg.codigo_usr);

				usrController.insertSaidas(saida);
				reg.descricao = "";
				editDesc.setText("");
				editDate.setText("");
				editValor.setText("");

				Toast toast = Toast.makeText(getApplicationContext(),
						"Dados gravados com sucesso!", Toast.LENGTH_LONG);
				toast.show();
			} catch (SQLiteConstraintException e) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						CadastroSaidasActivity.this);
				dialog.setMessage("Categoria não existe, cadastre pelo menos uma categoria ! ");
				dialog.setNeutralButton("OK", null);
				dialog.setTitle("Não existe categoria cadastrada!");
				dialog.show();

				e.printStackTrace();

			} catch (Exception e) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Erro gravar dados!", Toast.LENGTH_LONG);
				toast.show();
				e.printStackTrace();
			}

		}
	}

	private void CapturarDataAtual() {

		/***** Capturar data do sistema ****/
		Locale locale = new Locale("pt", "BR");
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy", locale);
		dtsaida = form.format(calendar.getTime());

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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

	public void MontaCategoriaSpinner() throws Exception {
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
							it.setClass(CadastroSaidasActivity.this,
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

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(
				R.animator.animation_esquerda_para_direita_aparece,
				R.animator.animation_esquerda_para_direita_some);
		finish();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			MontaCategoriaSpinner();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
