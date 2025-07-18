package com.example.apiropa;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class AdaptadorVideo extends ArrayAdapter<Video> {
    private static final String TAG = "AdaptadorVideo";

    public AdaptadorVideo(Context context, ArrayList<Video> videos) {
        super(context, R.layout.item, videos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;

        // Reutilizar la vista si existe (mejor performance)
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.item, parent, false);
        }

        // Obtener el video actual
        Video video = getItem(position);
        if (video == null) {
            Log.e(TAG, "Video es null en posición: " + position);
            return item;
        }

        // Obtener los elementos de la vista y asignarles los valores
        TextView lblNombre = item.findViewById(R.id.idTitulo);
        if (lblNombre != null) {
            lblNombre.setText(video.getTitulo());
        }

        TextView lblDescripcion = item.findViewById(R.id.idFechapub);
        if (lblDescripcion != null) {
            lblDescripcion.setText(video.getFechapub());
        }

        TextView lblCategoria = item.findViewById(R.id.idUrlvideo1);
        if (lblCategoria != null) {
            lblCategoria.setText(video.getUrlvideo1());
        }

        // Cargar imagen con Glide
        ImageView imageView = item.findViewById(R.id.idportadavideo);
        if (imageView != null) {
            String urlImagen = video.getPortadavideo();

            Log.d(TAG, "=== CARGANDO IMAGEN ===");
            Log.d(TAG, "Posición: " + position);
            Log.d(TAG, "URL de imagen: " + urlImagen);

            if (urlImagen != null && !urlImagen.trim().isEmpty()) {
                Glide.with(getContext())
                        .load(urlImagen.trim()) // Eliminar espacios en blanco
                        .placeholder(android.R.drawable.ic_menu_gallery) // Imagen mientras carga
                        .error(android.R.drawable.ic_dialog_alert) // Imagen si hay error
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cachear la imagen
                        .centerCrop() // Ajustar la imagen
                        .into(imageView);

                Log.d(TAG, "Glide iniciado para: " + urlImagen);
            } else {
                Log.w(TAG, "URL de imagen vacía o null para posición: " + position);
                // Establecer imagen por defecto si no hay URL
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            Log.e(TAG, "ImageView idportadavideo no encontrado en el layout");
        }

        return item;
    }
}