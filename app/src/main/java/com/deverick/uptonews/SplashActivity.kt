package com.deverick.uptonews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        auth.currentUser.let { user ->
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
            }

            finish()
        }
    }
}