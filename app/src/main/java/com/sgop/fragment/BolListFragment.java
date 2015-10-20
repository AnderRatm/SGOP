package com.sgop.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.projeto.SGOP.R;
import com.sgop.dao.Cad_BoletoDAO;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by anderson on 25/11/13.
 */
@SuppressLint("NewApi")
public class BolListFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {

	public static final String ARG_ITEM_ID = "boleto_list";
	ArrayList<Boleto> lista;
	ListView boletolistView;
	Activity activity;

	

	BoletoListAdapter listaAdapt;
	Cad_BoletoDAO dao;
	private GetEmpTask task;

	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = getActivity();
		dao = new Cad_BoletoDAO(activity);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main, container, false);
		findViewsById(view);

		task = new GetEmpTask(activity);
		task.execute((Void) null);

		boletolistView.setOnItemClickListener(this);
		boletolistView.setOnItemLongClickListener(this);		
		return view;
		
	}

	private void findViewsById(View view) {
		boletolistView = (ListView) view.findViewById(R.id.listView1);
	}

	public class GetEmpTask extends AsyncTask<Void, Void, ArrayList<Boleto>> {

		private final WeakReference<Activity> activityWeakRef;

		public GetEmpTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected ArrayList<Boleto> doInBackground(Void... arg0) {
			ArrayList<Boleto> boletoList = dao.getBoletos();
			return boletoList;
		}

		protected void onPostExecute(ArrayList<Boleto> bolList) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				lista = bolList;
				if (bolList != null) {
					if (bolList.size() != 0) {
						listaAdapt = new BoletoListAdapter(activity, bolList);
						boletolistView.setAdapter(listaAdapt);
					} else {
						Toast.makeText(activity, "Não há boletos cadastrados",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}

	


	@Override
	public void onPause() {
		dao.close();
		super.onPause();
	}

	@Override
	
	public void onItemClick(AdapterView<?> list, View view, int i, long l) {
		Boleto boleto = (Boleto) boletolistView.getItemAtPosition(i);

		if (boleto != null) {
			Bundle arguments = new Bundle();
			arguments.putParcelable("selectedBoleto", boleto);			
			CustomBolDialogFragment customBolDialogFragment = new CustomBolDialogFragment();
			customBolDialogFragment.setArguments(arguments);
			customBolDialogFragment.show(getFragmentManager(),
					CustomBolDialogFragment.ARG_ITEM_ID);	
		
		}
	}
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Boleto boleto = (Boleto) parent.getItemAtPosition(position);
		// Use AsyncTask to delete from database
		
			dao.deleteBoleto(boleto);		
		listaAdapt.remove(boleto);

		return true;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	 
		public void updateView() {
			task = new GetEmpTask(activity);
			task.execute((Void) null);
		}
		public void onResume() {
			getActivity().setTitle(R.string.app_name);
			getActivity().getActionBar().setTitle(R.string.app_name);
			super.onResume();
		}
}
