package com.example.taller2sebastianjimenez.conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "bd_universidades.db";
    private static final int VERSION_BD = 1;


    public static final String TABLA_UNIVERSIDADES = "UNIVERSIDADES";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_WWW = "www";


    private static final String CREAR_TABLA_UNIVERSIDADES = "CREATE TABLE " + TABLA_UNIVERSIDADES + " (" +
            CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_NOMBRE + " TEXT NOT NULL, " +
            CAMPO_WWW + " TEXT" +
            ")";

    public ConexionSQLiteHelper(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_UNIVERSIDADES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_UNIVERSIDADES);
        onCreate(db);
    }
}
