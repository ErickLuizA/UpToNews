package com.deverick.uptonews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                startActivity(Intent(this, AuthActivity::class.java))

                finish()
            }
        }

        findViewById<TextView>(R.id.hello).text = auth.currentUser?.displayName ?: "No"

        findViewById<Button>(R.id.signout).setOnClickListener {
            auth.signOut()
        }
    }
}