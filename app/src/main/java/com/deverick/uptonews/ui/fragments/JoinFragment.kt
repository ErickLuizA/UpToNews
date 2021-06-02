package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.deverick.uptonews.databinding.FragmentJoinBinding
import com.google.firebase.auth.FirebaseAuth

class JoinFragment : Fragment() {
    private lateinit var binding: FragmentJoinBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.signupGoogleBtn.setOnClickListener {

        }

        binding.signupFacebookBtn.setOnClickListener {

        }

        binding.signupEmailBtn.setOnClickListener {
            findNavController().navigate(JoinFragmentDirections.actionJoinFragmentToSignupFragment())
        }

        binding.signinText.setOnClickListener {
            findNavController().navigate(JoinFragmentDirections.actionJoinFragmentToWelcomeFragment())
        }
    }
}