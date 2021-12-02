package com.example.seriesmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.seriesmanager.databinding.ActivitySerieBinding;
import com.example.seriesmanager.databinding.ActivityTemporadaBinding;
import com.example.seriesmanager.model.serie.Serie;
import com.example.seriesmanager.model.temporada.Temporada;

public class TemporadaActivity extends AppCompatActivity {

    private ActivityTemporadaBinding activityTemporadaBinding;
    private int posicao = -1;
    private Serie serie;
    private Temporada temporada;
    private int numeroTemporada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTemporadaBinding = ActivityTemporadaBinding.inflate(getLayoutInflater());
        setContentView(activityTemporadaBinding.getRoot());

        numeroTemporada = (getIntent().getIntExtra(MainTemporadaActivity.EXTRA_POSICAO, 0) + 1);

        setTitle("Temporada");

        serie = getIntent().getParcelableExtra(MainTemporadaActivity.EXTRA_SERIE);

        if (serie != null){
            activityTemporadaBinding.nomeSerieEt.setText(serie.getNome());
            activityTemporadaBinding.nomeSerieEt.setEnabled(false);

            activityTemporadaBinding.numeroTemporadaEt.setText(String.valueOf(numeroTemporada));
            activityTemporadaBinding.numeroTemporadaEt.setEnabled(false);
        }



        activityTemporadaBinding.salvarTemporadaBt.setOnClickListener(
                (View view ) -> {
                    temporada = new Temporada(
                            String.valueOf(numeroTemporada),
                            activityTemporadaBinding.anoLancamentoEt.getText().toString(),
                            activityTemporadaBinding.qtdeEpisodesEt.getText().toString(),
                            activityTemporadaBinding.nomeSerieEt.getText().toString()
                    );

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainTemporadaActivity.EXTRA_TEMPORADA, temporada);

                    //se foi uma edicao retornar a posicao
                    if (posicao != -1){
                        resultIntent.putExtra(MainTemporadaActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
        );

        //verificar se e uma edicao ou consulta e preencher os campos
        posicao = getIntent().getIntExtra(MainTemporadaActivity.EXTRA_POSICAO, -1);
        temporada = getIntent().getParcelableExtra(MainTemporadaActivity.EXTRA_TEMPORADA);
        if (temporada != null){
            activityTemporadaBinding.numeroTemporadaEt.setEnabled(false);
            activityTemporadaBinding.numeroTemporadaEt.setText(temporada.getNumeroTemporada());
            activityTemporadaBinding.anoLancamentoEt.setText(temporada.getAnoDeLancamento());
            activityTemporadaBinding.qtdeEpisodesEt.setText(temporada.getQtdEpisodes());
            activityTemporadaBinding.nomeSerieEt.setEnabled(false);
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