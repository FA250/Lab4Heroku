package com.example.lab4heroku;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class fLibros extends Fragment {

    private View rootView;

    public fLibros() {
    }

    private void Actualizar_Datos() {
        try {
            Conexion conexion = new Conexion();

            String result = conexion.execute("https://hidden-thicket-64919.herokuapp.com/books.json", "GET").get();
            JSONArray librosJson = new JSONArray(result);

            conexion = new Conexion();
            String result1 = conexion.execute("https://hidden-thicket-64919.herokuapp.com/authors.json", "GET").get();
            JSONArray autoresJson = new JSONArray(result1);

            Actualizar_Lista(librosJson, autoresJson);
        } catch (InterruptedException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Actualizar_Lista(JSONArray librosJson,JSONArray autoresJson) {
        try {
            if (librosJson != null) {
                if (librosJson.length() > 0) {

                    final List<String> nombres = new ArrayList<>();
                    final List<String> autores = new ArrayList<>();

                    JSONObject elemento, elemento2;
                    for (int i = 0; i < librosJson.length(); i++) {
                        elemento = librosJson.getJSONObject(i);

                        nombres.add(elemento.getString("title"));

                        boolean existeAutor=false;
                        for(int j = 0;j<autoresJson.length();j++){
                            elemento2=autoresJson.getJSONObject(j);
                            if(elemento2.getString("id").equals(elemento.getString("author_id"))){
                                existeAutor=true;
                                autores.add(elemento2.getString("name")+" "+elemento2.getString("lastname"));
                                j=autoresJson.length();
                            }
                        }

                        if(!existeAutor)
                            autores.add("");
                    }



                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, nombres) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                            TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                            text1.setText(nombres.get(position).toString());
                            text2.setText(autores.get(position).toString());
                            return view;
                        }
                    };


                    ListView listaLibros=rootView.findViewById(R.id.listaLibros);

                    listaLibros.setAdapter(adapter);

                } else {
                    Toast.makeText(getContext(), "Ocurrio un error al mostrar los libros", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocurrio un error al mostrar los libros", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_libros, container, false);
        Actualizar_Datos();
        return rootView;
    }
}