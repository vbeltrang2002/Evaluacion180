package com.example.apiropa;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apiropa.WebServices.Asynchtask;
import com.example.apiropa.WebServices.WebService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Asynchtask {
    private static final String TAG = "MainActivity";
    private static final String BASE_URL =
            "https://apiws.uteq.edu.ec/h6RPoSoRaah0Y4Bah28eew/functions/information/entity/3";
    private static final String TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJfeDF1c2VyZGV2IiwiaWF0IjoxNzUyODY4NjIwLCJleHAiOjE3NTI5NTUwMjB9.tZPdC3XwEOhgHZz_QFk5TQa2SrFXimlgVSFa6cwhiwY";
    private static final String BASE_IMAGE_URL =
            "https://uteq.edu.ec/assets/images/videos/res-sem/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Llamada al WebService
        Map<String, String> datos = new HashMap<>();
        WebService ws = new WebService(BASE_URL, datos, this, this);
        ws.execute("GET", "Bearer ", TOKEN);
    }

    @Override
    public void processFinish(String result) {
        if (result == null || result.trim().isEmpty() || result.startsWith("ERROR:")) {
            Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONArray arr = new JSONArray(result);
            if (arr.length() == 0) {
                Toast.makeText(this, "No hay videos disponibles", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir a objetos Video y configurar URLs de imágenes
            ArrayList<Video> lstVideos = Video.JSonObjectstoBuild(arr);

            for (Video v : lstVideos) {
                if (v.getPortadavideo() != null && !v.getPortadavideo().isEmpty()) {
                    String urlCompleta = BASE_IMAGE_URL + v.getPortadavideo();
                    Log.d(TAG, "URL imagen: " + urlCompleta);
                    v.setPortadavideo(urlCompleta);
                }
            }

            // Configurar ListView
            ListView lista = findViewById(R.id.lista_videos);
            if (lista != null) {
                AdaptadorVideo adapter = new AdaptadorVideo(this, lstVideos);
                lista.setAdapter(adapter);
                Toast.makeText(this, lstVideos.size() + " videos cargados", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parseando JSON", e);
            Toast.makeText(this, "Error procesando datos", Toast.LENGTH_SHORT).show();
        }
    }
}