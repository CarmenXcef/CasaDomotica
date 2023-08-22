package com.example.casadomotica;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link recamara#newInstance} factory method to
 * create an instance of this fragment.
 */
public class recamara extends Fragment {
    private boolean isFragmentLoaded = false;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private Handler handler;

    String t="";
    ImageButton btnFoco;

    private boolean isImage1 = true;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public recamara() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recamara.
     */
    // TODO: Rename and change types and number of parameters
    public static recamara newInstance(String param1, String param2) {
        recamara fragment = new recamara();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance() {
        Bundle args = new Bundle();

        recamara fragment = new recamara();
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
        View rootView = inflater.inflate(R.layout.fragment_recamara, container, false);

        btnFoco = (ImageButton) rootView.findViewById(R.id.btnfoco);

        Buscar("https://acerate-sizes.000webhostapp.com/casa/Casita/leerFoco.php");

        // Agrega un OnClickListener al ImageButton
        btnFoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar la imagen del ImageButton al hacer clic en él
                if (isImage1) {
                    btnFoco.setBackgroundResource(R.drawable.encendido);
                    Actualizar1("https://acerate-sizes.000webhostapp.com/casa/Casita/actualizarFoco.php");
                } else {
                    btnFoco.setBackgroundResource(R.drawable.apagado);
                    Actualizar2("https://acerate-sizes.000webhostapp.com/casa/Casita/actualizarFoco.php");
                }
                // Cambiar el estado de la variable para la próxima vez que se presione el botón
                isImage1 = !isImage1;
            }
        });

        return rootView;
    }

    private void Actualizar1(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Encendiendo...", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Estado", "1");

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private void Actualizar2(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Apagando...", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Estado", "0");

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private void Buscar(String url){
        JsonArrayRequest stringArray=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        t = (jsonObject.getString("Estado"));
                        if (t.equals("1"))
                        {
                            btnFoco.setBackgroundResource(R.drawable.encendido);
                            isImage1 = true;
                        }
                        else {
                            btnFoco.setBackgroundResource(R.drawable.apagado);
                            isImage1 = false;
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error buscar 1:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getContext(), "Error Temp:"+error.toString(), Toast.LENGTH_SHORT).show();
                //texto.setText(error.toString());
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
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentLoaded = false;
        cancelarBuscarSiEsNecesario();
    }

    private void cancelarBuscarSiEsNecesario() {
        if (jsonArrayRequest != null) {
            jsonArrayRequest.cancel();
        }
    }

    private void ejecutarBuscarSiEsNecesario() {
        Toast.makeText(getContext(), "Buscar...", Toast.LENGTH_SHORT).show();
        if (isFragmentLoaded) {
            // Inicializar el RequestQueue si aún no está inicializado
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(requireContext());
            }
            String url = "https://acerate-sizes.000webhostapp.com/casa/Casita/leerFoco.php";
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
                                    t = (jsonObject.getString("Estado"));
                                    if (t.equals("1"))
                                    {
                                        btnFoco.setBackgroundResource(R.drawable.encendido);
                                        isImage1 = true;
                                    }
                                    else {
                                        btnFoco.setBackgroundResource(R.drawable.apagado);
                                        isImage1 = false;
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Error buscar2:"+e.getMessage(), Toast.LENGTH_SHORT).show();

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