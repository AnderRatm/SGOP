package com.sgop.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.projeto.SGOP.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResults;
import com.sgop.fragment.*;
import com.sgop.principal.Controller;
import com.sgop.principal.ListarActivity;
import com.sgop.principal.Sessao;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CamActivity extends Activity implements OnClickListener,
OnGestureListener{ 

	private GestureDetector detector = null;
	private static final String TAG = "CamActivity";
	private Button scan;
	String cod_boleto, segmento, instituicao, dt_venc, valor_duplicado, dt_cad;
	double valor_calc;
	private TextView formatTxt, contentTxt;
	private Controller usrController;
	private Context context;
	private int codigo_status;
	private Date cadastro;
	private Date vencimento;

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.bt_pesquisar) {

		} else if (item.getItemId() == R.id.bt_editar) {

		} else if (item.getItemId() == android.R.id.home) {
			finish();

		}
		return super.onOptionsItemSelected(item);

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cam);
		context = this;
		usrController = Controller.getInstance(context);
		
		this.detector = new GestureDetector(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		scan = (Button) findViewById(R.id.scan_button);
		formatTxt = (TextView) findViewById(R.id.scan_format);
		contentTxt = (TextView) findViewById(R.id.scan_content);

		scan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// respond to clicks
		if (v.getId() == R.id.scan_button) {
			// scan
		}

		try {
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve scan result
		IntentResults scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult.contents != null) {
			String scanContent = scanResult.getContents();
			String scanFormat = scanResult.getFormatName();

			cod_boleto = scanContent;
			if (scanFormat.equals("ITF")) {
				try {
					CalcularValor();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (instituicao.equals("")) {

					try {
						CalcularEMissor();

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				try {
					CalcularData();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					CapturarDataAtual();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			instituicao = segmento + " - " + instituicao;

			formatTxt.setText("FORMAT0: " + scanFormat + "\n Emissor  "
					+ segmento + "-" + instituicao + "\n Valor: " + valor_calc
					+ "\n Data de vencimento " + dt_venc
					+ "\n Data de cadastro " + dt_cad);
			contentTxt.setText("CÓDIGO: " + scanContent);

			final int codigo_usr = Sessao.codigo_usr;
			
			SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
			
				try {
				cadastro = new Date (ft.parse(dt_cad).getTime());
				vencimento = new Date (ft.parse(dt_venc).getTime());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
			
		
			if (cadastro.before(vencimento)) {
				// createStatus(1,"OK");
				// createStatus(2,"Pen");
				// createStatus(3,"Ven");
				codigo_status = 2;
			
			} else {
				codigo_status = 3;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("Status Boleto");
			builder.setMessage("O boleto ja está pago?");

			builder.setPositiveButton("SIM",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Exportar dados alteraveis para activity alterar
							// boletos
							codigo_status = 1;							
							dialog.dismiss();
							onResume();

						}

					});

			builder.setNegativeButton("NÃO",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Mantem o valor calculado com base nas datas de
							// cadastro e
							// vencimento
							dialog.dismiss();
							onResume();
						}

					});
			AlertDialog alert = builder.create();
			
			
			
		
		
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("Gravar dados");
			builder.setMessage("O que deseja fazer com os dados do boleto?");

			builder.setPositiveButton("SALVAR",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Do do my action here
							//VerificaPago();
							if (VerificaExistente() == true) {
							
								try {
									final Boleto boleto = new Boleto(cod_boleto, instituicao, dt_venc,
											valor_calc, dt_cad, codigo_usr, 1, codigo_status);
									usrController.insertBoleto(boleto);
									Toast toast = Toast.makeText(
											getApplicationContext(),
											"Dados gravados com sucesso!",
											Toast.LENGTH_LONG);
									toast.show();

								} catch (Exception e) {
									e.printStackTrace();
									Toast toast = Toast.makeText(
											getApplicationContext(),
											"Erro ao gravar dados!",
											Toast.LENGTH_LONG);
									toast.show();
									Log.e(TAG, "Erro ao gravar");
								}
								dialog.dismiss();

							} else {
								Toast toast = Toast.makeText(
										getApplicationContext(),
										"Boleto ja existe!", Toast.LENGTH_LONG);
								toast.show();
							}
						}

					});

			builder.setNegativeButton("EDITAR",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Exportar dados alteraveis para activity alterar
							// boletos

							Intent intent = new Intent(CamActivity.this,
									EditarBoleto.class);
							Bundle params = new Bundle();

							params.putString("cod", cod_boleto);
							params.putString("inst", instituicao);
							params.putString("dt_venc", dt_venc);
							params.putDouble("valor", valor_calc);
							params.putString("DtCad", dt_cad);
							intent.putExtras(params);

							startActivity(intent);
							// finish();
							// conecta_thread();

							overridePendingTransition(
									R.animator.animation_direita_para_esquerda_aparece,
									R.animator.animation_direita_para_esquerda_some);

							dialog.dismiss();
						}
					});
			AlertDialog alert2 = builder.create();		
			alert2.show();
			alert.show();

		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Sem dados recebidos!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void CapturarDataAtual() {

		/***** Capturar data do sistema ****/
		Locale locale = new Locale("pt", "BR");
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy", locale);
		dt_cad = form.format(calendar.getTime());

	}

	/* *******Verificar boleto bancario ***** */
	private void CalcularValor() {
		String temp;
		int verificador = Character.getNumericValue(cod_boleto.charAt(3));
		if (verificador == 9) {// posiçao 4

			temp = cod_boleto.substring(10, 19);
			valor_calc = Double.parseDouble(temp) / 100;
			Toast toast = Toast.makeText(getApplicationContext(),
					"Valor boleto! " + valor_calc, Toast.LENGTH_SHORT);
			toast.show();

		} else {
			temp = cod_boleto.substring(5, 15);
			valor_calc = Double.parseDouble(temp) / 100;
			Toast toast = Toast.makeText(getApplicationContext(),
					"Valor boleto! " + valor_calc, Toast.LENGTH_SHORT);
			toast.show();
		}
		int banco = Integer.parseInt(cod_boleto.substring(0, 3));// posiçao 1-3
		switch (banco) {
		case 1:
			instituicao = "Banco do Brasil S.A.";
			break;
		case 33:
			instituicao = "Banco Santander (Brasil) S.A.";
			break;
		case 104:
			instituicao = "Caixa Econômica Federal";
			break;
		case 237:
			instituicao = " Banco Bradesco S.A.	";
			break;
		case 341:
			instituicao = "Banco Itaú S.A.	";
			break;
		case 356:
			instituicao = "Banco Real S.A.	";
			break;
		case 389:
			instituicao = "Banco Mercantil do Brasil S.A	";
			break;
		case 399:
			instituicao = "HSBC Bank Brasil S.A.";
			break;
		case 422:
			instituicao = "Banco Safra S.A.	";
			break;
		case 453:
			instituicao = "Banco Rural S.A.	";
			break;
		case 633:
			instituicao = "Banco Rendimento S.A.	";
			break;
		case 652:
			instituicao = "Itaú Unibanco Holding S.A.	";
			break;
		case 745:
			instituicao = "Banco Citibank S.A.	";
			break;
		case 748:
			instituicao = "Banco Sicredi S.A.	";
			break;

		default:
			instituicao = "";
		}
	}

	private void CalcularEMissor() {
		/* *******Define a instituição/ orgão emissor do boleto *** */
		int temp = Character.getNumericValue(cod_boleto.charAt(1));// posiçao 2
		switch (temp) {
		case 1:
			segmento = "Prefeituras";
			break;
		case 2:
			segmento = "Saneamento";
			break;
		case 3:
			segmento = "Energia Elétrica e Gás";
			break;
		case 4:
			segmento = "Telecomunicações";
			break;
		case 5:
			segmento = "Órgãos Governamentais";
			break;
		case 7:
			segmento = "Multas de trânsito";
			break;

		default:
			segmento = "Boleto bancário";

		}
		/*
		 * "1.Prefeituras; "2.Saneamento; "3.Energia Elétrica e Gás "4.Órgãos
		 * Governamentais "5.Carnes e Assemelhados ou demais "6.Empresas /
		 * Órgãos que serão identificadas através do CNPJ "7. Multas de trânsito
		 * "9. Uso exclusivo do banco"
		 */

		/*
		 * *******Define o valor do boleto calculo com digito verificador pelo
		 * módulo 11 ***
		 */
		if (cod_boleto.charAt(3) == 6) {

		} else if (cod_boleto.charAt(3) == 7) {

		} else if (cod_boleto.charAt(3) == 8) {

		} else if (cod_boleto.charAt(3) == 9) {

		}

	}

	private void CalcularData() {
		/**** Referencia 07/10/1997 - 01/01/2012 = 5199 dias ****/

		int dia, mes, ano, valor_boleto, valor_temp;
		valor_boleto = Integer.parseInt(cod_boleto.substring(5, 9));
		ano = valor_boleto / 365 + 1997;
		dia = 7;
		mes = 10;

		if (ano > 2016) {
			valor_temp = (valor_boleto % 365) - 5;
			int qnt_dia = 31;

			while (valor_temp > qnt_dia) {
				mes++;
				valor_temp = valor_temp - qnt_dia;
				if (mes > 12) {
					ano++;
					mes = 1;
				}
				if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8
						|| mes == 10 || mes == 12) {
					qnt_dia = 31;
				} else if (mes == 2) {
					qnt_dia = 28;
				} else {
					qnt_dia = 30;
				}
				if (mes > 12) {
					ano++;
					mes = 1;
				}
			}
			if (valor_temp < 31) {
				dia = valor_temp + dia;
				if (dia > qnt_dia) {
					dia = dia - qnt_dia;
					mes++;
				}
				if (mes > 12) {
					ano++;
					mes = 1;
				}
			}

		} else if (ano < 2016 && ano > 2012) {
			valor_temp = valor_boleto % 365 - 4;
			int qnt_dia = 31;

			while (valor_temp > qnt_dia) {
				mes++;
				valor_temp = valor_temp - qnt_dia;
				if (mes > 12) {
					ano++;
					mes = 1;
				}
				if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8
						|| mes == 10 || mes == 12) {
					qnt_dia = 31;
				} else if (mes == 2) {
					qnt_dia = 28;
				} else {
					qnt_dia = 30;
				}
				if (mes > 12) {
					ano++;
					mes = 1;
				}
			}
			if (valor_temp < 31) {
				dia = valor_temp + dia;
				if (dia > qnt_dia) {
					dia = dia - qnt_dia;
					mes++;
				}
				if (mes > 12) {
					ano++;
					mes = 1;
				}

			}
		} else {
			valor_temp = valor_boleto % 365 - 3;
			int qnt_dia = 31;

			while (valor_temp > qnt_dia) {
				mes++;
				valor_temp = valor_temp - qnt_dia;
				if (mes > 12) {
					ano++;
					mes = 1;
				}
				if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8
						|| mes == 10 || mes == 12) {
					qnt_dia = 31;
				} else if (mes == 2) {
					qnt_dia = 28;
				} else {
					qnt_dia = 30;
				}
				if (mes > 12) {
					ano++;
					mes = 1;
				}
			}
			if (valor_temp < 31) {
				dia = valor_temp + dia;
				if (dia > qnt_dia) {
					dia = dia - qnt_dia;
					mes++;
				}
				if (mes > 12) {
					ano++;
					mes = 1;
				}
			}
		}

		if (dia < 10 && mes < 10) {
			dt_venc = String.valueOf("0" + dia)
					+ String.valueOf("/0" + mes + "/") + String.valueOf(ano);
		} else if (mes < 10) {
			dt_venc = String.valueOf(dia) + String.valueOf("/0" + mes + "/")
					+ String.valueOf(ano);
		} else if (dia < 10) {
			dt_venc = String.valueOf("0" + dia)
					+ String.valueOf("/" + mes + "/") + String.valueOf(ano);
		} else {
			dt_venc = String.valueOf(dia) + String.valueOf("/" + mes + "/")
					+ String.valueOf(ano);
		}

		/*
		 * SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		 * java.sql.Date dt_vencq = new
		 * java.sql.Date(format.parse(dt_venc).getTime());
		 */

	}

	private boolean VerificaExistente() {
		// TODO Auto-generated method stub
		// ******Busca no BD se existe um item ja cadastrado com o mesmo nome
		// que o usuario está tentando inserir

		SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
				Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT * FROM " + "cad_boleto"
				+ " WHERE codigo_boleto = ? AND Codigo_usuario = "
				+ Sessao.codigo_usr, new String[] { cod_boleto });

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
