package com.example.casadomotica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements miMenu{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void menuboton(int boton) {
        Intent in = new Intent(MainActivity.this, Estacionamiento.class);
        in.putExtra("botonpul", boton);

        //startActivities(new Intent[]{in});
        startActivity(in);
    }
}