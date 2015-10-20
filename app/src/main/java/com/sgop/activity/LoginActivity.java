package com.sgop.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projeto.SGOP.R;
import com.sgop.WebServer.EnviarDadosActivity;
import com.sgop.dao.MySQLiteOpenHelper;
import com.sgop.principal.Controller;
import com.sgop.principal.Sessao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

public class LoginActivity extends Activity implements
OnGestureListener  {

	private EditText editemail, editSenha;
	private Context context;
	private Controller usrController;
	private AlertDialog.Builder alert;
	Integer User_log;
	private CheckBox lembrarSenha;
	private GestureDetector detector = null;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuracoes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (item.getItemId() == android.R.id.home) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Saindo ......", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		else if (id == R.id.action_settings) {

			Intent intent = new Intent(LoginActivity.this, EnviarDadosActivity.class);
			startActivity(intent);

			Toast.makeText(getBaseContext(), "Configurações", Toast.LENGTH_LONG)
					.show();
			return true;

		} else if (item.getItemId() == R.id.action_exportar_dados) {

			// getallDb();
			new ExportDatabaseCSVTask().execute();
			/*
			 * Toast.makeText(getBaseContext(), "Exportar dados",
			 * Toast.LENGTH_LONG).show();
			 */

		} else if (item.getItemId() == R.id.action_sobre) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("Sobre");
			builder.setMessage("Developed by Anderson Pereira");

			final TextView input = new TextView(this);		
			
			
			input.setText("Contato: ander.ratm@gmail.com");			
			builder.setView(input);
		

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {						

							dialog.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		} 
		return super.onOptionsItemSelected(item);

	}

	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.detector = new GestureDetector(this);

		context = this;
		usrController = Controller.getInstance(context);
		Button Entrar = (Button) findViewById(R.id.bt_Cadastrar);
		TextView rec_senha = (TextView) findViewById(R.id.tv_rec_senha);
		TextView cadastro = (TextView) findViewById(R.id.tv_cadastro);
		editemail = (EditText) findViewById(R.id.email_txt);
		editSenha = (EditText) findViewById(R.id.senha_txt);
		lembrarSenha = (CheckBox) findViewById(R.id.checkSalvar);

		try {
			testaInicializacao();
		} catch (Exception e) {
			exibeDialogo("Erro inicializando banco de dados");
			e.printStackTrace();
		}

		try{
			carregaUsuario();
		}catch (Exception e){
			e.printStackTrace();

		}

		Entrar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				RealizarLogin();
			}
		});

		rec_senha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				AbrirTelaRecSenha();
			}
		});

		cadastro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(android.view.View arg0) {
				// TODO Auto-generated method stub
				AbrirTelaCadastro();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			carregaUsuario();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void carregaUsuario() throws Exception{
		SharedPreferences pref = getSharedPreferences("prefs", MODE_PRIVATE);
		editemail.setText(pref.getString("email",null));
		editSenha.setText(pref.getString("senha", null));


		Log.e("Email", pref.getString("email",null));
		Log.e("Pass", pref.getString("senha", null));
	}


	private void RealizarLogin() {
		String email = editemail.getText().toString();
		String senha = editSenha.getText().toString();

		if (editemail.getText().toString().length() <= 0) {
			editemail.setError("O e-mail não pode estar em branco");
			editemail.requestFocus();
		} else if (editSenha.getText().toString().length() <= 0) {
			editSenha.setError("A senha não pode estar em branco");
			editSenha.requestFocus();
		} else {
			try {
				boolean isValid = usrController.validaLogin(email, senha);
				
				if (Controller.usuarioValido != null) {
					Controller.usuarioValido= null;
					if (isValid) {

						// exibeDialogo("Usuario e senha validados com sucesso!");
						Toast toast = Toast.makeText(getApplicationContext(),
								"Olá " + Sessao.nome + " bem-vindo novamente!",
								Toast.LENGTH_LONG);
						toast.show();
						Intent it = new Intent();
						it.setClass(this, PrincipalActivity.class);
						startActivity(it);

						if (lembrarSenha.isChecked()) {
							//Salva usuário e senha por SharedPreferences
							SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
							editor.putString("email", email);
							editor.putString("senha", senha);

							String user = " " + email;
							String pass = " " +senha;

							Log.e("Email",user);
							Log.e("Pass",pass);
							editor.commit();
						}



						editemail.setText("");
						editSenha.setText("");
					} else {
						editSenha.setError("Senha incorreta!");
						editSenha.requestFocus();
						editSenha.setText("");
					}
				} else {
					editemail.setError("Usuário não cadastrado!");
					editemail.requestFocus();
					editSenha.setText("");
				}
			} catch (Exception e) {
				exibeDialogo("Erro validando usuario e senha");
				e.printStackTrace();
			}

		}

	}

	private void AbrirTelaRecSenha() {
		Intent it = new Intent();
		it.setClass(this, RecSenhaActivity.class);
		startActivity(it);

	}

	private void AbrirTelaCadastro() {
		Intent it = new Intent();
		it.setClass(this, CadastroActivity.class);
		startActivity(it);

	}

	public void testaInicializacao() throws Exception {
		if (Controller.findStatus().isEmpty()) {
			Controller.insertStatus();
		}
	}

	public void exibeDialogo(String mensagem) {
		alert = new AlertDialog.Builder(context);
		alert.setPositiveButton("OK", null);
		alert.setMessage(mensagem);
		alert.create().show();
	}
	
	// Exporta Banco de dados para arquivo CSV
		public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {

			private final ProgressDialog dialog = new ProgressDialog(
					LoginActivity.this);

			@Override
			protected void onPreExecute()

			{

				this.dialog.setMessage("Exportando dados...");

				this.dialog.show();

			}

			protected Boolean doInBackground(final String... args)

			{
				SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
						Context.MODE_PRIVATE, null);

				File dbFile = getDatabasePath("database_name");
				// AABDatabaseManager dbhelper = new
				// AABDatabaseManager(getApplicationContext());
				MySQLiteOpenHelper dbhelper = new MySQLiteOpenHelper(
						LoginActivity.this);
				System.out.println(dbFile); // displays the data base path in your
											// logcat

				File exportDir = new File(
						Environment.getExternalStorageDirectory(), "");

				if (!exportDir.exists())

				{
					exportDir.mkdirs();
				}

				File file = new File(exportDir, "UsuariosSGOP.csv");

				try

				{

					if (file.createNewFile()) {
						System.out.println("Arquivo criado com sucesso!");
						System.out.println("meuarquivo.csv "
								+ file.getAbsolutePath());
					} else {
						System.out.println("Arquivo ja existe.");
					}

					CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
					// SQLiteDatabase db = dbhelper.getWritableDatabase();

					Cursor curCSV = db.rawQuery("select * from "
							+ " cad_usuario", null);

					csvWrite.writeNext(curCSV.getColumnNames());

					while (curCSV.moveToNext())

					{

						String arrStr[] = { curCSV.getString(0),
								curCSV.getString(1), curCSV.getString(2),
								curCSV.getString(3), curCSV.getString(4),
								curCSV.getString(5)/*, curCSV.getString(6),
								curCSV.getString(7)*/ };

						csvWrite.writeNext(arrStr);

					}

					csvWrite.close();
					curCSV.close();
					/*
					 * String data=""; data=readSavedData(); data= data.replace(",",
					 * ";"); writeData(data);
					 */

					return true;

				}

				catch (SQLException sqlEx)

				{

					Log.e("LoginActivity", sqlEx.getMessage(), sqlEx);

					return false;

				}

				catch (IOException e)

				{

					Log.e("LoginActivity", e.getMessage(), e);

					return false;

				}

			}

			protected void onPostExecute(final Boolean success)

			{

				if (this.dialog.isShowing())

				{

					this.dialog.dismiss();

				}

				if (success)

				{

					Toast.makeText(LoginActivity.this,
							"Dados exportados com sucesso", Toast.LENGTH_SHORT)
							.show();

				}

				else

				{

					Toast.makeText(LoginActivity.this, "Exportação falhou",
							Toast.LENGTH_SHORT).show();

				}
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
