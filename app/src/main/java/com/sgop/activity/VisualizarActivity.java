package com.sgop.activity;

import java.text.NumberFormat;

import com.projeto.SGOP.R;
import com.sgop.principal.Controller;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.TextView;

public class VisualizarActivity extends Activity implements
OnGestureListener{
	private GestureDetector detector = null;
	double TotalEntradas;
	double TotalSaidas;
	double Total;
	Controller controler;
	private Context context;
	
	private NumberFormat nf =  NumberFormat.getCurrencyInstance();
	

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.bt_filter) {

		} else if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}



	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizar_status);

		context = this;
		controler = Controller.getInstance(context);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.detector = new GestureDetector(this);
		
		CalculaEntradas();
		try {
			CalculaSaidas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CalculaTotal();
		TextView TotalSaida = (TextView) findViewById(R.id.TextView_res_saidas_Visualizar);
		TotalSaida.setText(String.valueOf(nf.format(TotalSaidas)));

		TextView TotalEntrada = (TextView) findViewById(R.id.textView_res_entradas_visualizar);
		TotalEntrada.setText(String.valueOf(nf.format(TotalEntradas)));
		
		TextView Totalv = (TextView) findViewById(R.id.TextView_res_total_visualizar);
		Totalv.setText(String.valueOf(nf.format(Total)));
	}

	private void CalculaTotal() {
		// TODO Auto-generated method stub
		Total=(TotalEntradas-TotalSaidas);
		
	}

	private void CalculaSaidas() {
		// TODO Auto-generated method stub
		TotalSaidas = controler.findByFilter();
	}

	private void CalculaEntradas() {
		// TODO Auto-generated method stub
		TotalEntradas = controler.findByFilterEntradas();
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
