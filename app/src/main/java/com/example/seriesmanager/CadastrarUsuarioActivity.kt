package com.example.seriesmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seriesmanager.databinding.ActivityCadastrarUsuarioBinding

class CadastrarUsuarioActivity : AppCompatActivity() {

    private val activityCadastrarUsuarioBinding : ActivityCadastrarUsuarioBinding by lazy {
        ActivityCadastrarUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityCadastrarUsuarioBinding.root)

        supportActionBar?.subtitle = "cadastrar usuario"

        with(activityCadastrarUsuarioBinding) {
            cadastrarUsuarioBt.setOnClickListener {
                val email = emailEt.text.toString()
                val senha = senhaEt.text.toString()
                val repetirSenha = repetirSenhaEt.text.toString()

                if(senha == repetirSenha) {
                    //cadastra
                    AutenticacaoFirebase.firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnSuccessListener {
                        //cadastro deu certo
                        Toast.makeText(this@CadastrarUsuarioActivity, "cadastrado com sucesso!" , Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener{
                        //cadastro deu errado
                        Toast.makeText(this@CadastrarUsuarioActivity, "cadastro falhou!" , Toast.LENGTH_SHORT).show()
                    }
                }else{
                    //nao cadastra
                    Toast.makeText(this@CadastrarUsuarioActivity, "senhas nao estao iguais!" , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}