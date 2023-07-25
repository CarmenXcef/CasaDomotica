package com.example.casadomotica;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu extends Fragment {

    private final int [] botones = {R.id.btnCocina, R.id.btnEstacionamiento, R.id.btnRecamara};
    private ImageButton cocina, estacionamiento, recamara;
    private final int [] botonesIluminados = {R.drawable.cocina2, R.drawable.estacionamiento2, R.drawable.recamara2};
    private int boton = -1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Menu.
     */
    // TODO: Rename and change types and number of parameters
    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mimenu = inflater.inflate(R.layout.fragment_menu, container, false);

        if(getArguments()!=null){
            boton = getArguments().getInt("botonpulsado2");
        }

        ImageButton botonMenu = null;
        for (int i=0; i<botones.length; i++){

            botonMenu = (ImageButton) mimenu.findViewById(botones[i]);

            if(boton==i){
                botonMenu.setImageResource(botonesIluminados[i]);
            }
            final int queBoton = i;

            botonMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity miActividad = getActivity();
                    ((miMenu)miActividad).menuboton(queBoton);
                }
            });
        }
        return mimenu;

    }
}