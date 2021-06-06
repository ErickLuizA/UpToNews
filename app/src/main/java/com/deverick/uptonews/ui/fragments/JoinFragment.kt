package com.deverick.uptonews.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.deverick.uptonews.BuildConfig
import com.deverick.uptonews.MainActivity
import com.deverick.uptonews.databinding.FragmentJoinBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

const val REQUEST_CODE_SIGN_IN = 0

class JoinFragment : Fragment() {
    private lateinit var binding: FragmentJoinBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.signupGoogleBtn.setOnClickListener {
            loginWithGoogle()
        }

        binding.signupFacebookBtn.setOnClickListener {
            loginWithFacebook()
        }

        binding.signupEmailBtn.setOnClickListener {
            findNavController().navigate(JoinFragmentDirections.actionJoinFragmentToSignupFragment())
        }

        binding.signinText.setOnClickListener {
            findNavController().navigate(JoinFragmentDirections.actionJoinFragmentToWelcomeFragment())
        }
    }

    private fun loginWithGoogle() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), options)

        googleSignInClient.signInIntent.also {
            startActivityForResult(it, REQUEST_CODE_SIGN_IN)
        }
    }

    private fun googleAuth(token: String) {
        val credentials = GoogleAuthProvider.getCredential(token, null)

        try {
            auth.signInWithCredential(credentials).addOnCompleteListener { task ->
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
                        "Google sign in failed",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            Snackbar.make(
                binding.mainLayout,
                "Google sign in failed",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email, public_profile"))

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult?) {
                    if (loginResult != null) {
                        facebookAuth(loginResult.accessToken)
                    }
                }

                override fun onCancel() {
                    Snackbar.make(
                        binding.mainLayout,
                        "Facebook sign in failed",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onError(exception: FacebookException?) {
                    Snackbar.make(
                        binding.mainLayout,
                        "Facebook sign in failed",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun facebookAuth(token: AccessToken) {
        val credentials = FacebookAuthProvider.getCredential(token.token)

        try {
            auth.signInWithCredential(credentials).addOnCompleteListener { task ->
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
                        "Facebook sign in failed",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            Snackbar.make(
                binding.mainLayout,
                "Facebook sign in failed",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    account.idToken?.let { googleAuth(it) }
                }
            } catch (e: ApiException) {
                Snackbar.make(
                    binding.mainLayout,
                    "Google sign in failed",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
}