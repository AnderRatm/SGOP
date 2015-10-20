package com.sgop.fragment;

import com.projeto.SGOP.R;
import com.sgop.dao.Cad_CategoriaDAO;
import com.sgop.fragment.CustomBolDialogFragment.CustomBolDialogFragmentListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by anderson on 25/11/13.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class ListaBoletoActivityNew extends FragmentActivity implements
		CustomBolDialogFragmentListener {

	private Fragment contentFragment;
	private BolListFragment boletoListFragment;

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.bt_filter) {

		} else if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_filtro, menu);
		return true;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_boletos);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);

		android.app.FragmentManager fragmentManager = getFragmentManager();

		new Cad_CategoriaDAO(this);

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("content")) {
				savedInstanceState.getString("content");
			}
			if (fragmentManager.findFragmentByTag(BolListFragment.ARG_ITEM_ID) != null) {
				boletoListFragment = (BolListFragment) fragmentManager
						.findFragmentByTag(BolListFragment.ARG_ITEM_ID);
				contentFragment = boletoListFragment;
			}
		} else {
			boletoListFragment = new BolListFragment();
			setTitle(R.string.app_name);
			switchContent(boletoListFragment, "teste");
		}
	}
	

	@Override
	public void onSaveInstanceState(Bundle outState) {

		outState.putString("content", BolListFragment.ARG_ITEM_ID);

		super.onSaveInstanceState(outState);
	}

	/*
	 * We consider BolListFragment as the home fragment and it is not added to
	 * the back stack.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void switchContent(Fragment fragment, String tag) {
		android.app.FragmentManager fragmentManager = getFragmentManager();
		while (fragmentManager.popBackStackImmediate())
			;

		if (fragment != null) {
			android.app.FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.content_frame, fragment, tag);
			// Only EmpAddFragment is added to the back stack.
			if (!(fragment instanceof BolListFragment)) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			contentFragment = fragment;
		}
	}

	/*
	 * We call super.onBackPressed(); when the stack entry count is > 0. if it
	 * is instanceof BolListFragment or if the stack entry count is == 0, then
	 * we prompt the user whether to quit the app or not by displaying dialog.
	 * In other words, from BolListFragment on back press it quits the app.
	 */
	@Override
	public void onBackPressed() {
		android.app.FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			super.onBackPressed();
		} else if (contentFragment instanceof BolListFragment
				|| fm.getBackStackEntryCount() == 0) {
			finish();
			// Shows an alert dialog on quit

		}
	}

	/*
	 * Callback used to communicate with BolListFragment to notify the list
	 * adapter. Communication between fragments goes via their Activity class.
	 */
	@Override
	public void onFinishDialog() {
		if (boletoListFragment != null) {
			boletoListFragment.updateView();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
