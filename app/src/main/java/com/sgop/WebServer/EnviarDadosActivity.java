package com.sgop.WebServer;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.projeto.SGOP.R;
import com.sgop.principal.Controller;
import com.sgop.principal.ListarActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnviarDadosActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;
	private Context context;
	private Controller usrController;
	String TotalDados[];

	JSONParser jsonParser = new JSONParser();

	/*
    $mysql_host = "mysql1.000webhost.com";
            $mysql_database = "a6869162_sgop";
            $mysql_user = "a6869162_admin";
            $mysql_password = "sgop2015";*/

	// url to create new product
	//private static String url_create_product = "http://10.0.2.2/android_connect/create_product.php";
	private static String url_create_product = "http://sgop.web44.net/BancoDeDados/criar_usuario.php";
	private static String url= "http://systemsgop.esy.es/BancoDeDados/criar_usuario.php";
	// JSON Node names
	private static final String TAG_SUCCESS = "Sucesso";
	private String query;
	private RequestQueue mQueue;
	private HttpClient mHttpClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.add_product);
		context = this;
		usrController = Controller.getInstance(context);
		
		
		/*****Consulta em todas as tabelas e realiza o  backup no servodor web *******/
		
		SQLiteDatabase db = openOrCreateDatabase("CodBarras.db",
				Context.MODE_PRIVATE, null);

		/*Cursor cursor = db.rawQuery("select * from "
				+ " cad_usuario, cad_boleto,cad_entradas,cad_saidas,categoria,fonte_entrada,status", null);*/
		Cursor cursor = db.rawQuery("select * from "
				+ " cad_usuario", null);

			query = "INSERT INTO cad_usuario (email,senha,telefone,nome,cod_email) ";
				 for (int i = 0 ; i< cursor.getColumnCount();i++){

				 }
			if (cursor.moveToFirst()) {
				query += "VALUES ('" + cursor.getString(1) + "', MD5('" + cursor.getString(2) + "'),'" + cursor.getString(3)
						+ "','" + cursor.getString(4) + "','" + cursor.getString(5) + "')";
			}
			 	System.out.println(TotalDados);
			    cursor.close();
			Log.d("onCreate", query);
		mHttpClient = new DefaultHttpClient();
		mQueue = Volley.newRequestQueue(EnviarDadosActivity.this, new HttpClientStack(mHttpClient));



		// creating new product in background thread
				//new CreateNewProduct().execute();
				//new syncUsuario().execute();
		request(query);
	}

	private void request(String query) {
		final String parameter = query;
		StringRequest myReq = new StringRequest(Request.Method.POST,
				url,
				createMyReqSuccessListener(),
				createMyReqErrorListener()) {

			protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("query", parameter);
				//params.put("param2", num2);
				return params;
			};
		};
		mQueue.add(myReq);
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return null;
	}

	private Response.Listener<String> createMyReqSuccessListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d("onResponse",  response );
			}
		};
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EnviarDadosActivity.this);
			pDialog.setMessage("Creating Product..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			
			
		
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			/*params.add(new BasicNameValuePair("nome", nome));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("senha", senha));
			params.add(new BasicNameValuePair("telefone", telefone));
			params.add(new BasicNameValuePair("cod_email", cod_email));*/

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					/*Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
					startActivity(i);*/
					
					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}


	class syncUsuario extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EnviarDadosActivity.this);
			pDialog.setMessage("Sincronizando dados...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("query", query));


			HttpClient httpclient=new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			try {

				HttpResponse response =httpclient.execute(httppost);
				// Examine the response status
				Log.i("Praeda", response.getStatusLine().toString());

				// Get hold of the response entity
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
				// to worry about connection release

				if (entity != null) {
					// A Simple JSON Response Read

					Log.d("Response:", entity.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// check for success tag
		/*	try {
				int success = ent.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					*//*Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
					startActivity(i);*//*

					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}*/

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}
}

