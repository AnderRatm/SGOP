package com.sgop.activity;

import com.projeto.SGOP.R;
import com.sgop.fragment.*;
import com.sgop.principal.Controller;
import com.sgop.principal.FonteEntrada;
import com.sgop.principal.ListarActivity;
import com.sgop.principal.Sessao;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by anderson on 11/12/13.
 */
public class CadCategoria_FonteActivity extends Activity implements
OnGestureListener{
	private GestureDetector detector = null;

	EditText editDesc;
	Categoria reg;
	FonteEntrada fnt;
	private Controller usrController;
	private Context context;
	public static String chamada;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (chamada.equals("Fonte")) {
			ListarActivity.controle = "fonte_entrada";
		} else {
			ListarActivity.controle = "categoria";
		}
		
		if (item.getItemId() == R.id.bt_pesquisar) {
			Intent it = new Intent(getBaseContext(),
					com.sgop.principal.ListarActivity.class);
			startActivity(it);
			overridePendingTransition(
					R.animator.animation_direita_para_esquerda_aparece,
					R.animator.animation_direita_para_esquerda_some);
			finish();
			

		} else if (item.getItemId() == R.id.bt_editar) {

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
		setContentView(R.layout.activity_categoria);

		context = this;
		usrController = Controller.getInstance(context);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.detector = new GestureDetector(this);
		
		Button Cad_geral = (Button) findViewById(R.id.bt_CadastrarGeral);
		editDesc = (EditText) findViewById(R.id.descricao_Cadastro_geral_txt);

		Cad_geral.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					CadastrarCategoria();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void CadastrarCategoria() throws Exception {
		if (chamada.equals("Fonte")) {
			fnt = new FonteEntrada(null, 0);
			fnt.descricao = editDesc.getText().toString();
			fnt.cod_usr = Sessao.codigo_usr;

			if (fnt.descricao.equals("")) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						CadCategoria_FonteActivity.this);
				dialog.setMessage("Favor preencher todos os campos  ");
				dialog.setNeutralButton("OK", null);
				dialog.setTitle("Campos vazio!");
				dialog.show();
			} else {
				if (usrController.verificaFnt(fnt.descricao) == true) {
					try {
						FonteEntrada fonte = new FonteEntrada(fnt.descricao,
								fnt.cod_usr);

						usrController.insertFonteEntrada(fonte);
						editDesc.setText("");
						Toast toast = Toast.makeText(getApplicationContext(),
								"Dados gravados com sucesso!",
								Toast.LENGTH_LONG);
						toast.show();

					} catch (Exception e) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"Erro gravar dados!", Toast.LENGTH_LONG);
						toast.show();
						e.printStackTrace();
					}
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Fonte de entrada ja existe!", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		} else if (chamada.equals("Categoria")) {
			reg = new Categoria(null, 0);
			reg.descricao = editDesc.getText().toString();
			reg.cod_usr = Sessao.codigo_usr;

			if (reg.descricao.equals("")) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						CadCategoria_FonteActivity.this);
				dialog.setMessage("Favor preencher todos os campos  ");
				dialog.setNeutralButton("OK", null);
				dialog.setTitle("Campos vazio!");
				dialog.show();
			} else {
				if (usrController.verificaCateg(reg.descricao) == true) {
					try {
						Categoria categoria = new Categoria(reg.descricao,
								reg.cod_usr);
						usrController.insertCategoria(categoria);
						editDesc.setText("");
						Toast toast = Toast.makeText(getApplicationContext(),
								"Dados gravados com sucesso!",
								Toast.LENGTH_LONG);
						toast.show();

					} catch (Exception e) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"Erro gravar dados!", Toast.LENGTH_LONG);
						toast.show();
						e.printStackTrace();
					}
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Categoria ja existe!", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		}
	}

	@Override
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
