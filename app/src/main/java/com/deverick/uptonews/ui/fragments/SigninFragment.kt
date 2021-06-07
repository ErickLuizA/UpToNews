package com.deverick.uptonews.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deverick.uptonews.MainActivity
import com.deverick.uptonews.R
import com.deverick.uptonews.databinding.FragmentSigninBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SigninFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.continueButton.setOnClickListener {
            loginWithEmail()
        }
    }

    private fun loginWithEmail() {
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPassword.text.toString()

        val isValid = validate(email, password)

        if (!isValid) {
            return
        }

        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(
                        activity,
                        MainActivity::class.java
                    )

                    startActivity(intent)

                    activity?.finish()
                } else {
                    Snackbar.make(
                        binding.mainLayout,
                        "User does not exists",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            Snackbar.make(
                binding.mainLayout,
                "Sign in failed",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun validate(email: String, password: String): Boolean {
        var isValid = true
        binding.textInputEmailLayout.error = null
        binding.textInputPasswordLayout.error = null

        val validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        if (!validEmail) {
            isValid = false

            binding.textInputEmailLayout.error = resources.getString(R.string.invalid_email)
        }

        val validPassword = password.length >= 6

        if (!validPassword) {
            isValid = false

            binding.textInputPasswordLayout.error = resources.getString(R.string.invalid_password)
        }

        return isValid
    }
}