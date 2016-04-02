package com.tdp2.ordertracker;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import Model.Request;
import Model.RequestHandler;
import Model.Response;

public class DetallesProducto extends AppCompatActivity {

    private JSONObject producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_productos);

        try {
            producto = new JSONObject(getIntent().getStringExtra("jsonArray"));
        }catch(Exception e){}


        this.setAdaptador(this.getListaDetalles());

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_imagenes);

        ArrayList<String> imagenes = getListaImagenes();

        for (int i = 0; i < imagenes.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            //imageView.setImageBitmap(BitmapFactory.decodeResource(
              //      getResources(), R.drawable.alice));
            try {
                File f = new File("/mnt/sdcard/Download/", imagenes.get(i) + ".jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(b);
            }
            catch(Exception e){}



            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
        }


    }

    public ArrayList<String> getListaImagenes(){
        ArrayList<String> listaImagenes = new ArrayList<String>();
        try {
            Request request = new Request("GET", "GetListaImagenes.php?id_producto=" + producto.getString("id"));
            Response resp = new RequestHandler().sendRequest(request);
            for (int i=0;i<resp.getJsonArray().length();i++)
                listaImagenes.add(resp.getJsonArray().getJSONObject(i).getString("id"));
        }
        catch(Exception e){}

        return listaImagenes;
    }

    public ArrayList<String> getListaDetalles()
    {
        ArrayList<String> datos = new ArrayList<String>();

        try {
            datos.add("Precio: " + producto.getString("precio"));
            datos.add("Codigo: " + producto.getString("codigo"));
            datos.add("Caracteristicas: " + producto.getString("caracteristicas"));
            datos.add("Stock: " + producto.getString("stock"));
            datos.add("Marca: " + producto.getString("marca"));
            datos.add("Estado: " + producto.getString("estado"));
            datos.add("Categoria: " + producto.getString("categoria"));
        }
        catch(Exception e){}

        return datos;
    }

    public void setAdaptador(ArrayList<String> datos)
    {
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
        ListView listado = (ListView) findViewById(R.id.list);
        listado.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalles_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
