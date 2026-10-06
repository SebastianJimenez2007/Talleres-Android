package com.ejemplo.conexionhttp.vistas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ejemplo.conexionhttp.R;
import com.ejemplo.conexionhttp.controladores.ConexionHttpPostServer;
import com.ejemplo.conexionhttp.controladores.NameValuePair;
import com.ejemplo.conexionhttp.datos.Usuario;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PantallaListado extends AppCompatActivity {

    private ProgressDialog barraProgreso;
    private ConexionHttpPostServer conexionServidor;
    private ListView listaUsuariosView;
    private Button btnGuardar;
    private Button btnCerrarSesion;
    private List<Usuario> listaUsuarios;
    private ArrayAdapter<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_listado);

        listaUsuariosView = findViewById(R.id.listaUsuarios);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        conexionServidor = new ConexionHttpPostServer();

        // Botón "Edita Tus Datos": Abre la pantalla en MODO EDICIÓN
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(PantallaListado.this, PantallaCrudUsuario.class);
                intento.putExtra("modo", "editar");
                if (PantallaInicio.fulanito != null) {
                    intento.putExtra("sesion", PantallaInicio.fulanito);
                }
                startActivity(intento);
            }
        });

        // Botón "Cerrar Sesión": Limpia la sesión y vuelve a la pantalla inicial
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantallaInicio.fulanito = null;
                finish(); // Regresa a PantallaInicio
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar automáticamente la lista cada vez que volvemos a esta pantalla
        items.clear();
        new TareaListarTodo().execute();
    }

    class TareaListarTodo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barraProgreso = new ProgressDialog(PantallaListado.this);
            barraProgreso.setMessage("Cargando lista de usuarios...");
            barraProgreso.setIndeterminate(false);
            barraProgreso.setCancelable(false);
            barraProgreso.show();
        }

        @Override
        protected String doInBackground(String... params) {
            boolean resultado = procesarRespuestaPeticion();
            if (resultado) {
                return "OK";
            } else {
                return "NO";
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            if (barraProgreso != null && barraProgreso.isShowing()) {
                barraProgreso.dismiss();
            }

            if ("OK".equals(resultado)) {
                mostrarUsuariosEnLista();
            } else {
                Toast.makeText(PantallaListado.this, "No se pudieron obtener los usuarios", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void mostrarUsuariosEnLista() {
        items.clear();
        if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
            int i = 1;
            for (Usuario alguien : listaUsuarios) {
                String elemento = i + ". " + alguien.getEmail() + " - " + alguien.getNombre();
                items.add(elemento);
                i++;
            }
        } else {
            items.add("No hay usuarios registrados");
        }
        listaUsuariosView.setAdapter(items);
    }

    private boolean procesarRespuestaPeticion() {
        ArrayList<NameValuePair> listaParametros = new ArrayList<>();
        listaParametros.add(new NameValuePair("accion", "listar"));

        try {
            String resultadoDelServidor = conexionServidor.conexionConElServidor(
                    listaParametros,
                    ConexionHttpPostServer.direccionDelServidor
            );

            if (resultadoDelServidor != null && !resultadoDelServidor.trim().isEmpty()) {
                System.out.println("JSON recibido: " + resultadoDelServidor);
                Gson json = new Gson();
                Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
                listaUsuarios = json.fromJson(resultadoDelServidor, tipoLista);
                return true;
            } else {
                return false;
            }
        } catch (Exception error) {
            error.printStackTrace();
            return false;
        }
    }
}
