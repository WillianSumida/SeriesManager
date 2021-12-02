package com.example.seriesmanager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

object AutenticacaoFirebase {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
}