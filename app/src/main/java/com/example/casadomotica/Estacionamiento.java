package com.example.casadomotica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class Estacionamiento extends AppCompatActivity implements miMenu{

    Fragment[] misFragmentos;
    private final int [] img = {R.drawable.cocina2, R.drawable.estacionamiento2, R.drawable.recamara2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacionamiento);


        Bundle extra = getIntent().getExtras();
        menuboton(extra.getInt("botonpul"));
    }

    @Override
    public void menuboton(int boton) {
        FragmentManager miManejador = getSupportFragmentManager();
        FragmentTransaction miTransicion = miManejador.beginTransaction();

        Fragment menuIluminado = new Menu();
        Bundle datos = new Bundle();
        datos.putInt("botonpulsado2", boton);
        menuIluminado.setArguments(datos);
        miTransicion.replace(R.id.menu, menuIluminado);
    }
}