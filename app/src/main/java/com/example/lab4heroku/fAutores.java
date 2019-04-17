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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class fAutores extends Fragment {
    private View rootView;


    public fAutores() {
        // Required empty public constructor
    }


    private void Actualizar_Datos() {
        try {
            Conexion conexion = new Conexion();

            String result1 = conexion.execute("https://hidden-thicket-64919.herokuapp.com/authors.json", "GET").get();
            JSONArray autoresJson = new JSONArray(result1);

            Actualizar_Lista(autoresJson);
        } catch (InterruptedException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Actualizar_Lista(JSONArray autoresJson) {
        try {
            if (autoresJson != null) {
                if (autoresJson.length() > 0) {

                    final List<String> autores = new ArrayList<>();

                    JSONObject elemento, elemento2;
                    for (int i = 0; i < autoresJson.length(); i++) {
                        elemento = autoresJson.getJSONObject(i);

                        autores.add(elemento.getString("name")+" "+elemento.getString("lastname"));
                    }



                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, autores);


                    ListView listaLibros=rootView.findViewById(R.id.listaAutores);

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
        rootView= inflater.inflate(R.layout.fragment_autores, container, false);
        Actualizar_Datos();
        return rootView;
    }
}
