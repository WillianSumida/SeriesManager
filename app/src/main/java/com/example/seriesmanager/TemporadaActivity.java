package com.example.seriesmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.seriesmanager.databinding.ActivitySerieBinding;
import com.example.seriesmanager.databinding.ActivityTemporadaBinding;
import com.example.seriesmanager.model.serie.Serie;
import com.example.seriesmanager.model.temporada.Temporada;

public class TemporadaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporada);
    }
}