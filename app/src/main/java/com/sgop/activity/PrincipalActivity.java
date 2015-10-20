package com.sgop.activity;

import com.projeto.SGOP.R;
import com.sgop.WebServer.EnviarDadosActivity;
import com.sgop.fragment.ListaBoletoActivityNew;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PrincipalActivity extends Activity implements
OnGestureListener {
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
			
			finish();
		}
		else if (id == R.id.action_settings) {

			Intent intent = new Intent(PrincipalActivity.this, EnviarDadosActivity.class);
			startActivity(intent);

			Toast.makeText(getBaseContext(), "Configurações", Toast.LENGTH_LONG)
					.show();
			return true;

		} else if (item.getItemId() == R.id.action_exportar_dados) {

			// getallDb();
			//new ExportDatabaseCSVTask().execute();
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
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.detector = new GestureDetector(this);
        /*final RadioGroup RB = (RadioGroup)findViewById(R.id.rb_group);

        final RadioButton RB1 =(RadioButton)findViewById(R.id.rb_cad_boleto);
        final RadioButton RB2 =(RadioButton)findViewById(R.id.rb_cad_entradas);
        final RadioButton RB3 =(RadioButton)findViewById(R.id.rb_cad_saidas);
        final RadioButton RB4 =(RadioButton)findViewById(R.id.rb_ver_status);
        final RadioButton RB5 =(RadioButton)findViewById(R.id.rb_ver_boletos);*/
        
        ImageButton RB1 = (ImageButton)findViewById(R.id.imageButton1);
        ImageButton RB2 = (ImageButton)findViewById(R.id.ImageButton01);
        ImageButton RB3 = (ImageButton)findViewById(R.id.ImageButton02);
        ImageButton RB4 = (ImageButton)findViewById(R.id.ImageButton03);
        ImageButton RB5 = (ImageButton)findViewById(R.id.ImageButton04);

        RB1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {             
                AbrirTelaCadBoleto();


            }
        });

        RB2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences.Editor editor;
               // editor.putString(PrincipalActivity.);              
                AbrirTelaCadEntrada();


            }
        });
        RB3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {              
                AbrirTelaCadSaidas();



            }
        });
        RB4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {               
                AbrirTelaVerStatus();

            }
        });
        RB5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {                
                AbrirTelaVerBoletos();

            }
        });
}

    private void AbrirTelaVerBoletos() {
        Intent it = new Intent();
        //it.setClass(this,VisualizarBoletosFragment.class);
        it.setClass(this,ListaBoletoActivityNew.class);
        startActivity(it);

    }

    private void AbrirTelaVerStatus() {
    	 Intent it = new Intent();
         it.setClass(this,VisualizarActivity.class);
         startActivity(it);
    }

    private void AbrirTelaCadSaidas() {
        Intent it = new Intent();
        it.setClass(this,CadastroSaidasActivity.class);
        startActivity(it);

    }

    private void AbrirTelaCadEntrada() {
        Intent it = new Intent();
        it.setClass(this,CadEntradasActivity.class);
        startActivity(it);


    }

    private void AbrirTelaCadBoleto() {

        Intent it = new Intent();
        it.setClass(this,CamActivity.class);
        startActivity(it);

    }

    @Override
    public void onBackPressed() {
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
