package com.metehanbolat.teknofestegitim.view.mainviews.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentCoinDenemeBinding
import com.metehanbolat.teknofestegitim.utils.UserCoin

class CoinDenemeFragment : Fragment() {

    private var _binding : FragmentCoinDenemeBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCoinDenemeBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = Firebase.auth
        firebaseFirestore = Firebase.firestore

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_coinDenemeFragment_to_mainFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonArttir.setOnClickListener {
            val userCoin = UserCoin(firebaseFirestore,"UserData",firebaseAuth.currentUser!!.email.toString())
            userCoin.getCoin("userCoin"){
                val newCoin = userCoin.userCoinIncrease("userCoin",it!!,10)
                binding.coinText.text = newCoin.toString()
            }

        }

        binding.buttonAzalt.setOnClickListener {
            val userCoin = UserCoin(firebaseFirestore,"UserData",firebaseAuth.currentUser!!.email.toString())
            userCoin.getCoin("userCoin"){
                val newCoin = userCoin.userCoinDecrease("userCoin",it!!,10)
                binding.coinText.text = newCoin.toString()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}