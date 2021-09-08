package com.metehanbolat.teknofestegitim.view.mainviews.machinelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineLearningBinding

class MachineLearningFragment : Fragment() {

    private var _binding : FragmentMachineLearningBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore : FirebaseFirestore
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var auth : FirebaseAuth

    private lateinit var userEmail : String
    private lateinit var name : String

    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineLearningBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.machine_learning_background_color)

        auth = Firebase.auth

        binding.machineLearningButton.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_machineLearningFragment_to_machineHumanNeuralFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_machineLearningFragment_to_mainFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        firestore = Firebase.firestore
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        userEmail = firebaseUser.email.toString()
        getData(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData(view: View){
        firestore.collection("UserData").whereEqualTo("userEmail",userEmail).addSnapshotListener { value, error ->
            if (error != null){
                Snackbar.make(view, resources.getString(R.string.error_occurred), Snackbar.LENGTH_SHORT).show()
            }else{
                if (value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        for (document in documents){
                            name = document.get("userName") as String
                        }
                    }
                }
            }
        }
    }
}