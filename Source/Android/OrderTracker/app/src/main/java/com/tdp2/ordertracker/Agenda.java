package com.tdp2.ordertracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 06/04/16.
 */
public class Agenda {
    public String nombre;
    public String hora;
    public String direccion;
    public String id;
    public String estadoVisita;
    private List<Agenda> persons;

    Agenda(String unNombre, String unaDireccion, String unaHora,String identificador, String estadoVisita){
        this.nombre=unNombre;
        this.hora=unaHora;
        this.estadoVisita = estadoVisita;
        this.direccion=unaDireccion;
        this.id = identificador;
    }


}

