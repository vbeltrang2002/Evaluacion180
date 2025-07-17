package com.example.apiropa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Producto {
        private String nombre;
        private String descripcion;
        private double precio;
        private String imagenUrl;
        private String categoria;
        public Producto(JSONObject a) throws JSONException{
            nombre = a.getString("title");
            descripcion = a.getString("description");
            precio = a.getDouble("price");
            imagenUrl = a.getString("image");
            categoria = a.getString("category");
        }
        public static  ArrayList<Producto> JSonObjectstoBuild(JSONArray a) throws JSONException{
            ArrayList<Producto> productos = new ArrayList<>();
            for (int i = 0; i < a.length() && i<20; i++) {
                productos.add(new Producto(a.getJSONObject(i)));
            }
            return productos;
        }
        public String getNombre() {
            return nombre;
        }
        public String getDescripcion() {
            return descripcion;
        }
        public double getPrecio() {
            return precio;
            }
        public String getImagenUrl() {
            return imagenUrl;
        }
        public String getCategoria() {
            return categoria;
        }
}
