package com.ejemplo.conexionhttp.vistas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ejemplo.conexionhttp.R;
import com.ejemplo.conexionhttp.controladores.ConexionHttpPostServer;
import com.ejemplo.conexionhttp.controladores.Mensaje;
import com.ejemplo.conexionhttp.controladores.NameValuePair;
import com.ejemplo.conexionhttp.datos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class PantallaCrudUsuario extends AppCompatActivity {

    private TextView txtTituloCrud;
    private EditText campoEmail;
    private EditText campoPassword;
    private EditText campoNombre;
    private Button botonGuardar;
    private Button botonCancelar;
    private ProgressDialog barraDeprogreso;

    private boolean esModoEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_crud_usuario);

        txtTituloCrud = findViewById(R.id.txtTituloCrud);
        campoEmail = findViewById(R.id.campoCodigo2);
        campoPassword = findViewById(R.id.campoPassword2);
        campoNombre = findViewById(R.id.campoNombre);
        botonGuardar = findViewById(R.id.botonModificar);
        botonCancelar = findViewById(R.id.botonCancelar2);

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Determinar si venimos a REGISTRAR o a EDITAR
        Intent intento = getIntent();
        String modo = intento != null ? intento.getStringExtra("modo") : null;

        if ("editar".equalsIgnoreCase(modo) || (intento != null && intento.hasExtra("sesion"))) {
            esModoEdicion = true;
            txtTituloCrud.setText("EDITAR MIS DATOS");
            botonGuardar.setText("Actualizar");

            Usuario usuarioActual = null;
            if (intento.hasExtra("sesion")) {
                usuarioActual = (Usuario) intento.getSerializableExtra("sesion");
            } else if (PantallaInicio.fulanito != null) {
                usuarioActual = PantallaInicio.fulanito;
            }

            if (usuarioActual != null) {
                campoNombre.setText(usuarioActual.getNombre());
                campoEmail.setText(usuarioActual.getEmail());
                campoPassword.setText(usuarioActual.getPassword());
                // El correo es la clave primaria en la base de datos, no se debe modificar
                campoEmail.setEnabled(false);
            }
        } else {
            // MODO REGISTRO
            esModoEdicion = false;
            txtTituloCrud.setText("REGISTRO DE USUARIO");
            botonGuardar.setText("Registrarse");
            campoEmail.setEnabled(true);
            campoEmail.setText("");
            campoPassword.setText("");
            campoNombre.setText("");
        }

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });
    }

    public void guardar() {
        String correo = campoEmail.getText().toString().trim();
        String clave = campoPassword.getText().toString().trim();
        String nombre = campoNombre.getText().toString().trim();

        if (correo.isEmpty() || clave.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        TareaGuardar tarea = new TareaGuardar();
        tarea.execute();
    }

    public String guardarUsuario() {
        String url = ConexionHttpPostServer.direccionDelServidor;

        String accion = esModoEdicion ? "editar" : "Agregar";

        String correo = campoEmail.getText().toString().trim();
        String clave = campoPassword.getText().toString().trim();
        String nombre = campoNombre.getText().toString().trim();

        List<NameValuePair> parametros = new ArrayList<>();
        parametros.add(new NameValuePair("accion", accion));
        parametros.add(new NameValuePair("email", correo));
        parametros.add(new NameValuePair("psw", clave));
        parametros.add(new NameValuePair("nombre", nombre));

        ConexionHttpPostServer conexionHttp = new ConexionHttpPostServer();
        try {
            String jsonRespuesta = conexionHttp.conexionConElServidor(parametros, url);
            if (jsonRespuesta != null) {
                Gson json = new Gson();
                Mensaje m = json.fromJson(jsonRespuesta, Mensaje.class);
                return m != null ? m.getMensaje() : "OK";
            } else {
                return "ERROR: Sin respuesta del servidor";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    class TareaGuardar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barraDeprogreso = new ProgressDialog(PantallaCrudUsuario.this);
            barraDeprogreso.setMessage(esModoEdicion ? "Actualizando datos..." : "Registrando usuario...");
            barraDeprogreso.setIndeterminate(false);
            barraDeprogreso.setCancelable(false);
            barraDeprogreso.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return guardarUsuario();
        }

        @Override
        protected void onPostExecute(String resultado) {
            if (barraDeprogreso != null && barraDeprogreso.isShowing()) {
                barraDeprogreso.dismiss();
            }

            if ("OK".equalsIgnoreCase(resultado)) {
                if (esModoEdicion) {
                    Toast.makeText(PantallaCrudUsuario.this, "¡Datos actualizados con éxito!", Toast.LENGTH_SHORT).show();
                    // Actualizar sesión en memoria
                    if (PantallaInicio.fulanito != null) {
                        PantallaInicio.fulanito.setNombre(campoNombre.getText().toString().trim());
                        PantallaInicio.fulanito.setPassword(campoPassword.getText().toString().trim());
                    }
                } else {
                    Toast.makeText(PantallaCrudUsuario.this, "¡Usuario registrado con éxito! Ya puedes iniciar sesión.", Toast.LENGTH_LONG).show();
                }
                finish(); // Volver a la pantalla anterior
            } else {
                Toast.makeText(PantallaCrudUsuario.this, "Error: " + resultado, Toast.LENGTH_LONG).show();
            }
        }
    }
}
