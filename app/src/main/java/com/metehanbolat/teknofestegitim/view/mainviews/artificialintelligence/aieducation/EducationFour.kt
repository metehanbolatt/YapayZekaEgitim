package com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.aieducation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var userName : Any? = null
    private var userSurname : Any? = null
    private var userEmail : Any? = null
    private var userNick : Any? = null
    private var userEducationLevel : Any? = null
    private var userBirthday : Any? = null
    private var userCoin : Any? = null
    private var learningOne : Any? = null
    private var learningTwo : Any? = null
    private var learningThree : Any? = null
    private var newUserDataMap = hashMapOf<String, Any>()

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

        binding.nameText.text = resources.getString(R.string.otonom_cars)
        binding.infoText.text = resources.getString(R.string.education_four_ai_info_one)
        binding.modelImage.setImageResource(R.drawable.modelcar)
        binding.realImage.setImageResource(R.drawable.realcar)
        binding.informationText.text = resources.getString(R.string.education_four_ai_info_two)
        binding.gif.visibility = View.VISIBLE

        binding.educationFourEndTextGift.setOnClickListener {

            if (learningThree!! == resources.getString(R.string.true_ai)) {
                Snackbar.make(it, resources.getString(R.string.awards_already_collected_ai), Snackbar.LENGTH_LONG).show()
            }else{
                val getData = firestore.collection(resources.getString(R.string.firebase_userData)).document(auth.currentUser!!.email.toString())
                newUserDataMap.replace(resources.getString(R.string.ai_learning_three), resources.getString(R.string.false_ai), resources.getString(R.string.true_ai))
                val userCoinInt : Int = userCoin.toString().toInt()
                val newCoin : Int = userCoinInt + 10
                newUserDataMap[resources.getString(R.string.firebase_userCoin)] = newCoin
                getData.set(newUserDataMap)
            }

            navController = findNavController()
            navController.navigate(R.id.action_educationFour_to_mainEducation)
        }
    }

    private fun getDataFun(mail : String, view: View){

        val getUserData = firestore.collection(resources.getString(R.string.firebase_userData)).document(mail)
        getUserData.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    userName = document.data!![resources.getString(R.string.firebase_userName)]
                    userSurname = document.data!![resources.getString(R.string.firebase_userSurname)]
                    userEmail = document.data!![resources.getString(R.string.firebase_userEmail)]
                    userNick = document.data!![resources.getString(R.string.firebase_userNick)]
                    userEducationLevel = document.data!![resources.getString(R.string.firebase_educationLevel)]
                    userBirthday = document.data!![resources.getString(R.string.firebase_userBirthday)]
                    learningOne = document.data!![resources.getString(R.string.firebase_aiLearningOne)]
                    learningTwo = document.data!![resources.getString(R.string.firebase_aiLearningTwo)]
                    learningThree = document.data!![resources.getString(R.string.firebase_aiLearningThree)]
                    userCoin = document.data!![resources.getString(R.string.firebase_userCoin)]

                    newUserDataMap = hashMapOf()
                    newUserDataMap[resources.getString(R.string.firebase_userCoin)]= userCoin as Any
                    newUserDataMap[resources.getString(R.string.firebase_userName)] = userName as Any
                    newUserDataMap[resources.getString(R.string.firebase_userSurname)] = userSurname as Any
                    newUserDataMap[resources.getString(R.string.firebase_userNick)] = userNick as Any
                    newUserDataMap[resources.getString(R.string.firebase_userEmail)] = userEmail as Any
                    newUserDataMap[resources.getString(R.string.firebase_userBirthday)] = userBirthday as Any
                    newUserDataMap[resources.getString(R.string.firebase_educationLevel)] = userEducationLevel as Any
                    newUserDataMap[resources.getString(R.string.firebase_aiLearningOne)] = learningOne as Any
                    newUserDataMap[resources.getString(R.string.firebase_aiLearningTwo)] = learningTwo as Any
                    newUserDataMap[resources.getString(R.string.firebase_aiLearningThree)] = learningThree as Any

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

