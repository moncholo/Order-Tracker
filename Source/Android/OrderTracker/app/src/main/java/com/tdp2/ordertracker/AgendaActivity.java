package com.tdp2.ordertracker;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import GoogleMaps.GMPolyline;
import Model.Request;
import Model.RequestHandler;
import Model.Response;

/**
 * Created by juan on 06/04/16.
 */


public class AgendaActivity extends AppCompatActivity  {

    RecyclerView rv;
    String vendedor;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerList;
    private GoogleMap mMap;
    private String[] mPlanetTitles;

    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;

    private String fechaActual;
    private GMPolyline map;

    ArrayList<Agenda> usuarios;
    ArrayList<TextView> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agenda);

        this.vendedor = ManejadorPersistencia.obtenerVendedor(this);
        this.items = new ArrayList<TextView>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        fechaActual = dateFormat.format(date).toString();
        usuarios = getUsuariosAgenda();

        rv = (RecyclerView)findViewById(R.id.recycler_view_agenda);
        rv.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        AgendaAdapter adapter = new AgendaAdapter(usuarios);
        rv.setAdapter(adapter);


        mPlanetTitles = getResources().getStringArray(R.array.dias_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RelativeLayout) findViewById(R.id.left_drawer);


        cargarItems();
        mTitle = mDrawerTitle = getTitle();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                ((TextView)findViewById(R.id.lunes)).setTypeface(null, Typeface.BOLD);
            case Calendar.TUESDAY:
                ((TextView)findViewById(R.id.martes)).setTypeface(null, Typeface.BOLD);
            case Calendar.WEDNESDAY:
                ((TextView)findViewById(R.id.miercoles)).setTypeface(null, Typeface.BOLD);
            case Calendar.THURSDAY:
                ((TextView)findViewById(R.id.jueves)).setTypeface(null, Typeface.BOLD);
            case Calendar.FRIDAY:
                ((TextView)findViewById(R.id.viernes)).setTypeface(null, Typeface.BOLD);

        }


        this.crearDraweToggle();
    }

    private void cargarItems(){
        TextView unDia = (TextView)findViewById(R.id.itemDia);
        unDia = new TextView(this);

        unDia.setText("Lunes");
        items.add(unDia);
    }

    private void crearDraweToggle()
    {
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.monitor,
                R.string.action_settings,
                R.string.action_settings
        ) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
                mDrawerList.bringToFront();
                mDrawerLayout.requestLayout();
            }};

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        //mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public ArrayList<Agenda> getUsuariosAgenda(){
        ArrayList<Agenda> listaUsuarios= new ArrayList<Agenda>();
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        try {
            Request request = new Request("GET", "GetClientes.php?id="+vendedor+"&fecha="+fechaActual);
            Response resp = new RequestHandler().sendRequest(request);

            for (int i=0;i<resp.getJsonArray().length();i++){
                JSONObject agenda = resp.getJsonArray().getJSONObject(i);
                Date date = format.parse(agenda.getString("fecha"));
                String hora = new SimpleDateFormat("HH:mm").format(date);

                Agenda unaAgenda = new Agenda(agenda.getString("nombre"),agenda.getString("direccion"), hora,agenda.getString("id"));
                listaUsuarios.add(unaAgenda);
            }
        }
        catch(Exception e){}

        return listaUsuarios;
    }



    public void verDia(View view){
        int id = view.getId();
        String diaSeleccionado = "";
        selectItem(id);

        switch (id) {

            //Fuera de ruta
            case R.id.fueraDeRuta:

                Intent documentsActivity = new Intent(view.getContext(), ListadoClientes.class);
                startActivity(documentsActivity);
                break;
            case R.id.lunes:

                diaSeleccionado = "Lunes";
                break;
            case R.id.martes:

                diaSeleccionado = "Martes";
                break;
            case R.id.miercoles:

                diaSeleccionado = "Miercoles";
                break;
            case R.id.jueves:
                diaSeleccionado = "Jueves";
                break;
            case R.id.viernes:
                diaSeleccionado = "Viernes";
                break;
        }

        fechaActual = diaSeleccionado;

        usuarios = getUsuariosAgenda();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = new GMPolyline(usuarios,mapFragment, this);
        AgendaAdapter adapter = new AgendaAdapter(usuarios);
        rv.setAdapter(adapter);

    }

}