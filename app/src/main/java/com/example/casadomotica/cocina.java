package com.example.casadomotica;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cocina#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cocina extends Fragment {
    private boolean isFragmentLoaded = false;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private Handler handler;
    private final int SEARCH_INTERVAL = 3000;
    TextView texto;
    String t;

    MediaPlayer M1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public cocina() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cocina.
     */
    // TODO: Rename and change types and number of parameters
    public static cocina newInstance(String param1, String param2) {
        cocina fragment = new cocina();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        cocina fragment = new cocina();
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
        View view = inflater.inflate(R.layout.fragment_cocina, container, false);
        texto=(TextView)view.findViewById(R.id.textohumo);
        M1 = MediaPlayer.create(getContext(),R.raw.fuego);
        Buscar("https://acerate-sizes.000webhostapp.com/casa/Casita/buscarHumo.php");
        return view;
    }

    private void Buscar(String url){
        JsonArrayRequest stringArray=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        t = (jsonObject.getString("Dato"));
                        if (t.equals("1"))
                        {
                            texto.setText("Presencia de humo");
                        }
                        else {
                            texto.setText("No hay presencia de humo");
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getContext(), "Error Temp:"+error.toString(), Toast.LENGTH_SHORT).show();
                texto.setText(error.toString());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(requireContext());
        requestQueue.add(stringArray);
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentLoaded = true;
        ejecutarBuscarSiEsNecesario();
        iniciarBusquedaPeriodica();
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentLoaded = false;
        cancelarBuscarSiEsNecesario();
        detenerBusquedaPeriodica();
        M1.start();
        M1.pause();
    }

    private void iniciarBusquedaPeriodica() {
        if (isFragmentLoaded) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ejecutarBuscarSiEsNecesario();
                    handler.postDelayed(this, SEARCH_INTERVAL);
                }
            }, SEARCH_INTERVAL);
        }
    }

    private void detenerBusquedaPeriodica() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void cancelarBuscarSiEsNecesario() {
        if (jsonArrayRequest != null) {
            jsonArrayRequest.cancel();
        }
    }

    private void ejecutarBuscarSiEsNecesario() {
        Toast.makeText(getContext(), "Actualizando...", Toast.LENGTH_SHORT).show();
        if (isFragmentLoaded) {
            // Inicializar el RequestQueue si aún no está inicializado
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(requireContext());
            }
            String url = "https://acerate-sizes.000webhostapp.com/casa/Casita/buscarHumo.php";
            jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET, // Método de solicitud (GET en este caso)
                    url, // URL de la solicitud
                    null, // Datos en el cuerpo de la solicitud (en este caso, no hay)
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // ... Procesar la respuesta JSON ...
                            JSONObject jsonObject = null;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    t = (jsonObject.getString("Dato"));
                                    if (t.equals("1"))
                                    {
                                        texto.setText("Presencia de humo");
                                        M1.start();
                                    }
                                    else {
                                        texto.setText("No hay presencia de humo");
                                        M1.start();
                                        M1.pause();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // ... Manejar el error ...
                        }
                    }
            );
            requestQueue.add(jsonArrayRequest);
        }
    }

}