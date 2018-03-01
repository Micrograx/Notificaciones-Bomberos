package com.breakapp.notificaciones_bomberos;

import android.app.Application;

/**
 * Created by valentinocerutti on 25/2/18.
 */

public class Globals extends Application {
    private String localidad = "NULL";

    public String getData(){
        return this.localidad;
    }

    public void setData(String d){
        this.localidad=d;
    }
}