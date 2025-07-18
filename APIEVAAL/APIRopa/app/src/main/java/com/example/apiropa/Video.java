package com.example.apiropa;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Video {
    private static final String TAG = "Video";

    private String titulo;
    private String fechapub;
    private String urlvideo1;
    private String portadavideo;

    public Video(JSONObject a) throws JSONException {
        // Log para debug
        Log.d(TAG, "JSON recibido: " + a.toString());


        this.titulo = a.has("titulo") ? a.getString("titulo") : "Sin título";
        this.fechapub = a.has("fechapub") ? a.getString("fechapub") : "Sin fecha";
        this.urlvideo1 = a.has("urlvideo1") ? a.getString("urlvideo1") : "";
        this.portadavideo = a.has("portadavideo") ? a.getString("portadavideo") : "";

        Log.d(TAG, "Video creado: " + this.titulo);
    }

    // Getter y setter para portadavideo
    public String getPortadavideo() {
        return portadavideo;
    }

    public void setPortadavideo(String portadavideo) {
        this.portadavideo = portadavideo;
    }

    // Getters para los demás campos
    public String getTitulo() {
        return titulo;
    }

    public String getFechapub() {
        return fechapub;
    }

    public String getUrlvideo1() {
        return urlvideo1;
    }

    public static ArrayList<Video> JSonObjectstoBuild(JSONArray datos) throws JSONException {
        Log.d(TAG, "Procesando " + datos.length() + " videos");

        ArrayList<Video> videos = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            try {
                videos.add(new Video(datos.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Error en video " + i + ": " + e.getMessage());
                // Continuar con el siguiente video
            }
        }

        Log.d(TAG, "Videos procesados: " + videos.size());
        return videos;
    }
}