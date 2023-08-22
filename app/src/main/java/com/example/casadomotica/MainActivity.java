package com.example.casadomotica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{
    private BottomNavigationView btnMenu;
    private Fragment fragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initValues();
        initListener();
    }
    private void initView()
    {
        btnMenu=findViewById(R.id.btnMenu);
    }
    private void initValues()
    {
        manager=getSupportFragmentManager();
        loadFirstfragment();
    }
    private void initListener()
    {
        btnMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idMenu=item.getItemId();
                switch (idMenu)
                {
                    case R.id.menu_recamara:
                        getSupportActionBar().setTitle(R.string.recamara_title);
                        fragment=recamara.newInstance();
                        openFragment(fragment);
                        return true;
                    case R.id.menu_cocina:
                        getSupportActionBar().setTitle(R.string.cocina_title);
                        fragment=cocina.newInstance();
                        openFragment(fragment);
                        return true;
                    case R.id.menu_estacionamiento:
                        getSupportActionBar().setTitle(R.string.estacionamiento_title);
                        fragment=estacionamiento.newInstance();
                        openFragment(fragment);
                        return true;
                    case R.id.menu_tinaco:
                        getSupportActionBar().setTitle(R.string.tinaco_title);
                        fragment=tinaco.newInstance();
                        openFragment(fragment);
                        return true;
                }
                return false;
            }
        });
    }
    private void openFragment(Fragment fragment)
    {
        manager.beginTransaction().replace(R.id.FrameContainer,fragment).commit();
    }
    private void loadFirstfragment()
    {
        getSupportActionBar().setTitle(R.string.cocina_title);
        fragment=cocina.newInstance();
        openFragment(fragment);
    }


}