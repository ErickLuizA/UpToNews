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
import com.deverick.uptonews.databinding.FragmentSignupBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.continueButton.setOnClickListener {
            loginWithEmail()
        }
    }

    private fun loginWithEmail() {
        val name = binding.textInputName.text.toString()
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPassword.text.toString()

        val isValid = validate(name, email, password)

        if (!isValid) {
            return
        }

        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    auth.currentUser?.updateProfile(profileUpdates)

                    val intent = Intent(
                        activity,
                        MainActivity::class.java
                    )

                    startActivity(intent)

                    activity?.finish()
                } else {
                    Snackbar.make(
                        binding.mainLayout,
                        "User already exists",
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

    private fun validate(name: String, email: String, password: String): Boolean {
        var isValid = true
        binding.textInputNameLayout.error = null
        binding.textInputEmailLayout.error = null
        binding.textInputPasswordLayout.error = null

        val validName = name.isNotEmpty()

        if (!validName) {
            isValid = false

            binding.textInputNameLayout.error = resources.getString(R.string.invalid_name)
        }

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