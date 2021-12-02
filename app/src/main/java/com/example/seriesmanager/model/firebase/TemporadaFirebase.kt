package com.example.seriesmanager.model.firebase

import android.util.Log
import com.example.seriesmanager.model.serie.Serie
import com.example.seriesmanager.model.temporada.Temporada
import com.example.seriesmanager.model.temporada.TemporadaDao
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TemporadaFirebase: TemporadaDao {

    companion object {
        private val BD_SERIE = "serie"
    }

    //referencia para o realtime database -> temporadas
    private val temporadasRtDb = Firebase.database.getReference(BD_SERIE)

    //lista de temporadas que simula consulta
    private val temporadasList: MutableList<Temporada> = mutableListOf()
    init{
        temporadasRtDb.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novaTemporada: Temporada? = snapshot.child("serie").child("temporada").value as? Temporada
                novaTemporada?.apply {
                    if(temporadasList.find { it.numeroTemporada == this.numeroTemporada} == null){
                        temporadasList.add(novaTemporada)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val temporadaAlterada: Temporada? = snapshot.child("serie").child("temporada").value as? Temporada
                temporadaAlterada?.apply {
                    temporadasList[temporadasList.indexOfFirst { it.numeroTemporada == this.numeroTemporada }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val temporadaDeletada: Temporada? = snapshot.child("serie").child("temporada").value as? Temporada
                temporadaDeletada?.apply {
                    temporadasList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //nao usa
            }

            override fun onCancelled(error: DatabaseError) {
                //nao usa
            }
        })
    }

    override fun createTemporada(temporada: Temporada): Long {
        createOrUpdate(temporada)
        return 0L
    }

    override fun listOneTemporada(temporada: Temporada): Temporada {
        return temporadasList.firstOrNull { it == temporada } ?: Temporada()
    }

    override fun litsAllTemporada(nomeSerie: String): MutableList<Temporada>{
        temporadasRtDb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                temporadasList.clear()
                snapshot.child(nomeSerie).child("temporada").children.forEach{
                    val temporada: Temporada = it.getValue<Temporada>()?: Temporada()
                    temporadasList.add(temporada)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //nao usa
            }
        })

        return temporadasList
    }

    override fun updateTemporada(temporada: Temporada): Int {
        createOrUpdate(temporada)
        Log.i("aaa", temporada.numeroTemporada.toString())
        return 1
    }

    override fun deleteTemporada(numeroTemporada: String, nomeSerie: String): Int {
        temporadasRtDb.child(nomeSerie).child("temporada").child(numeroTemporada).removeValue()
        return 1
    }

    private fun createOrUpdate (temporada: Temporada){
        temporadasRtDb.child(temporada.nomeSerie).child("temporada").child(temporada.numeroTemporada).setValue(temporada)
    }
}