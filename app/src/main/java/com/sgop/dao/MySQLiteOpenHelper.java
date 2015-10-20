package com.sgop.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by anderson on 05/11/13.
 */
public class    MySQLiteOpenHelper extends SQLiteOpenHelper {


    private final static int VERSAO = 1;
    private final static String NOME = "CodBarras.db";
    
    private static final String DATABASE_NAME = "CodBarras.db";
	private static final int DATABASE_VERSION = 1;

	public static final String EMPLOYEE_TABLE = "cad_boleto"; //Boleto
	public static final String DEPARTMENT_TABLE = "categoria";//Categoria

	public static final String ID_COLUMN = "_id";
	public static final String NAME_COLUMN = "descricao";
	public static final String EMPLOYEE_DOB = "dob";
	public static final String EMPLOYEE_SALARY = "salary";
	public static final String EMPLOYEE_DEPARTMENT_ID = "Codigo_categoria";
    
    private static final String CREATE   = "CREATE TABLE  IF NOT EXISTS   cad_usuario " +
            "(codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
            " email VARCHAR ( 60 ), " +
            "senha VARCHAR( 8 )NOT NULL," +
            "telefone VARCHAR ( 21 )," +
            " nome VARCHAR ( 100), " +
            "cod_email VARCHAR (5));";
    protected SQLiteDatabase database;

    private static final String CREATE_CATEGORIA = "CREATE TABLE  IF NOT EXISTS categoria" +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "descricao VARCHAR(20) NOT NULL,"+
            "Codigo_usuario INTEGER NOT NULL,"+
            "FOREIGN KEY (Codigo_usuario) REFERENCES cad_usuario(codigo));";

    private static final String CREATE_STATUS = "CREATE TABLE IF NOT EXISTS  status" +
            " (cod_status INTEGER PRIMARY KEY AUTOINCREMENT," +
            "descricao VARCHAR(3) NOT NULL)";

    private static final String ESTORNO= "CREATE TABLE  IF NOT EXISTS estorno" +
            "(cod_estorno INTEGER PRIMARY KEY ," +
            "descricao varchar(500) NOT NULL,"+
            "Codigo_boleto INTEGER NOT NULL,"+
            "Codigo INTEGER NOT NULL,"+
            "Codigo_saida INTEGER NOT NULL,"+
            "Codigo_entrada INTEGER NOT NULL,"+
            "Codigo_usuario INTEGER NOT NULL,"+
            "FOREIGN KEY (Codigo_usuario) REFERENCES cad_usuario(codigo),"+
            "FOREIGN KEY (Codigo_boleto, Codigo) REFERENCES cad_boleto(codigo_boleto,_id ),"+
            "FOREIGN KEY (Codigo_saida) REFERENCES cad_saidas(_id),"+
            "FOREIGN KEY (Codigo_entrada) REFERENCES cad_entrada(_id));";

    private static final String CREATE_FONTE_ENTRADA = "CREATE TABLE IF NOT EXISTS  fonte_entrada  " +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "descricao VARCHAR(50) NOT NULL,"+
            "Codigo_usuario INTEGER NOT NULL,"+
            "FOREIGN KEY (Codigo_usuario) REFERENCES cad_usuario(codigo));";



    private static final String CREATE_BOLETO = "CREATE TABLE IF NOT EXISTS  cad_boleto" +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "codigo_boleto VARCHAR(50) ," +
            "data_cadastro VARCHAR(12) NOT NULL,"+
            " data_vencimento VARCHAR (12) ," +
            " valor NUMERIC (15,2)," +
            " emissor VARCHAR (50), " +
            "Codigo_categoria INTEGER NOT NULL,"+
            "Codigo_usuario INTEGER NOT NULL,"+
            "Cod_status INTEGER NOT NULL,"+
            "FOREIGN KEY (Codigo_categoria) REFERENCES categoria(_id),"+
            "FOREIGN KEY (Codigo_usuario) REFERENCES cad_usuario(codigo),"+
            "FOREIGN KEY (Cod_status) REFERENCES status(cod_status));";


