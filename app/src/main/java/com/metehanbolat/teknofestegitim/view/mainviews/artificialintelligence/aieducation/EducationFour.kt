package com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.aieducation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentEducationFourBinding

class EducationFour : Fragment() {

    private var _binding : FragmentEducationFourBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    private var userCoin : Any? = null
    private var learningThree : Any? = null

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEducationFourBinding.inflate(inflater,container,false)
        val view = binding.root

        auth = Firebase.auth
        firestore = Firebase.firestore

        getDataFun(auth.currentUser!!.email.toString(), view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore

        binding.nameText.text = resources.getString(R.string.autonomous_cars)
        binding.infoText.text = resources.getString(R.string.education_four_ai_info_one)
        binding.modelImage.setImageResource(R.drawable.model_car)
        binding.realImage.setImageResource(R.drawable.real_car)
        binding.informationText.text = resources.getString(R.string.education_four_ai_info_two)
        binding.gif.visibility = View.VISIBLE

        binding.educationFourEndTextGift.setOnClickListener {

            if (learningThree!! == resources.getString(R.string.true_ai)) {
                Snackbar.make(it, resources.getString(R.string.awards_already_collected_ai), Snackbar.LENGTH_LONG).show()
            }else{
                val getData = firestore.collection(resources.getString(R.string.firebase_userData)).document(auth.currentUser!!.email.toString())
                val userCoinInt : Int = userCoin.toString().toInt()
                val newCoin : Int = userCoinInt + 10
                getData.update(resources.getString(R.string.ai_learning_three),resources.getString(R.string.true_ai),resources.getString(R.string.firebase_userCoin),newCoin)
            }

            navController = findNavController()
            navController.navigate(R.id.action_educationFour_to_mainEducation)
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_educationFour_to_mainEducation)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun getDataFun(mail : String, view: View){

        val getUserData = firestore.collection(resources.getString(R.string.firebase_userData)).document(mail)
        getUserData.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    learningThree = document.data!![resources.getString(R.string.firebase_aiLearningThree)]
                    userCoin = document.data!![resources.getString(R.string.firebase_userCoin)]
                }else{
                    Snackbar.make(view, resources.getString(R.string.no_user_data), Snackbar.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener {
            Snackbar.make(view, it.toString(), Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

