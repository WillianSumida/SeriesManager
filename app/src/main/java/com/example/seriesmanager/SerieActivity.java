package com.example.seriesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seriesmanager.databinding.ActivitySerieBinding;
import com.example.seriesmanager.model.serie.Serie;

import java.util.ArrayList;
import java.util.Arrays;

public class SerieActivity extends AppCompatActivity {

    private ActivitySerieBinding activitySerieBinding;
    private int posicao = -1;
    private Serie serie;
    private ArrayList<String> generos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySerieBinding = ActivitySerieBinding.inflate(getLayoutInflater());
        setContentView(activitySerieBinding.getRoot());

        setTitle("Serie");

        generos = new ArrayList<>(Arrays.asList(getResources().getStringArray((R.array.generos))));
        ArrayAdapter<String> formacaoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, generos);
        activitySerieBinding.generoSp.setAdapter(formacaoAdapter);

        activitySerieBinding.salvarBt.setOnClickListener(
                (View view ) -> {
                    serie = new Serie(
                            activitySerieBinding.nomeEt.getText().toString(),
                            activitySerieBinding.anoLancamentoEt.getText().toString(),
                            activitySerieBinding.emissoraEt.getText().toString(),
                            activitySerieBinding.generoSp.getSelectedItem().toString()
                    );

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.EXTRA_SERIE, serie);

                    //se foi uma edicao retornar a posicao
                    if (posicao != -1){
                        resultIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
        );

        //verificar se e uma edicao ou consulta e preencher os campos
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        serie = getIntent().getParcelableExtra(MainActivity.EXTRA_SERIE);
        if (serie != null){
            activitySerieBinding.nomeEt.setEnabled(false);
            activitySerieBinding.nomeEt.setText(serie.getNome());
            activitySerieBinding.anoLancamentoEt.setText(serie.getAnoDeLancamento());
            activitySerieBinding.emissoraEt.setText(serie.getEmissora());
            activitySerieBinding.generoSp.setSelection(formacaoAdapter.getPosition(serie.getGenero()));


            if (posicao == -1){
                for (int i=0; i<activitySerieBinding.getRoot().getChildCount(); i++){
                    activitySerieBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activitySerieBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}
