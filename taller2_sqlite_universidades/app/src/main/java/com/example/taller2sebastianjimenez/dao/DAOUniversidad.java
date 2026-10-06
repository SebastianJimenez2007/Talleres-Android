package com.example.taller2sebastianjimenez.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taller2sebastianjimenez.conexion.ConexionSQLiteHelper;
import com.example.taller2sebastianjimenez.entidades.Universidad;

import java.util.ArrayList;
import java.util.List;

public class DAOUniversidad {

    private final ConexionSQLiteHelper conexionHelper;

    public DAOUniversidad(Context context) {
        this.conexionHelper = new ConexionSQLiteHelper(context);
    }

    // Agregar una universidad
    public long agregarUniversidad(Universidad universidad) {
        SQLiteDatabase db = conexionHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConexionSQLiteHelper.CAMPO_NOMBRE, universidad.getNombre());
        values.put(ConexionSQLiteHelper.CAMPO_WWW, universidad.getWww());

        long idResultado = db.insert(ConexionSQLiteHelper.TABLA_UNIVERSIDADES, null, values);
        db.close();
        return idResultado;
    }


    public Universidad consultarUnaUniversidad(String id) {
        SQLiteDatabase db = conexionHelper.getReadableDatabase();
        Universidad universidad = null;

        Cursor cursor = db.query(
                ConexionSQLiteHelper.TABLA_UNIVERSIDADES,
                new String[]{ConexionSQLiteHelper.CAMPO_ID, ConexionSQLiteHelper.CAMPO_NOMBRE, ConexionSQLiteHelper.CAMPO_WWW},
                ConexionSQLiteHelper.CAMPO_ID + "=?",
                new String[]{id},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int idVal = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_NOMBRE));
            String www = cursor.getString(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_WWW));

            universidad = new Universidad(idVal, nombre, www);
            cursor.close();
        }

        db.close();
        return universidad;
    }


    public List<Universidad> listarTodasLasUniversidades() {
        List<Universidad> lista = new ArrayList<>();
        SQLiteDatabase db = conexionHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ConexionSQLiteHelper.TABLA_UNIVERSIDADES + " ORDER BY " + ConexionSQLiteHelper.CAMPO_ID + " DESC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_NOMBRE));
                String www = cursor.getString(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_WWW));

                lista.add(new Universidad(id, nombre, www));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return lista;
    }


    public List<Universidad> buscarUniversidades(String query) {
        List<Universidad> lista = new ArrayList<>();
        SQLiteDatabase db = conexionHelper.getReadableDatabase();

        String selection = ConexionSQLiteHelper.CAMPO_NOMBRE + " LIKE ? OR " + ConexionSQLiteHelper.CAMPO_ID + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(
                ConexionSQLiteHelper.TABLA_UNIVERSIDADES,
                new String[]{ConexionSQLiteHelper.CAMPO_ID, ConexionSQLiteHelper.CAMPO_NOMBRE, ConexionSQLiteHelper.CAMPO_WWW},
                selection, selectionArgs, null, null,
                ConexionSQLiteHelper.CAMPO_ID + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_NOMBRE));
                String www = cursor.getString(cursor.getColumnIndexOrThrow(ConexionSQLiteHelper.CAMPO_WWW));

                lista.add(new Universidad(id, nombre, www));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return lista;
    }


    public int borrarUniversidad(String id) {
        SQLiteDatabase db = conexionHelper.getWritableDatabase();
        int filasAfectadas = db.delete(
                ConexionSQLiteHelper.TABLA_UNIVERSIDADES,
                ConexionSQLiteHelper.CAMPO_ID + "=?",
                new String[]{id}
        );
        db.close();
        return filasAfectadas;
    }


    public int editarUniversidad(Universidad universidad) {
        SQLiteDatabase db = conexionHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConexionSQLiteHelper.CAMPO_NOMBRE, universidad.getNombre());
        values.put(ConexionSQLiteHelper.CAMPO_WWW, universidad.getWww());

        int filasAfectadas = db.update(
                ConexionSQLiteHelper.TABLA_UNIVERSIDADES,
                values,
                ConexionSQLiteHelper.CAMPO_ID + "=?",
                new String[]{String.valueOf(universidad.getId())}
        );
        db.close();
        return filasAfectadas;
    }


    public int proximoId() {
        SQLiteDatabase db = conexionHelper.getReadableDatabase();
        int proximo = 1;

        Cursor cursor = db.rawQuery("SELECT MAX(" + ConexionSQLiteHelper.CAMPO_ID + ") FROM " + ConexionSQLiteHelper.TABLA_UNIVERSIDADES, null);
        if (cursor != null && cursor.moveToFirst()) {
            proximo = cursor.getInt(0) + 1;
            cursor.close();
        }

        db.close();
        return proximo;
    }
}
