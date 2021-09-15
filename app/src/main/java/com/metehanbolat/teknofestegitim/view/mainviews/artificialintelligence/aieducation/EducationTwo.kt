package com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.aieducation

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentEducationTwoBinding

class EducationTwo : Fragment() {

    private var _binding : FragmentEducationTwoBinding?= null
    private val binding get() = _binding!!
    private var control : Int = 0
    private lateinit var navController: NavController
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private var userCoin : Any? = null
    private var learningTwo : Any? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEducationTwoBinding.inflate(inflater,container,false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.fragment_education_background)

        auth = Firebase.auth
        firestore = Firebase.firestore

        getDataFun(auth.currentUser!!.email.toString(), view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_educationTwo_to_mainEducation)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.educationTwoButton.setOnClickListener {
            when(control){
                0 -> {
                    binding.educationTwoTitle.text = resources.getString(R.string.why)
                    binding.educationTwoInfoOne.text = resources.getString(R.string.education_two_info)
                    binding.educationTwoImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.why))
                    control = 1
                }
                1 -> {
                    binding.educationTwoTitle.text = resources.getString(R.string.where)
                    binding.educationTwoInfoOne.text = resources.getString(R.string.that_where_work_ai)
                    binding.educationTwoImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.where_sign))
                    control = 2
                }
                2 -> {
                    binding.educationTwoTitle.text = resources.getString(R.string.when_str)
                    binding.educationTwoInfoOne.text = resources.getString(R.string.that_when_work_ai)
                    binding.educationTwoImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.when_watch))
                    control = 3
                }
                3 -> {
                    binding.educationTwoTitle.text = resources.getString(R.string.who)
                    binding.educationTwoInfoOne.text = resources.getString(R.string.that_who_use_ai)
                    binding.educationTwoImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.who))
                    control = 4
                }
                4 -> {
                    binding.educationTwoTitle.text = resources.getString(R.string.how)
                    binding.educationTwoInfoOne.text = resources.getString(R.string.that_work_ai)
                    binding.educationTwoImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.how))
                    control = 5
                }
                5 -> {
                    if (learningTwo!! == resources.getString(R.string.true_ai)) {
                        Snackbar.make(it, resources.getString(R.string.awards_already_collected_ai), Snackbar.LENGTH_LONG).show()
                    }else{
                        val getData = firestore.collection(resources.getString(R.string.firebase_userData)).document(auth.currentUser!!.email.toString())
                        val userCoinInt: Int = userCoin.toString().toInt()
                        val newCoin: Int = userCoinInt + 5
                        getData.update(resources.getString(R.string.ai_learning_two),resources.getString(R.string.true_ai),resources.getString(R.string.firebase_userCoin), newCoin)
                        Snackbar.make(it, resources.getString(R.string.earn_five_gold), Snackbar.LENGTH_LONG).show()
                    }
                    navController = findNavController()
                    navController.navigate(R.id.action_educationTwo_to_mainEducation)
                }
            }
        }
    }

    private fun getDataFun(mail:String, view: View){
        val getUserData = firestore.collection(resources.getString(R.string.firebase_userData)).document(mail)
        getUserData.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    learningTwo = document.data!![resources.getString(R.string.firebase_aiLearningTwo)]
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