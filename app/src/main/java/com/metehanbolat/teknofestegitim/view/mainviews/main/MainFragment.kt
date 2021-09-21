package com.metehanbolat.teknofestegitim.view.mainviews.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
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
import com.metehanbolat.teknofestegitim.databinding.FragmentMainBinding
import com.metehanbolat.teknofestegitim.databinding.UserQuizRoomClickAlertDesignBinding
import com.metehanbolat.teknofestegitim.view.userviews.UserActivity

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var _alertBinding : UserQuizRoomClickAlertDesignBinding? = null
    private val alertBinding get() = _alertBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var navController: NavController

    private var userName : Any? = null
    private var userSurname : Any? = null
    private var userCoin : Any? = null

    private lateinit var list : ArrayList<String>

    private var counter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        _alertBinding = UserQuizRoomClickAlertDesignBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        firestore = Firebase.firestore

        getUserData(auth.currentUser!!.email.toString(), view)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                when(counter){
                    0 -> {
                        Snackbar.make(view,resources.getString(R.string.againClickExit),Snackbar.LENGTH_SHORT).show()
                        counter++
                    }
                    1 -> {
                        activity?.finish()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.header_status_bar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.artificialIntelligenceCardView.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_animMainEducationFragment)
        }

        binding.achievementCardView.setOnClickListener {

            getQuizRoomList()

            val builder = AlertDialog.Builder(requireContext()).create()
            builder.setView(alertBinding.root)
            alertBinding.alertUserButton.setOnClickListener {
                if (alertBinding.alertUserEditText.text.isNullOrBlank()){
                    Snackbar.make(view, resources.getString(R.string.user_enter_quiz_room_password), Snackbar.LENGTH_LONG).show()
                }else{
                    for (id in list){
                        if (id == alertBinding.alertUserEditText.text.toString()){
                            navController = findNavController()
                            val action = MainFragmentDirections.actionMainFragmentToStudentQuizFragment(id)
                            navController.navigate(action)
                            builder.dismiss()
                            break
                        }
                    }
                }
            }

            builder.setOnCancelListener {
                (alertBinding.root.parent as ViewGroup).removeView(alertBinding.root)
            }
            builder.show()
        }

        binding.machineLearningCardView.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_machineLearningFragment)
        }

        binding.computerVision.setOnClickListener {
            navController = findNavController()
            val action = MainFragmentDirections.actionMainFragmentToComputerVisionFragment(auth.currentUser?.email.toString(), userCoin.toString().toInt())
            navController.navigate(R.id.action_mainFragment_to_computerVisionFragment, action.arguments)
        }

        binding.giftCardView.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_giftFragment)
        }

        binding.giftCardView.setOnLongClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_achievementFragment)

            return@setOnLongClickListener true
        }

        binding.signOutCardView.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), UserActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun getUserData(name : String, view : View){
        val getUserData = firestore.collection(resources.getString(R.string.firebase_userData)).document(name)
        getUserData.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    userName = document.data!![resources.getString(R.string.firebase_userName)]
                    userSurname = document.data!![resources.getString(R.string.firebase_userSurname)]
                    userCoin = document.data!![resources.getString(R.string.firebase_userCoin)]
                    binding.userName.text = resources.getString(R.string.name_surname, userName.toString(), userSurname.toString())
                    binding.userCoin.text = userCoin.toString()
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.cardGridLayout.visibility = View.VISIBLE
                }else{
                    Snackbar.make(view, resources.getString(R.string.no_user_data), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getQuizRoomList(){

        firestore.collection(resources.getString(R.string.quiz_room)).get().addOnCompleteListener {
            if (it.isSuccessful){
                list = ArrayList()
                for (doc in it.result!!){
                    list.add(doc.id)
                }
                alertBinding.alertUserEditText.visibility = View.VISIBLE
                alertBinding.alertUserButton.visibility = View.VISIBLE
                alertBinding.alertUserTitle.visibility = View.VISIBLE
                alertBinding.studentProgressBar.visibility = View.INVISIBLE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _alertBinding = null
    }

}