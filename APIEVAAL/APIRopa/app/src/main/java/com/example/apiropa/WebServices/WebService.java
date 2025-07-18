package com.example.apiropa.WebServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.util.Map;

public class WebService extends AsyncTask<String, Long, String> {
    private static final String TAG = "WebService";

    //Variable con los datos para pasar al web service
    private Map<String, String> datos;
    //Url del servicio web
    private String url;

    //Actividad para mostrar el cuadro de progreso
    private Context actividad;

    //Resultado
    private String xml = null;

    //Clase a la cual se le retorna los datos del ws
    private Asynchtask callback = null;

    ProgressDialog progDailog;

    /**
     * Crea una instancia de la clase webService para hacer consultas a ws
     * @param urlWebService Url del servicio web
     * @param data Datos a enviar del servicios web
     * @param activity Actividad de donde se llama el servicio web, para mostrar el cuadro de "Cargando"
     * @param callback Clase a la que se le retornará los datos del servicio web
     */
    public WebService(String urlWebService, Map<String, String> data, Context activity, Asynchtask callback) {
        this.url = urlWebService;
        this.datos = data;
        this.actividad = activity;
        this.callback = callback;
    }

    public WebService() {
        // Constructor vacío
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            progDailog = new ProgressDialog(actividad);
            progDailog.setMessage("Conectando con API...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        } catch (Exception e) {
            Log.e(TAG, "Error mostrando ProgressDialog: " + e.getMessage());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpsURLConnection conn = null;
        BufferedReader reader = null;
        InputStream inputStream = null;

        try {
            Log.d(TAG, "Iniciando conexión a: " + this.url);
            Log.d(TAG, "Método HTTP: " + (params.length > 0 ? params[0] : "GET"));
            Log.d(TAG, "Parámetros recibidos: " + params.length);

            // Configurar SSL para aceptar todos los certificados (solo para desarrollo)
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            // Crear conexión
            URL url = new URL(this.url);
            conn = (HttpsURLConnection) url.openConnection();

            // Configurar timeouts
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);

            // Configurar método HTTP
            String method = (params.length > 0) ? params[0] : "GET";
            conn.setRequestMethod(method);

            // Configurar headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Android-App/1.0");

            // Agregar Authorization header si se proporciona
            if (params.length >= 3) {
                String authHeader = params[1] + params[2];
                conn.setRequestProperty("Authorization", authHeader);
                Log.d(TAG, "Authorization header configurado: " + authHeader);
            }

            conn.setDoInput(true);
            conn.setUseCaches(false);

            // Conectar
            Log.d(TAG, "Estableciendo conexión...");
            conn.connect();

            // Obtener código de respuesta
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            Log.d(TAG, "Código de respuesta: " + responseCode);
            Log.d(TAG, "Mensaje de respuesta: " + responseMessage);

            // Leer respuesta
            if (responseCode >= 200 && responseCode < 300) {
                inputStream = conn.getInputStream();
                Log.d(TAG, "Leyendo respuesta exitosa...");
            } else {
                inputStream = conn.getErrorStream();
                Log.e(TAG, "Error en respuesta. Código: " + responseCode + ", Mensaje: " + responseMessage);
            }

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                result = sb.toString();
                Log.d(TAG, "Respuesta recibida (" + result.length() + " caracteres): " +
                        result.substring(0, Math.min(200, result.length())));
            } else {
                result = "ERROR: No se pudo leer la respuesta";
                Log.e(TAG, result);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error en doInBackground", e);
            result = "ERROR: " + e.getMessage();
        } finally {
            // Limpiar recursos
            try {
                if (reader != null) reader.close();
                if (inputStream != null) inputStream.close();
                if (conn != null) conn.disconnect();
            } catch (IOException e) {
                Log.e(TAG, "Error cerrando conexión", e);
            }
        }

        Log.d(TAG, "Resultado final: " + result);
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        try {
            if (progDailog != null && progDailog.isShowing()) {
                progDailog.dismiss();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cerrando ProgressDialog", e);
        }

        this.xml = response;

        // Retornar los datos al callback
        try {
            if (callback != null) {
                callback.processFinish(this.xml);
            } else {
                Log.e(TAG, "Callback es nulo");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error en callback", e);
        }
    }

    // Getters y Setters
    public Asynchtask getCallback() {
        return callback;
    }

    public void setCallback(Asynchtask callback) {
        this.callback = callback;
    }

    public Map<String, String> getDatos() {
        return datos;
    }

    public void setDatos(Map<String, String> datos) {
        this.datos = datos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Context getActividad() {
        return actividad;
    }

    public void setActividad(Context actividad) {
        this.actividad = actividad;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public ProgressDialog getProgDailog() {
        return progDailog;
    }

    public void setProgDailog(ProgressDialog progDailog) {
        this.progDailog = progDailog;
    }
}