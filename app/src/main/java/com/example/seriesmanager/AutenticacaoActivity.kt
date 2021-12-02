package com.example.seriesmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seriesmanager.databinding.ActivityAutenticacaoBinding

class AutenticacaoActivity : AppCompatActivity() {

    private val activityAutenticacaoBinding: ActivityAutenticacaoBinding by lazy {
        ActivityAutenticacaoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityAutenticacaoBinding.root)

        supportActionBar?.subtitle = "autenticacao"

        with(activityAutenticacaoBinding) {
            cadastrarUsuarioBt.setOnClickListener{
                startActivity(Intent(this@AutenticacaoActivity, CadastrarUsuarioActivity::class.java))
            }

            recuperarSenhaBt.setOnClickListener{
                startActivity(Intent(this@AutenticacaoActivity, RecuperarSenhaActivity::class.java))
            }

            entrarBt.setOnClickListener{
                val email = emailEt.text.toString()
                val senha = senhaEt.text.toString()
                AutenticacaoFirebase.firebaseAuth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                    Toast.makeText(this@AutenticacaoActivity, "autenticado com sucesso!" , Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AutenticacaoActivity, MainActivity::class.java))
                    finish()
                }.addOnFailureListener{
                    Toast.makeText(this@AutenticacaoActivity, "falha na autenticacao!" , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}