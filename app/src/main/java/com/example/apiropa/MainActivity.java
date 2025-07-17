package com.example.apiropa;

import android.os.Bundle;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apiropa.WebServices.Asynchtask;
import com.example.apiropa.WebServices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Asynchtask {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Map<String, String> datos = new HashMap<>();
        WebService ws = new WebService("https://fakestoreapi.com/products", datos, MainActivity.this, MainActivity.this);

        ws.execute("GET");
    }

    @Override
    public void processFinish(String result) throws JSONException {
        ListView lstListaProducto = findViewById(R.id.select_dialog_listview);

        // Parsear la respuesta JSON
        JSONObject JSONlista = new JSONObject(result);
        JSONArray JSONlistaProductos = JSONlista.getJSONArray("data");

        // Convertir JSON a objetos Producto
        ArrayList<Producto> lstProductos = Producto.JsonObjectsBuild(JSONlistaProductos);

        // Crear el adaptador y asignarlo a la ListView
        AdaptadorProducto adaptadorProducto = new AdaptadorProducto(this, lstProductos);
        lstListaProducto.setAdapter(adaptadorProducto);
    }
}
