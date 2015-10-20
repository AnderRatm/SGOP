package com.sgop.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.sgop.fragment.Boleto;
import com.sgop.fragment.Boleto.Status;
import com.sgop.fragment.Categoria;
import com.sgop.principal.Sessao;




/**
 * Created by anderson on 24/11/13.
 */
public class Cad_BoletoDAO extends  MySQLiteOpenHelper {
    public static final String TABLE = "cad_boleto";
    private final String TABLE_ST = "status";  
	public static String[] return_vetor;
    public static final String BOLETO_ID_WITH_PREFIX = "bol._id";
	public static final String BOLETO_CODIGO_WITH_PREFIX = "bol.codigo";
	public static final String CATEGORIA_DESC_WITH_PREFIX = "catg.descricao";
	
	//private static final String WHERE_ID_EQUALS = SQLiteOpenHelper.ID_COLUMN + " =?";
    public Cad_BoletoDAO(Context context) {
        super(context);
    }
    public void open()  throws SQLException {
        database = getWritableDatabase();
    }

    public void insert(Boleto boleto) throws Exception {
        ContentValues values = new ContentValues();


        values.put("codigo_boleto", boleto.getCod_boleto());
        values.put("data_cadastro", boleto.getDt_cad());
        values.put("data_vencimento", boleto.getDt_venc());
        values.put("valor", boleto.getValor_calc());
        values.put("emissor", boleto.getInstituicao());       
        values.put("Codigo_categoria", boleto.getCodigo_categoria());
        values.put("Codigo_usuario",boleto.getCodigo_usr());
        values.put("Cod_status", boleto.getCodigo_status());

        getDatabase().insert(TABLE, null, values);
    }

    public  long update(Boleto boleto)  {
        ContentValues values = new ContentValues();

        values.put("codigo_boleto", boleto.getCod_boleto());
        values.put("data_cadastro", boleto.getDt_cad());
        values.put("data_vencimento", boleto.getDt_venc());
        values.put("valor", boleto.getValor_calc());
        values.put("emissor", boleto.getInstituicao());
        values.put("Codigo_categoria", 1);
        values.put("Codigo_usuario",boleto.getCodigo_usr());
        values.put("Cod_status", 1);

        long result = getDatabase().update(TABLE, values, "codigo = ?", 
        		new String[]{String.valueOf(boleto.getId())});
       
		Log.d("Update Result:", "=" + result);
		return result;
    }
 
    
    public int deleteBoleto(Boleto boleto) {
		return database.delete(TABLE, "_id=?",
				new String[] { boleto.getId() + "" });
	}

