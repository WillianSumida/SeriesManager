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
    private ActivityTemporadaBinding activityTemporadaBinding;
    private int posicao = -1;
    private Temporada temporada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTemporadaBinding = ActivityTemporadaBinding.inflate(getLayoutInflater());
        setContentView(activityTemporadaBinding.getRoot());

        activityTemporadaBinding.salvarTemporadaBt.setOnClickListener(
                (View view ) -> {
                    temporada = new Temporada(
                            Integer.parseInt(activityTemporadaBinding.numeroTemporadaEt.getText().toString()),
                            activityTemporadaBinding.anoLancamentoTemporadaEt.getText().toString(),
                            Integer.parseInt(activityTemporadaBinding.qtdeEpisodiosEt.getText().toString()),
                            activityTemporadaBinding.nomeSerieEt.getText().toString()
                    );

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(SerieActivity.EXTRA_TEMPORADA, temporada);

                    //se foi uma edicao retornar a posicao
                    if (posicao != -1){
                        resultIntent.putExtra(SerieActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
        );

        //verificar se e uma edicao ou consulta e preencher os campos
        posicao = getIntent().getIntExtra(TemporadaActivity.EXTRA_POSICAO, -1);
        temporada = getIntent().getParcelableExtra(TemporadaActivity.EXTRA_TEMPORADA);
        if (temporada != null){
            activityTemporadaBinding.numeroTemporadaEt.setEnabled(false);
            activityTemporadaBinding.anoLancamentoTemporadaEt.setText(temporada.getAnoDeLancamento());
            activityTemporadaBinding.qtdeEpisodiosEt.setText(temporada.getQtdeEpisodios());
            activityTemporadaBinding.nomeSerieEt.setText(temporada.getNomeSerie());

            if (posicao == -1){
                for (int i=0; i<activityTemporadaBinding.getRoot().getChildCount(); i++){
                    activityTemporadaBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityTemporadaBinding.salvarTemporadaBt.setVisibility(View.GONE);
            }
        }
    }
}