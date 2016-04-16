package com.tdp2.ordertracker;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.NumberPicker;




import org.json.JSONArray;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import Files.FileHandler;
import Model.Request;
import Model.RequestHandler;
import Model.Response;

public class ListadoProductos extends AppCompatActivity implements NumberPicker.OnValueChangeListener{
    private RecyclerView rv;
    private JSONArray productos;
    private FileHandler fileHandler;
    private List<String> firstIdImagenes;

    ProductoAdapter adapter;
    private String cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_productos);

        try {
            cliente = getIntent().getStringExtra("cliente");
        }catch(Exception e){}


        this.pedirProductos();

        rv = (RecyclerView)findViewById(R.id.recycler_view_productos);

        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new ProductoAdapter(obtenerProductos(), DetallesProducto.class, this);
        adapter.setJsonArray(productos);
        rv.setAdapter(adapter);
    }


    private void pedirProductos()
    {
        Request request = new Request("GET", "GetProductos.php");
        Response resp = new RequestHandler().sendRequest(request);

        if (resp.getStatus())
            productos = resp.getJsonArray();
        else
            productos = new JSONArray();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listado_productos, menu);
        return true;
    }


    public void downloadImagenesProductos()
    {
        fileHandler = new FileHandler();
        firstIdImagenes = new ArrayList<>();

        for (int i=0;i<productos.length();i++) {

            try {
                String idProducto = productos.getJSONObject(i).getString("id");
                String idFirstImagen = fileHandler.downloadFile("/mnt/sdcard/Download/", idProducto);

                firstIdImagenes.add(idFirstImagen);

            }catch(Exception e){}

        }
    }


    public List<RecyclerViewItem> obtenerProductos() {

        downloadImagenesProductos();
        List<RecyclerViewItem> items = new ArrayList<>();
        try {
            for (int i = 0; i < productos.length(); i++) {
                int idProducto = productos.getJSONObject(i).getInt("id");
                int idImagen;
                try {
                    if (firstIdImagenes.get(i)=="") idImagen=0;
                    else idImagen = Integer.parseInt(firstIdImagenes.get(i));
                }catch(Exception e)
                {
                    idImagen = 0;
                }

                items.add(new RecyclerViewItem(productos.getJSONObject(i).getString("nombre"),"$ " + productos.getJSONObject(i).getString("precio"),idImagen));
            }
        }
        catch(Exception e)
        {
            return null;
        }

        return items;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.finalizarPedido:
                finalizarPedido();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void finalizarPedido() {

        Hashtable<Integer, JSONObject> pedidos = adapter.getPedidos();
        JSONArray productos = new JSONArray();

        Enumeration<Integer> enumKey = pedidos.keys();

        while(enumKey.hasMoreElements()) {
            Integer key = enumKey.nextElement();
            JSONObject producto = pedidos.get(key);
            productos.put(producto);
        }



        Context contexto = this.getApplicationContext();
        Intent documentsActivity = new Intent(this, DetallesPedido.class);
        try {
            documentsActivity.putExtra("jsonArray", productos.toString());
            documentsActivity.putExtra("cliente", cliente);
        }
        catch(Exception e)
        {   return;
        }

        startActivity(documentsActivity);
    }


}
