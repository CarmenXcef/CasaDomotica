package com.example.casadomotica;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Use the {@link tinaco#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tinaco extends Fragment {
    private boolean isFragmentLoaded = false;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest1;
    private JsonArrayRequest jsonArrayRequest2;
    private Handler handler;
    private final int SEARCH_INTERVAL = 3000;
    TextView temperatura, nivel;
    ImageView imgNivel, imgTemp;
    private boolean isSearchExecuted = false;

    String t="", n="";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public tinaco() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tinaco.
     */
    // TODO: Rename and change types and number of parameters
    public static tinaco newInstance(String param1, String param2) {
        tinaco fragment = new tinaco();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance() {
        Bundle args = new Bundle();

        tinaco fragment = new tinaco();
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
        View view = inflater.inflate(R.layout.fragment_tinaco, container, false);

        temperatura = (TextView) view.findViewById(R.id.Temperatura);
        nivel = (TextView) view.findViewById(R.id.Nivel);

        imgNivel = view.findViewById(R.id.imgNivel);
        imgTemp = view.findViewById(R.id.imgTemp);



        if (!isSearchExecuted) {
            // Solo ejecuta la búsqueda si no se ha ejecutado antes
            BuscarT("https://acerate-sizes.000webhostapp.com/casa/Casita/leerTemp.php");
            BuscarN("https://acerate-sizes.000webhostapp.com/casa/Casita/leerNivel.php");
            isSearchExecuted = true;
        }

        return view;
    }

    private void BuscarT(String url){
        JsonArrayRequest stringArray=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        t = (jsonObject.getString("Temperatura"));
                        img();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error Temp:"+error.toString(), Toast.LENGTH_SHORT).show();
                //etisbn.setText(error.toString());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(requireContext());
        requestQueue.add(stringArray);
    }

    private void BuscarN(String url){
        JsonArrayRequest stringArray=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        n = (jsonObject.getString("Nivel"));
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error Niv:"+error.toString(), Toast.LENGTH_SHORT).show();
                nivel.setText(error.toString());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(requireContext());
        requestQueue.add(stringArray);
    }

    void img(){
        temperatura.setText(t+" °C");
        if (Integer.parseInt(t) <= 10) {
            imgTemp.setImageResource(R.drawable.termometro2);
        }else{
            imgTemp.setImageResource(R.drawable.termometro1);
        }

        if (n.equals("0")) {
            imgNivel.setImageResource(R.drawable.tinaco2);
            nivel.setText("Vacío/Bajo");
        }else{
            imgNivel.setImageResource(R.drawable.tinaco1);
            nivel.setText("Lleno");
        }

        // Toast.makeText(getContext(), "t: "+t, Toast.LENGTH_SHORT).show();
        // Toast.makeText(getContext(), "n: "+n, Toast.LENGTH_SHORT).show();
    }

    public void onResume() {
        super.onResume();
        isFragmentLoaded = true;
        ejecutarBuscarSiEsNecesario();
        iniciarBusquedasPeriodicas();
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentLoaded = false;
        cancelarBuscarSiEsNecesario();
        detenerBusquedasPeriodicas();
    }

    private void iniciarBusquedasPeriodicas() {
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

    private void detenerBusquedasPeriodicas() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void ejecutarBuscarSiEsNecesario() {
        Toast.makeText(getContext(), "Actualizando...", Toast.LENGTH_SHORT).show();
        if (isFragmentLoaded) {
            // Inicializar el RequestQueue si aún no está inicializado
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(requireContext());
            }

            String url1 = "https://acerate-sizes.000webhostapp.com/casa/Casita/leerTemp.php";
            jsonArrayRequest1 = new JsonArrayRequest(
                    Request.Method.GET, url1, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // ... Procesar la respuesta de la primera búsqueda ...
                            JSONObject jsonObject = null;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    t = (jsonObject.getString("Temperatura"));
                                    img();
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // ... Manejar el error de la primera búsqueda ...
                        }
                    }
            );
            requestQueue.add(jsonArrayRequest1);

            String url2 = "https://acerate-sizes.000webhostapp.com/casa/Casita/leerNivel.php";
            jsonArrayRequest2 = new JsonArrayRequest(
                    Request.Method.GET, url2, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // ... Procesar la respuesta de la segunda búsqueda ...
                            JSONObject jsonObject = null;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    n = (jsonObject.getString("Nivel"));
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // ... Manejar el error de la segunda búsqueda ...
                        }
                    }
            );
            requestQueue.add(jsonArrayRequest2);
        }
    }

    private void cancelarBuscarSiEsNecesario() {
        if (jsonArrayRequest1 != null) {
            jsonArrayRequest1.cancel();
        }
        if (jsonArrayRequest2 != null) {
            jsonArrayRequest2.cancel();
        }
    }
}