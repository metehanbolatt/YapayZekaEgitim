package com.metehanbolat.teknofestegitim.view.userviews

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentUserSignInBinding
import com.metehanbolat.teknofestegitim.view.mainviews.MainActivity

class UserSignInFragment : Fragment() {

    private var _binding : FragmentUserSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController : NavController

    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userButtonSignIn.setOnClickListener{
            val email = binding.userNameSignIn.text.toString()
            val password = binding.userPasswordSignIn.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Snackbar.make(it, "Email veya şifre boş bırakılmış.", Snackbar.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.logoLinear.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_userSignInFragment_to_userSignUpFragment)
        }

        binding.googleLinear.setOnClickListener {
            // Google ile giriş yapılacak.
        }

        binding.facebookLinear.setOnClickListener {
            // Facebook ile giriş yapılacak.
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}