    public List<Boleto> findByDAtaVencimento(Date dt_venc) {
        List<Boleto> retorno = new ArrayList<Boleto>();
        String sql = "SELECT * FROM " + TABLE + " WHERE dt_venc = ?";
        String[] selectionArgs = new String[] { "" + dt_venc };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaBoleto(cursor));
            cursor.moveToNext();
        }
        return retorno;

    }
    public List<Boleto> findByCodigo(int codigo) {
        List<Boleto> retorno = new ArrayList<Boleto>();
        String sql = "SELECT * FROM " + TABLE + " WHERE codigo = ?";
        String[] selectionArgs = new String[] { "" + codigo };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaBoleto(cursor));
            cursor.moveToNext();
    }
        return retorno;
    }
    public List<Boleto> findByStatus(String status) {
        List<Boleto> retorno = new ArrayList<Boleto>();
        String sql = "SELECT * FROM " + TABLE + " WHERE status = ?";
        String[] selectionArgs = new String[] { "" + status };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaBoleto(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public List<Boleto> findAll() throws Exception {
        List<Boleto> retorno = new ArrayList<Boleto>();
        String sql = "SELECT * FROM " + TABLE;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaBoleto(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public List<Boleto> getAll() {
        List<Boleto> notes = new ArrayList<Boleto>();
        String sql = "SELECT * FROM " + TABLE;
        Cursor cursor = getDatabase().rawQuery(sql, null);
         cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Boleto note = new Boleto();
            note.setCod_boleto(cursor.getString(1));
            note.setDt_cad(cursor.getString(2));
            note.setDt_venc(cursor.getString(3));
            note.setInstituicao(cursor.getString(5));
            note.setValor_calc(cursor.getInt(4));
            notes.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return notes;
    }
    public Cursor getData2(){
        Cursor mCursor = database.query(TABLE, new String[] {"codigo","codigo_boleto",
                "data_cadastro","data_vencimento","valor","emissor" },
                null, null, null, null, null,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
 
    public Boleto montaBoleto(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }

        String codigo_boleto =	cursor.getString(cursor.getColumnIndex("codigo_boleto"));
        String dt_cad = cursor.getString(cursor.getColumnIndex("data_cadastro"));
        String dt_venc = cursor.getString(cursor.getColumnIndex("data_vencimento"));
        Double valor = cursor.getDouble(cursor.getColumnIndex("valor"));
        String emissor = cursor.getString(cursor.getColumnIndex("emissor"));
        int codigo_usr = cursor.getInt(cursor.getColumnIndex("Codigo_usuario"));
        int Cd_status = cursor.getInt(cursor.getColumnIndex("Cad_status"));
        int Cd_cat = cursor.getInt(cursor.getColumnIndex("Codigo_categoria"));

        return new Boleto(codigo_boleto,dt_cad,dt_venc,valor,emissor,codigo_usr,Cd_status,Cd_cat);

    }

    public Status montaStatus(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        int cod_status =	cursor.getInt(cursor.getColumnIndex("cod_status"));
        String desc = cursor.getString(cursor.getColumnIndex("descricao"));

        return new Boleto.Status(cod_status,desc);
    }

    public long createStatus(Integer id, String descricao ) {

        ContentValues initialValues = new ContentValues();
        initialValues.put("cod_status", id);
        initialValues.put("descricao", descricao);


        return database.insert(TABLE_ST, null, initialValues);
    }


    public void insertStatus() throws Exception {
        new ContentValues();

        createStatus(1,"OK");
        createStatus(2,"Pen");
        createStatus(3,"Ven");

    }
    public List<String> findStatus() throws Exception {
    	ArrayList<String>Return_cod = new ArrayList<String>();
        ArrayList<String> retorno = new ArrayList<String>();
        String sql = "SELECT cod_status, descricao from " + TABLE_ST  +
        		" ORDER BY descricao";  
      
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            Return_cod.add(cursor.getString(cursor.getColumnIndexOrThrow("cod_status")));
            cursor.moveToNext();
        }
        return_vetor=(String[])Return_cod.toArray(new String[Return_cod.size()]);  
            return retorno;
    }
    
 // metodo 1
    // Usando rawQuery() para juntar tabelas
    
   public ArrayList<Boleto> getBoletos() {
	   
        ArrayList<Boleto> boletos = new ArrayList<Boleto>();
        
//SELECT emp.id,emp.name,dob,salary,dept_id,dept.name FROM employee emp, department dept WHERE emp.dept_id = dept.id 
//SELECT bol.id,bol.codigo_boleto,data_vencimento,valor,emissor,Codigo_categoria,catg.descricao FROM cad_boleto bol, categoria catg WHERE bol.Codigo_categoria = dept._id
//SELECT bol._id,bol.codigo_boleto,data_cadastro,data_vencimento,valor,emissor,Codigo_categoria,Cod_status , catg.descricao, st.descricao 
        //FROM cad_boleto bol, categoria catg, status st WHERE bol.Codigo_categoria = catg._id AND bol.Cod_status = st.cod_status AND bol.Codigo_usuario = 1
        

       String query = "SELECT " + "bol._id" + ","
                + "bol.codigo_boleto"                
        		+ "," + "data_cadastro"
        		+ "," + "data_vencimento"
                + "," + "valor" + ","
                + "emissor" + ","
                + "Codigo_categoria" + ","
                + "bol.Cod_status" + " , "
                + "catg.descricao, " 
                + "st.descricao" +" FROM "
                + TABLE + " bol, "
                + MySQLiteOpenHelper.DEPARTMENT_TABLE + " catg, status st WHERE bol."
                + MySQLiteOpenHelper.EMPLOYEE_DEPARTMENT_ID + " = catg."
                + MySQLiteOpenHelper.ID_COLUMN
                + " AND bol.Cod_status = st.cod_status"
                + " AND bol.Codigo_usuario = " + Sessao.codigo_usr;
 
       /* String query = "SELECT " + "bol._id" + ","
                + "bol.codigo_boleto"                
        		+ "," + "data_cadastro"
        		+ "," + "data_vencimento"
                + "," + "valor" + ","
                + "emissor" + ","
                + "Codigo_categoria" + ","             
                + "catg.descricao "  +" FROM "
                + TABLE + " bol, "
                + MySQLiteOpenHelper.DEPARTMENT_TABLE + " catg WHERE bol."
                + MySQLiteOpenHelper.EMPLOYEE_DEPARTMENT_ID + " = catg."
                + MySQLiteOpenHelper.ID_COLUMN               
                + " AND bol.Codigo_usuario = " + Sessao.codigo_usr;
        	*/
 
        Log.d("query", query);
        Cursor cursor = getDatabase().rawQuery(query, null);
        while (cursor.moveToNext()) {
            Boleto boleto = new Boleto();
            boleto.setId(cursor.getInt(0));
            boleto.setCod_boleto(cursor.getString(1));
            boleto.setDt_cad(cursor.getString(2)); 
            boleto.setDt_venc(cursor.getString(3));
            boleto.setValor_calc(cursor.getDouble(4));
            boleto.setInstituicao(cursor.getString(5));  
 
            Categoria categoria = new Categoria();
            categoria.setId(cursor.getInt(6));
            categoria.setDescricao(cursor.getString(8));
            
            Status st = new Status();
            st.setCod_status(cursor.getInt(7));
            st.setDesc(cursor.getString(9));
 
            boleto.setCategoria(categoria);
            boleto.setStatus(st);
 
            boletos.add(boleto);
        }
        return boletos;
    }
public List<String> findByFilter() {	
     
        ArrayList<String>TotalBoletos = new ArrayList<String>();
        String sql = "SELECT valor from " + TABLE  +
        		" where Codigo_usuario = " + Sessao.codigo_usr;
        
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {           
        	TotalBoletos.add(cursor.getString(cursor.getColumnIndexOrThrow("valor")));            
            cursor.moveToNext();
        }
       // return_vetor=(String[])Return_cod.toArray(new String[Return_cod.size()]);          
        return TotalBoletos;
    }

}
    
    //metodo 2
	/*public ArrayList<Boleto> getBoletos() {
		ArrayList<Boleto> retorno = new ArrayList<Boleto>();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables("cad_boleto" + " INNER JOIN " + "categoria"
				+ " ON " + "Codigo_categoria" + " = " + ("categoria._id"));

		// Get cursor
		
		Cursor cursor = queryBuilder.query(database, new String[] {

		/*
		 * public static final String BOLETO_ID_WITH_PREFIX = "emp.id"; public
		 * static final String BOLETO_NAME_WITH_PREFIX = "emp.name"; public
		 * static final String DEPT_NAME_WITH_PREFIX = "dept.name"; private
		 * static final String WHERE_ID_EQUALS = MySQLiteOpenHelper.ID_COLUMN +
		 * " =?";*/
		 /*

		"boleto.id",  "boleto.codigo_boleto",
				"emissor", "data_cadastro", "data_vencimento", "valor",
				"Codigo_categoria", "categoria.descricao" }, null, null, null, null,
				null);
		/*
		 * MySQLiteOpenHelper.BOLETO_DOB, MySQLiteOpenHelper.BOLETO_SALARY,
		 * MySQLiteOpenHelper.BOLETO_DEPARTMENT_ID,
		 * MySQLiteOpenHelper.DEPARTMENT_TABLE + "." + MySQLiteOpenHelper.NAME_COLUMN },
		 * null, null, null, null, null);
		 
		while (cursor.moveToNext()) {
			Boleto boleto = new Boleto();

			boleto.setId(cursor.getInt(0));
			boleto.setCod_boleto(cursor.getString(1));
			boleto.setInstituicao(cursor.getString(2));
			boleto.setDt_cad(cursor.getString(3));
			boleto.setDt_venc(cursor.getString(4));
			boleto.setValor_calc(cursor.getDouble(5));
			//boleto.setCodigo_status(cursor.getInt(6));

			Categoria categoria = new Categoria();
			categoria.setId(cursor.getInt(6));
			categoria.setDescricao(cursor.getString(7));

			boleto.setCategoria(categoria);

			retorno.add(boleto);
		}
		return retorno;
	}
}*/



