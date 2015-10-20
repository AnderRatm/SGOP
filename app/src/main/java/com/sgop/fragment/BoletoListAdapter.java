package com.sgop.fragment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.projeto.SGOP.R;
import com.sgop.fragment.*;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class BoletoListAdapter extends ArrayAdapter<Boleto> {

	private Context context;
	List<Boleto> boletos;

	
	
	public BoletoListAdapter(Context context, List<Boleto> boletos) {
		super(context, R.layout.list_boleto, boletos);
		this.context = context;
		this.boletos = boletos;
	}

	private class ViewHolder {
		TextView bltIdTxt;
		TextView bltcodTxt;
		TextView bltDt_cadTxt;
		TextView bltDt_vencTxt;
		TextView bltVltTxt;
		TextView bltEmissorTxt;
		TextView bltStatusTxt;
		TextView bltCategoriaNameTxt;
	}

	@Override
	public int getCount() {
		return boletos.size();
	}

	@Override
	public Boleto getItem(int position) {
		return boletos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_boleto, null);
			holder = new ViewHolder();
			
			
			holder.bltcodTxt = (TextView) convertView
					.findViewById(R.id.codigo);
			holder.bltDt_cadTxt = (TextView) convertView
					.findViewById(R.id.cadastro);
			holder.bltDt_vencTxt = (TextView) convertView
					.findViewById(R.id.vencimento);
			holder.bltVltTxt= (TextView) convertView
					.findViewById(R.id.valor);
			holder.bltEmissorTxt= (TextView) convertView
					.findViewById(R.id.emissor);
			holder.bltStatusTxt= (TextView) convertView
					.findViewById(R.id.Status);
			holder.bltCategoriaNameTxt= (TextView) convertView
					.findViewById(R.id.categoria);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Boleto boleto= (Boleto) getItem(position);
		//holder.bltIdTxt.setText(boleto.getId() + "");
		holder.bltcodTxt.setText(boleto.getCod_boleto());
		holder.bltDt_cadTxt.setText(boleto.getDt_cad());
		holder.bltDt_vencTxt.setText(boleto.getDt_venc());
		holder.bltVltTxt.setText(boleto.getValor_calc() + "");
		holder.bltEmissorTxt.setText(boleto.getInstituicao());
		holder.bltStatusTxt.setText(boleto.getStatus().getDesc());
		holder.bltCategoriaNameTxt.setText(boleto.getCategoria().getDescricao());
		
		//holder.bltDobTxt.setText(formatter.format(boleto.getDateOfBirth()));	
		
		return convertView;
	}

	@Override
	public void add(Boleto boleto) {
		boletos.add(boleto);
		notifyDataSetChanged();
		super.add(boleto);
	}

	@Override
	public void remove(Boleto boleto) {
		boletos.remove(boleto);
		notifyDataSetChanged();
		super.remove(boleto);
	}	
}

