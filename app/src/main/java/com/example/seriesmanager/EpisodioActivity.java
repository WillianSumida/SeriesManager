package com.example.seriesmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.seriesmanager.databinding.ActivityEpisodioBinding;
import com.example.seriesmanager.databinding.ActivityTemporadaBinding;
import com.example.seriesmanager.model.episodio.Episodio;
import com.example.seriesmanager.model.serie.Serie;
import com.example.seriesmanager.model.temporada.Temporada;

public class EpisodioActivity extends AppCompatActivity {
    private ActivityEpisodioBinding activityEpisodioBinding;
    private int posicao = -1;
    private Serie serie;
    private Temporada temporada;
    private Episodio episodio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEpisodioBinding = ActivityEpisodioBinding.inflate(getLayoutInflater());
        setContentView(activityEpisodioBinding.getRoot());

        setTitle("Episodio");

        serie = getIntent().getParcelableExtra(MainEpisodioActivity.EXTRA_SERIE);
        temporada = getIntent().getParcelableExtra(MainEpisodioActivity.EXTRA_TEMPORADA);

        if (serie != null && temporada!= null){
            activityEpisodioBinding.nomeSerieEt.setText(serie.getNome());
            activityEpisodioBinding.nomeSerieEt.setEnabled(false);

            activityEpisodioBinding.temporadaEt.setText(temporada.getNumeroTemporada());
            activityEpisodioBinding.temporadaEt.setEnabled(false);
        }

        activityEpisodioBinding.salvarEpisodioBt.setOnClickListener(

                (View view ) -> {
                    episodio = new Episodio(
                            activityEpisodioBinding.numeroEpisodioEt.getText().toString(),
                            activityEpisodioBinding.nomeEpisodioEt.getText().toString(),
                            activityEpisodioBinding.duracaoEt.getText().toString(),
                            activityEpisodioBinding.assistidoCb.isChecked() ? "true" : "false",
                            activityEpisodioBinding.nomeSerieEt.getText().toString(),
                            activityEpisodioBinding.temporadaEt.getText().toString()
                    );

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainEpisodioActivity.EXTRA_EPISODIO, episodio);

                    //se foi uma edicao retornar a posicao
                    if (posicao != -1){
                        resultIntent.putExtra(MainEpisodioActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
        );

        //verificar se e uma edicao ou consulta e preencher os campos
        posicao = getIntent().getIntExtra(MainEpisodioActivity.EXTRA_POSICAO, -1);
        episodio = getIntent().getParcelableExtra(MainEpisodioActivity.EXTRA_EPISODIO);
        if (episodio != null){
            Log.i("tag", episodio.getAssistido());
            activityEpisodioBinding.numeroEpisodioEt.setEnabled(false);
            activityEpisodioBinding.numeroEpisodioEt.setText(episodio.getNumeroEpisodio());
            activityEpisodioBinding.nomeEpisodioEt.setText(episodio.getNomeEpisodio());
            activityEpisodioBinding.duracaoEt.setText(episodio.getDuracao());
            activityEpisodioBinding.nomeSerieEt.setEnabled(false);
            activityEpisodioBinding.nomeSerieEt.setText(episodio.getNomeSerie());
            activityEpisodioBinding.temporadaEt.setEnabled(false);
            activityEpisodioBinding.temporadaEt.setText(episodio.getTemporadaNumero());

            Log.i("tag", episodio.toString());
            if(episodio.getAssistido().equals("true")) activityEpisodioBinding.assistidoCb.setChecked(true);

            if (posicao == -1){
                for (int i=0; i<activityEpisodioBinding.getRoot().getChildCount(); i++){
                    activityEpisodioBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityEpisodioBinding.salvarEpisodioBt.setVisibility(View.GONE);
            }
        }
    }
}