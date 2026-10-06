package com.example.taller2sebastianjimenez;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2sebastianjimenez.adapters.UniversidadAdapter;
import com.example.taller2sebastianjimenez.dao.DAOUniversidad;
import com.example.taller2sebastianjimenez.entidades.Universidad;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class UniversidadesActivity extends AppCompatActivity implements UniversidadAdapter.OnItemActionListener {

    private DAOUniversidad dao;
    private UniversidadAdapter adapter;
    private RecyclerView rvUniversidades;
    private LinearLayout layoutEmptyState;
    private SearchView searchView;
    private FloatingActionButton fabAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universidades);

        // Inicialización DAO
        dao = new DAOUniversidad(this);

        // UI Components
        rvUniversidades = findViewById(R.id.rvUniversidades);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        searchView = findViewById(R.id.searchView);
        fabAgregar = findViewById(R.id.fabAgregar);

        // Configurar RecyclerView
        rvUniversidades.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UniversidadAdapter(this);
        rvUniversidades.setAdapter(adapter);

        // Listener del Botón Flotante para Agregar
        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        // Listener del Buscador en Tiempo Real
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarLista(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarLista(newText);
                return true;
            }
        });

        // Cargar datos inicialmente
        cargarUniversidades();
    }

    private void cargarUniversidades() {
        List<Universidad> lista = dao.listarTodasLasUniversidades();
        actualizarUI(lista);
    }

    private void filtrarLista(String query) {
        if (TextUtils.isEmpty(query)) {
            cargarUniversidades();
        } else {
            List<Universidad> filtrada = dao.buscarUniversidades(query);
            actualizarUI(filtrada);
        }
    }

    private void actualizarUI(List<Universidad> lista) {
        adapter.setListaUniversidades(lista);
        if (lista.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvUniversidades.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvUniversidades.setVisibility(View.VISIBLE);
        }
    }

    // DIÁLOGO PARA AGREGAR
    private void mostrarDialogoAgregar() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_universidad, null);

        TextView tvTitulo = dialogView.findViewById(R.id.tvDialogTitulo);
        TextInputLayout tilNombre = dialogView.findViewById(R.id.tilNombre);
        TextInputEditText etNombre = dialogView.findViewById(R.id.etDialogNombre);
        TextInputEditText etWww = dialogView.findViewById(R.id.etDialogWww);

        tvTitulo.setText("Registrar Universidad");

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Guardar", null) // Se sobreescribe abajo para evitar que se cierre si hay error
                .setNegativeButton("Cancelar", (d, which) -> d.dismiss())
                .create();

        dialog.show();

        // Validar antes de guardar
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
            String www = etWww.getText() != null ? etWww.getText().toString().trim() : "";

            if (TextUtils.isEmpty(nombre)) {
                tilNombre.setError("El nombre es obligatorio");
                return;
            }

            tilNombre.setError(null);

            Universidad nuevaUni = new Universidad(nombre, www);
            long id = dao.agregarUniversidad(nuevaUni);

            if (id != -1) {
                Toast.makeText(this, "Universidad guardada correctamente", Toast.LENGTH_SHORT).show();
                cargarUniversidades();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // CALLBACK DEL ADAPTER: EDITAR
    @Override
    public void onEditar(Universidad universidad) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_universidad, null);

        TextView tvTitulo = dialogView.findViewById(R.id.tvDialogTitulo);
        TextInputLayout tilNombre = dialogView.findViewById(R.id.tilNombre);
        TextInputEditText etNombre = dialogView.findViewById(R.id.etDialogNombre);
        TextInputEditText etWww = dialogView.findViewById(R.id.etDialogWww);

        tvTitulo.setText("Editar Universidad #" + universidad.getId());
        etNombre.setText(universidad.getNombre());
        etWww.setText(universidad.getWww());

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Actualizar", null)
                .setNegativeButton("Cancelar", (d, which) -> d.dismiss())
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
            String www = etWww.getText() != null ? etWww.getText().toString().trim() : "";

            if (TextUtils.isEmpty(nombre)) {
                tilNombre.setError("El nombre es obligatorio");
                return;
            }

            tilNombre.setError(null);

            Universidad uniEditada = new Universidad(universidad.getId(), nombre, www);
            int filas = dao.editarUniversidad(uniEditada);

            if (filas > 0) {
                Toast.makeText(this, "Universidad actualizada", Toast.LENGTH_SHORT).show();
                cargarUniversidades();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Error al actualizar la universidad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // CALLBACK DEL ADAPTER: ELIMINAR
    @Override
    public void onEliminar(Universidad universidad) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar la " + universidad.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    int filas = dao.borrarUniversidad(String.valueOf(universidad.getId()));
                    if (filas > 0) {
                        Toast.makeText(this, "Universidad eliminada", Toast.LENGTH_SHORT).show();
                        cargarUniversidades();
                    } else {
                        Toast.makeText(this, "No se pudo eliminar la universidad", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
