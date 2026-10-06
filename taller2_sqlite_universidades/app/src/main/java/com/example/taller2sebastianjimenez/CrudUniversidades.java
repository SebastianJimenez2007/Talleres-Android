package com.example.taller2sebastianjimenez;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taller2sebastianjimenez.dao.DAOUniversidad;
import com.example.taller2sebastianjimenez.entidades.Universidad;

public class CrudUniversidades extends AppCompatActivity {

    private EditText txtId, txtNombre, txtWWW;
    private Button btnAgregar, btnBuscar, btnEditar, btnEliminar, btnLimpiar;
    private DAOUniversidad dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_universidades);

        // Inicialización del DAO
        dao = new DAOUniversidad(this);

        // Referencias a los componentes UI
        txtId = findViewById(R.id.txtId);
        txtNombre = findViewById(R.id.txtNombre);
        txtWWW = findViewById(R.id.txtWWW);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnLimpiar = findViewById(R.id.btnLimpiar);

        // Listeners de los botones
        btnAgregar.setOnClickListener(v -> agregarUniversidad());
        btnBuscar.setOnClickListener(v -> buscarUniversidad());
        btnEditar.setOnClickListener(v -> editarUniversidad());
        btnEliminar.setOnClickListener(v -> eliminarUniversidad());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
    }

    // ACCIÓN: AGREGAR
    private void agregarUniversidad() {
        String nombre = txtNombre.getText().toString().trim();
        String www = txtWWW.getText().toString().trim();

        // Validación: Nombre obligatorio
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "El nombre de la universidad es obligatorio", Toast.LENGTH_SHORT).show();
            txtNombre.requestFocus();
            return;
        }

        Universidad u = new Universidad(nombre, www);
        long result = dao.agregarUniversidad(u);

        if (result != -1) {
            txtId.setText(String.valueOf(result));
            Toast.makeText(this, "Universidad guardada con ID: " + result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error al guardar la universidad", Toast.LENGTH_SHORT).show();
        }
    }

    // ACCIÓN: BUSCAR
    private void buscarUniversidad() {
        String idStr = txtId.getText().toString().trim();

        if (TextUtils.isEmpty(idStr)) {
            Toast.makeText(this, "Ingrese un ID para buscar", Toast.LENGTH_SHORT).show();
            txtId.requestFocus();
            return;
        }

        Universidad u = dao.consultarUnaUniversidad(idStr);

        if (u != null) {
            txtNombre.setText(u.getNombre());
            txtWWW.setText(u.getWww());
            Toast.makeText(this, "Universidad encontrada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró universidad con ID: " + idStr, Toast.LENGTH_LONG).show();
        }
    }

    // ACCIÓN: EDITAR
    private void editarUniversidad() {
        String idStr = txtId.getText().toString().trim();
        String nombre = txtNombre.getText().toString().trim();
        String www = txtWWW.getText().toString().trim();

        if (TextUtils.isEmpty(idStr)) {
            Toast.makeText(this, "Ingrese el ID de la universidad a editar", Toast.LENGTH_SHORT).show();
            txtId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "El nombre de la universidad es obligatorio", Toast.LENGTH_SHORT).show();
            txtNombre.requestFocus();
            return;
        }

        int id = Integer.parseInt(idStr);
        Universidad u = new Universidad(id, nombre, www);

        int filas = dao.editarUniversidad(u);

        if (filas > 0) {
            Toast.makeText(this, "Universidad actualizada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se pudo actualizar. Verifique que el ID sea correcto", Toast.LENGTH_LONG).show();
        }
    }

    // ACCIÓN: ELIMINAR
    private void eliminarUniversidad() {
        String idStr = txtId.getText().toString().trim();

        if (TextUtils.isEmpty(idStr)) {
            Toast.makeText(this, "Ingrese el ID de la universidad a eliminar", Toast.LENGTH_SHORT).show();
            txtId.requestFocus();
            return;
        }

        int filas = dao.borrarUniversidad(idStr);

        if (filas > 0) {
            Toast.makeText(this, "Universidad eliminada correctamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "No existe universidad con ID: " + idStr, Toast.LENGTH_LONG).show();
        }
    }

    // ACCIÓN: LIMPIAR
    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtWWW.setText("");
        txtId.requestFocus();
        Toast.makeText(this, "Campos limpiados", Toast.LENGTH_SHORT).show();
    }
}
