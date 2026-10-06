package com.ejemplo.conexionhttp.controladores;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Controlador de conexión HTTP POST hacia el servidor PHP.
 * Utiliza HttpURLConnection nativo de Android, garantizando compatibilidad total
 * sin depender de librerías Apache obsoletas.
 */
public class ConexionHttpPostServer {

    // Dirección IP por defecto para el emulador de Android Studio hacia localhost
    public static String direccionDelServidor = "http://10.0.2.2/crudphpjson/crud/operacion.php";

    private String respuesta;
    private InputStream datosEntrada;

    /**
     * Realiza una petición POST al servidor enviando una lista de parámetros NameValuePair.
     *
     * @param parametros Lista de parámetros clave-valor a enviar.
     * @param rutaDeLaAplicacionWeb URL del endpoint PHP.
     * @return Cadena de texto con la respuesta en formato JSON.
     * @throws Exception En caso de error de red o timeout.
     */
    public String conexionConElServidor(List<NameValuePair> parametros, String rutaDeLaAplicacionWeb) throws Exception {
        HttpURLConnection conexion = null;
        OutputStream os = null;

        try {
            URL url = new URL(rutaDeLaAplicacionWeb);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setDoInput(true);
            conexion.setConnectTimeout(10000); // 10 segundos timeout
            conexion.setReadTimeout(10000);
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // Construir el cuerpo de la petición: clave1=valor1&clave2=valor2
            StringBuilder postData = new StringBuilder();
            for (int i = 0; i < parametros.size(); i++) {
                NameValuePair parametro = parametros.get(i);
                if (i > 0) {
                    postData.append("&");
                }
                postData.append(URLEncoder.encode(parametro.getName(), "UTF-8"));
                postData.append("=");
                postData.append(URLEncoder.encode(parametro.getValue(), "UTF-8"));
            }

            // Enviar los datos
            os = conexion.getOutputStream();
            byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();

            int codigoRespuesta = conexion.getResponseCode();

            if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
                datosEntrada = conexion.getInputStream();
                respuesta = procesarRespuestaDelServidor();
                return respuesta;
            } else {
                throw new Exception("ERROR HTTP " + codigoRespuesta + ": " + conexion.getResponseMessage());
            }

        } catch (Exception e) {
            throw new Exception("ERROR 3: Conexion fallida:\n" + e.getMessage(), e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ignored) {
                }
            }
            if (conexion != null) {
                conexion.disconnect();
            }
        }
    }

    /**
     * Procesa el InputStream y lo convierte a String con la respuesta JSON completa.
     */
    private String procesarRespuestaDelServidor() throws Exception {
        if (datosEntrada == null) {
            return null;
        }

        BufferedReader lectorDatos = null;
        try {
            lectorDatos = new BufferedReader(new InputStreamReader(datosEntrada, StandardCharsets.UTF_8));
            StringBuilder jsonBuilder = new StringBuilder();
            String linea;

            while ((linea = lectorDatos.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    jsonBuilder.append(linea);
                }
            }

            return jsonBuilder.toString();
        } catch (Exception error) {
            throw new Exception("ERROR 5: Sin respuesta legible\n" + error.getMessage(), error);
        } finally {
            if (lectorDatos != null) {
                try {
                    lectorDatos.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
