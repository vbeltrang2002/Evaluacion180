package com.example.apiropa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdaptadorProducto extends ArrayAdapter<Producto> {

    public AdaptadorProducto(Context context, ArrayList<Producto> productos) {
        super(context, R.layout.item, productos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.item, null);

        // Obtener los elementos de la vista y asignarles los valores
        TextView lblNombre = item.findViewById(R.id.idNombre);
        lblNombre.setText(getItem(position).getNombre());

        TextView lblDescripcion = item.findViewById(R.id.idDescription);
        lblDescripcion.setText(getItem(position).getDescripcion());

        TextView lblCategoria = item.findViewById(R.id.idCategoria);
        lblCategoria.setText(getItem(position).getCategoria());

        ImageView imageView = item.findViewById(R.id.imgProducto);
        Glide.with(this.getContext())
                .load(getItem(position).getImagenUrl())
                .into(imageView);

        return item;
    }
}
