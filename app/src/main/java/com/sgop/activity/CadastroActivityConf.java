package com.sgop.activity;

import com.projeto.SGOP.R;
import com.sgop.principal.Controller;
import com.sgop.principal.Registro;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by anderson on 18/11/13.
 */

public class CadastroActivityConf extends Activity implements
OnGestureListener{
	private GestureDetector detector = null;

	String cod_ativacao, senha_usr, telefone_usr, nome_usr, email_usr, cod_sms;
	Registro reg;
	private Context context;
	private Controller usrController;
	EditText Cod_ativacao;
	private String envio;

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.bt_pesquisar) {

		} else if (item.getItemId() == R.id.bt_editar) {

		} else if (item.getItemId() == android.R.id.home) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Cadastro cancelado!", Toast.LENGTH_LONG);
			toast.show();
			finish();

		}
		return super.onOptionsItemSelected(item);

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro2);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		context = this;
		usrController = Controller.getInstance(context);
		this.detector = new GestureDetector(this);

		TextView nome_exib = (TextView) findViewById(R.id.tv_nome);
		// TextView nome = new TextView(this);
		TextView email_exib = (TextView) findViewById(R.id.tv_email);
		// TextView email = new TextView(this);
		Cod_ativacao = (EditText) findViewById(R.id.cod_txt);
		// final EditText Cod_ativacao = new EditText(this);

		Button Bt_next = (Button) findViewById(R.id.bt_finalizar_cadastro);

		Intent intent = getIntent();
		Bundle params = intent.getExtras();

		if (params != null) {

			email_usr = params.getString("email");
			email_exib.setText(email_usr);

			senha_usr = params.getString("senha");

			telefone_usr = params.getString("telefone");

			nome_usr = params.getString("nome");
			nome_exib.setText(nome_usr);

			String cod_usr = params.getString("cod_email");
			cod_ativacao = cod_usr;

			envio = params.getString("envio");
			System.out.printf(envio);

		}
		if (envio.equals("sms")) {
			new CheckSms(this).execute();
		}

		Bt_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String temp = Cod_ativacao.getText().toString();
				if (cod_ativacao.equals(temp)) {

					try {
						Registro registro = new Registro(email_usr, senha_usr,
								telefone_usr, nome_usr, cod_ativacao);

						try {
							usrController.insert(registro);
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"Cadastro realizado com sucesso!",
									Toast.LENGTH_LONG);
							toast.show();
							Cod_ativacao.setText("");
							AbriMenuPrincipal();

						} catch (Exception e) {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"Erro ao cadastrar!", Toast.LENGTH_LONG);
							toast.show();

							Intent it = new Intent(view.getContext(),
									CadastroActivity.class);
							Bundle params = new Bundle();

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Codigo invalido!", Toast.LENGTH_LONG);
					toast.show();
					Cod_ativacao.setText("");
					System.out.println("Código de ativação " + cod_ativacao);

				}
			}
		});

	}

	private void AbriMenuPrincipal() {
		Intent it = new Intent();
		it.setClass(this, LoginActivity.class);
		startActivity(it);
		finish();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Intent intent = getIntent();
		Bundle params = intent.getExtras();

		if (params != null) {

			cod_sms = params.getString("codigo_sms");
			Cod_ativacao.setText(cod_sms);

		}
	}

	class CheckSms extends AsyncTask<Void, Void, Void> {

		private Context context;
		private ProgressDialog dialog;

		public CheckSms(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("Cadastro de usuario");
			dialog.setMessage("Detectando código enviado via SMS...");
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) { //
			// SystemClock.sleep(7000);
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			dialog.dismiss();
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
