package com.metehanbolat.teknofestegitim.view.mainviews

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMainBinding
import com.metehanbolat.teknofestegitim.view.userviews.UserActivity

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var navController: NavController

    private var userName : Any? = null
    private var userSurname : Any? = null
    private var userEmail : Any? = null
    private var userNick : Any? = null
    private var userEducationLevel : Any? = null
    private var userBirthday : Any? = null
    private var userCoin : Any? = null
    private var firstCoinComputerVision : Any? = null
    private var secondCoinComputerVision : Any? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.header_status_bar)

        auth = Firebase.auth
        firestore = Firebase.firestore

        getUserData(auth.currentUser!!.email.toString())

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.artificialIntelligenceCardView.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_artificialIntelligenceFragment)
        }

        binding.machineLearningCardView.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_machineLearningFragment)
        }

        binding.achievementCardView.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_achievementFragment)
        }

        binding.computerVision.setOnClickListener {
            navController = findNavController()
            val action = MainFragmentDirections.actionMainFragmentToComputerVisionFragment(auth.currentUser?.email.toString(),userCoin.toString().toInt())
            navController.navigate(R.id.action_mainFragment_to_computerVisionFragment,action.arguments)

        }

        binding.giftCardView.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_giftFragment)
        }

        binding.signOutCardView.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), UserActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData(name : String){
        val getUserData = firestore.collection("UserData").document(name)
        getUserData.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    userName = document.data!!["userName"]
                    userSurname = document.data!!["userSurname"]
                    userEmail = document.data!!["userEmail"]
                    userNick = document.data!!["userNick"]
                    userEducationLevel = document.data!!["userEducationLevel"]
                    userBirthday = document.data!!["userBirthday"]
                    userCoin = document.data!!["userCoin"]

                    binding.userName.text = "$userName $userSurname"
                    binding.userCoin.text = userCoin.toString()

                }else{
                    Toast.makeText(requireContext(),"Kullanıcı verisi bulunamadı",Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}