    private static final String CREATE_SAIDAS = "CREATE TABLE IF NOT EXISTS cad_saidas" +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "descricao VARCHAR(50) NOT NULL," +
            "valor NUMERIC (10,2)NOT NULL,"+
            "data_vencimento DATE ," +           
            "Codigo_categoria INTEGER NOT NULL,"+
            "data_cadastro DATE NOT NULL,"+ 
            "Codigo_usuario INTEGER NOT NULL,"+
            "FOREIGN KEY (Codigo_usuario) REFERENCES cad_usuario(codigo),"+
            "FOREIGN KEY (Codigo_categoria) REFERENCES categoria(_id));";


    private static final String CREATE_ENTRADAS= "CREATE TABLE  IF NOT EXISTS cad_entradas" +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "descricao VARCHAR(50) NOT NULL," +
            "valor NUMERIC (10,2) NOT NULL,"+
            "data_cadastro DATE NOT NULL,"+   //Data da entrada 
            "Codigo_fonte INTEGER NOT NULL,"+
            "Codigo_usuario INTEGER NOT NULL,"+
            "FOREIGN KEY (Codigo_usuario) REFERENCES cad_usuario(codigo),"+
            "FOREIGN KEY (Codigo_fonte) REFERENCES fonte_entrada(_id));";



    private static final String CREATE_TOTAL= "CREATE TABLE IF NOT EXISTS  total_mensal" +
            "(mes_id INTEGER PRIMARY KEY ," +
            "ano_id INTEGER NOT NUll ," +
            "total_entradas NUMERIC (15,2) NOT NULL,"+
            "total_saidas NUMERIC (15,2) NOT NULL,"+
            "total_mes   NUMERIC (15,2) NOT NULL,"+
            "Codigo_boleto INTEGER NOT NULL,"+
            "Codigo INTEGER NOT NULL,"+
            "Codigo_saida INTEGER NOT NULL,"+
            "Codigo_entrada INTEGER NOT NULL,"+
            "Codigo_usuario INTEGER NOT NULL,"+
            "FOREIGN KEY (Codigo_usuario) REFERENCES cad_usuario(codigo),"+
            "FOREIGN KEY (Codigo_boleto, Codigo) REFERENCES cad_boleto(codigo_boleto,_id),"+
            "FOREIGN KEY (Codigo_saida) REFERENCES cad_saidas(codigo),"+
            "FOREIGN KEY (Codigo_entrada) REFERENCES cad_entrada(codigo));";



    public MySQLiteOpenHelper(Context context) {
        super(context, NOME, null, VERSAO);
    }

    public void onDelete (SQLiteDatabase db){
        db.execSQL("DROP TABLE"+CREATE);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        // TODO Auto-generated method stub
        if (!db.isReadOnly())
        {
            db.execSQL("PRAGMA foreign_keys=ON;");

        }
        db.execSQL(CREATE);
        db.execSQL(CREATE_CATEGORIA);
        db.execSQL(CREATE_STATUS);
        db.execSQL(ESTORNO);
        db.execSQL(CREATE_FONTE_ENTRADA);
        db.execSQL(CREATE_BOLETO);
        db.execSQL(CREATE_SAIDAS);
        db.execSQL(CREATE_ENTRADAS);
        db.execSQL(CREATE_TOTAL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE + "'");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_SAIDAS + "'");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_CATEGORIA +"'");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_ENTRADAS + "'");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_FONTE_ENTRADA + "'");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_STATUS+"'");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TOTAL+"'");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS '" + ESTORNO + "'");
        onCreate(db);


    }
    public SQLiteDatabase getDatabase() {
        if (database == null) {
            database = getWritableDatabase();
        }
        return database;
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly())
        {
            // db.execSQL("PRAGMA foreign_keys=ON;");

        }
    }




}