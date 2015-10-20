package com.sgop.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.projeto.SGOP.R;
import com.sgop.mail.Mail;
import com.sgop.principal.Controller;
import com.sgop.principal.Registro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroActivity extends Activity  implements
OnGestureListener{
	Registro reg;
	private GestureDetector detector = null;

	EditText nome, email, telefone, senha, confirmar_senha, cod_area;
	private Context context;
	private Controller usrController;
	String codigo_email = new String();
	private String parametro_envio;

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.bt_pesquisar) {
			// Enviar_SMS_codigo();

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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);

		context = this;
		usrController = Controller.getInstance(context);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		this.detector = new GestureDetector(this);

		Button BtCadastrar = (Button) findViewById(R.id.bt_Cadastrar);
		nome = (EditText) findViewById(R.id.nome_txt);
		email = (EditText) findViewById(R.id.email_txt);
		cod_area = (EditText) findViewById(R.id.cod_area_txt);
		telefone = (EditText) findViewById(R.id.Telefone_txt);
		senha = (EditText) findViewById(R.id.senhacadastro_txt);
		confirmar_senha = (EditText) findViewById(R.id.conf_senha_txt);

		GerarCodigoRandomico();

		BtCadastrar.setOnClickListener(new OnClickListener() {
			String conf_senha;

			@Override
			public void onClick(final View arg0) {
				// TODO Auto-generated method stub

				try {
					reg = new Registro();

					reg.nome = nome.getText().toString();
					reg.email = email.getText().toString();
					reg.telefone = cod_area.getText().toString()
							+ telefone.getText().toString();
					reg.senha = senha.getText().toString();
					conf_senha = confirmar_senha.getText().toString();
					reg.cod_email = codigo_email;

					/***** Realizar consulta no servidor ******/

					// verifica se ja existe um usuário com o email cadastrado
					if (usrController.verificaEmail(reg.email) == true) {						

						if (reg.nome.equals("")) {
							nome.setError("Informe o campo!");
							nome.requestFocus();

						} else if (reg.nome.length() < 2) {
							nome.setError("O nome deve ter no minímo 2 caracteres!");
							nome.requestFocus();

						} else if (reg.email.equals("")) {
							email.setError("Informe o campo!");
							email.requestFocus();

						} else if (validarEmail(reg.email) == false) {
							email.setError("Informe um e-mail válido!");
							email.requestFocus();

						} else if (cod_area.getText().length() < 2) {
							cod_area.setError("Informe o código de área com 2 digitos!");
							cod_area.requestFocus();

						} else if (reg.telefone.equals("")) {
							telefone.setError("Informe o campo!");
							telefone.requestFocus();

						} else if (telefone.getText().length() < 8
								|| telefone.getText().length() > 9) {
							telefone.setError("Informe um telefone válido! ex. 99887766");
							telefone.requestFocus();

						} else if (reg.senha.length() < 4) {
							senha.setError("Informe uma senha de no minimo 4 caracteres!");
							senha.requestFocus();

						} else {
							if (!reg.senha.equals(conf_senha)) {

								confirmar_senha
										.setError("Campo confirmar senha nao confere !");
								confirmar_senha.requestFocus();

							} else {

								AlertDialog.Builder builder = new AlertDialog.Builder(
										CadastroActivity.this);

								builder.setTitle("Código de cadastro ");
								builder.setMessage("Como deseja enviar o código de cadastro ?");

								builder.setPositiveButton("E-mail",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												if (existeConexao() == true) {

													try {
														parametro_envio = "email";
														Enviar_Email();
														NextStep();

													} catch (Exception e) {
														// TODO Auto-generated
														// catch
														// block

														e.printStackTrace();
													}
													dialog.dismiss();
												} else {
													AlertDialog.Builder dialog2 = new AlertDialog.Builder(
															CadastroActivity.this);
													dialog2.setMessage("Verifique sua conexão com a internet!");
													dialog2.setNeutralButton(
															"OK", null);
													dialog2.setTitle("Código de cadastro");
													dialog2.show();
												}
											}

										});

								builder.setNeutralButton("SMS",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												AlertDialog.Builder dialog2 = new AlertDialog.Builder(
														CadastroActivity.this);

												dialog2.setMessage("Ao selecionar envio de SMS,"
														+ " será cobrado o valor de um envio normal, "
														+ "de acordo com a sua operadora. ");
												dialog2.setTitle("Código de cadastro");

												dialog2.setNegativeButton(
														"Cancelar", null);
												dialog2.setPositiveButton(
														"OK",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {

																parametro_envio = "sms";
																Enviar_SMS_codigo();
																NextStep();
																dialog.dismiss();
															}

														});
												dialog2.show();
											}

										});
								builder.setNegativeButton("Cancelar",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												dialog.dismiss();
											}

										});

								AlertDialog alert = builder.create();
								alert.show();

							}
						}
					} else {
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								CadastroActivity.this);
						dialog.setMessage("Email já cadastrado, tente recuperar sua senha!  ");
						dialog.setNeutralButton("OK", null);
						dialog.setTitle("Usuário ja existente!");
						dialog.show();

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public boolean validarEmail(String email) {

		boolean isEmailIdValid = false;
		if (email != null && email.length() > 0) {
			String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			Pattern pattern = Pattern.compile(expression,
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()) {
				isEmailIdValid = true;
			}
		}
		return isEmailIdValid;
	}

	private void NextStep() {
		Intent it = new Intent(this.getBaseContext(),
				CadastroActivityConf.class);
		Bundle params = new Bundle();

		params.putString("email", email.getText().toString());
		params.putString("senha", senha.getText().toString());
		params.putString("telefone", telefone.getText().toString());
		params.putString("nome", nome.getText().toString());
		params.putString("cod_email", reg.cod_email);
		params.putString("envio", parametro_envio);
		it.putExtras(params);
		startActivity(it);
		nome.setText("");
		email.setText("");
		cod_area.setText("");
		telefone.setText("");
		senha.setText("");
		confirmar_senha.setText("");
		finish();
	}

	private void Enviar_SMS_codigo() {

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage("+55" + reg.telefone, null,
				"Esse é seu código de ativação: " + reg.cod_email, null, null);

	}

	private void GerarCodigoRandomico() {
		// TODO Auto-generated method stub

		codigo_email = "";
		char[] caracteres = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z'};

		for (int i = 0; i < 5; i++) {
			int temp = (int) (StrictMath.random() * 62);
			codigo_email = codigo_email + caracteres[temp];
		}
	}

	public void Enviar_Email() {

		// Criar uma conta de e-mail para o aplicativo
		final Mail m = new Mail("sgop.sistema@gmail.com", "SGOP1234@");

		String[] toArr = { reg.email };
		m.setTo(toArr);
		m.setFrom("sgop.sistema@gmail.com");
		m.setSubject("Código de ativação do sistema SGOP.");
		m.setBody("Olá "
				+ reg.nome
				+ ", para continuar o cadastro do sistema SGOP insira o código de acesso no campo solicitado em seu dispositivo android ."
				+ "\nSeu código de acesso é: " + reg.cod_email);

		new Thread(new Runnable()

		{
			public void run() {
				try {
					// m.addAttachment("/sdcard/filelocation");

					// faça qualquer coisa aqui

					if (m.send()) {
						/*
						 * Toast.makeText(CadastroActivity.this,
						 * "Email enviado com sucesso.", Toast.LENGTH_LONG)
						 * .show();
						 */
						System.out.println("Email Enviado com sucesso");

					} else {
						System.out.println("Email não foi enviado.");

					}

				} catch (Exception e) {
					// Toast.makeText(MailApp.this,
					// "There was a problem sending the email.",
					// Toast.LENGTH_LONG).show();

					Log.e("MailApp", "O e-mail não pode ser enviado.", e);

				}
			}
		}).start();

	}

	public boolean existeConexao() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo netInfo = connectivity.getActiveNetworkInfo();

			// Se não existe nenhum tipo de conexão retorna false
			if (netInfo == null) {
				return false;
			}

			int netType = netInfo.getType();

			// Verifica se a conexão é do tipo WiFi ou Mobile e
			// retorna true se estiver conectado ou false em
			// caso contrário
			if (netType == ConnectivityManager.TYPE_WIFI
					|| netType == ConnectivityManager.TYPE_MOBILE) {
				return netInfo.isConnected();

			} else {
				return false;
			}
		} else {
			return false;
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