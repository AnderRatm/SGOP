package com.sgop.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.projeto.SGOP.R;
import com.sgop.dao.Cad_BoletoDAO;
import com.sgop.dao.Cad_CategoriaDAO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;



public class CustomBolDialogFragment extends DialogFragment {

	// UI references
	
	
	private EditText bolCodigotxt;
	private EditText bolValortxt;
	private EditText bolDatetxt;	
	private Spinner catgSpinner;
	private LinearLayout submitLayout;

	private Boleto boleto;

	Cad_BoletoDAO boletoDAO;
	ArrayAdapter<Categoria> adapter;

	public static final String ARG_ITEM_ID = "bol_dialog_fragment";
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"dd-MM-yyyy");
	
	/*
	 * Callback used to communicate with EmpListFragment to notify the list adapter.
	 * LoginActivity implements this interface and communicates with EmpListFragment.
	 */
	public interface CustomBolDialogFragmentListener {
		void onFinishDialog();

		void onBackPressed();
	}

	public CustomBolDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		boletoDAO = new Cad_BoletoDAO(getActivity());

		Bundle bundle = this.getArguments();
		boleto = bundle.getParcelable("selectedBoleto");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View customDialogView = inflater.inflate(R.layout.edit_boletos,
				null);
		builder.setView(customDialogView);

		bolCodigotxt = (EditText) customDialogView.findViewById(R.id.txv_Cod_boleto);
		bolDatetxt = (EditText) customDialogView
				.findViewById(R.id.tv_dt_boleto);
		bolValortxt = (EditText) customDialogView.findViewById(R.id.tv_valor_boleto);
		catgSpinner = (Spinner) customDialogView
				.findViewById(R.id.SpinnerCat);
		submitLayout = (LinearLayout) customDialogView
				.findViewById(R.id.layout_submit);
		submitLayout.setVisibility(View.GONE);
		setValue();

		builder.setTitle("Editar Boleto");
		builder.setCancelable(false);
		builder.setPositiveButton("Atualizar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							boleto.setDt_venc(formatter.parse(bolDatetxt.getText().toString()));
						} catch (ParseException e) {
							Toast.makeText(getActivity(),
									"Invalid date format!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						boleto.setCod_boleto(bolCodigotxt.getText().toString());
						boleto.setValor_calc(Double.parseDouble(bolValortxt
								.getText().toString()));
						Categoria dept = (Categoria) adapter
								.getItem(catgSpinner.getSelectedItemPosition());
						boleto.setCategoria(dept);
						long result = boletoDAO.update(boleto);
						if (result > 0) {
							ListaBoletoActivityNew activity = (ListaBoletoActivityNew) getActivity();
							activity.onFinishDialog();
						} else {
							Toast.makeText(getActivity(),
									"Erro ao atualziar boleto",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

		AlertDialog alertDialog = builder.create();

		return alertDialog;
	}

	private void setValue() {
		Cad_CategoriaDAO categoriaDAO = new Cad_CategoriaDAO(getActivity());

		List<Categoria> categorias = categoriaDAO.getCategorias();
		adapter = new ArrayAdapter<Categoria>(getActivity(),
				android.R.layout.simple_list_item_1, categorias);
		catgSpinner.setAdapter(adapter);
		int pos = adapter.getPosition(boleto.getCategoria());

		if (boleto != null) {
			bolCodigotxt.setText(boleto.getCod_boleto());
			bolDatetxt.setText(formatter.format(boleto.getDt_venc()));
			bolValortxt.setText(boleto.getValor_calc() + "");			
			catgSpinner.setSelection(pos);
		}
	}

	public void show(FragmentManager fragmentManager, String argItemId) {
		// TODO Auto-generated method stub
		
	}

	
}
