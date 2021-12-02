package com.example.seriesmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seriesmanager.databinding.ActivityRecuperarSenhaBinding

class RecuperarSenhaActivity : AppCompatActivity() {
    private val activityRecuperarSenhaBinding : ActivityRecuperarSenhaBinding by lazy{
        ActivityRecuperarSenhaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityRecuperarSenhaBinding.root)

        supportActionBar?.subtitle = "recuperar senha"

        with(activityRecuperarSenhaBinding){
            enviarEmailBt.setOnClickListener {
                val email = emailEt.text.toString()

                if(email.isNotEmpty()){
                    AutenticacaoFirebase.firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener{ envio ->
                        if(envio.isSuccessful) {
                            Toast.makeText(this@RecuperarSenhaActivity, "email de redefinicao enviado com sucesso!" , Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this@RecuperarSenhaActivity, "email de redefinicao nao enviado!" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}