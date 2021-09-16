package com.metehanbolat.teknofestegitim.view.teacherviews.main

import android.content.Intent
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
import com.metehanbolat.teknofestegitim.databinding.FragmentTeacherMainBinding
import com.metehanbolat.teknofestegitim.view.userviews.UserActivity
import java.util.*

class TeacherMainFragment : Fragment() {

    private var _binding : FragmentTeacherMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    private var teacherName : Any? = null
    private var teacherSurname : Any? = null

    private var counter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTeacherMainBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        firestore = Firebase.firestore

        getTeacherData(auth.currentUser!!.email.toString(), view)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                when(counter){
                    0 -> {
                        Snackbar.make(view,resources.getString(R.string.againClickExit), Snackbar.LENGTH_SHORT).show()
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

        if (Locale.getDefault().language.toString() == resources.getString(R.string.tr)){
            binding.teacherBanner.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.teacher_banner_tr))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.quizCardView.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_teacherMainFragment_to_teacherQuizFragment)
        }

        binding.signOutCardView.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), UserActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTeacherData(name : String, view : View){
        val getUserData = firestore.collection(resources.getString(R.string.firebase_teacherData)).document(name)
        getUserData.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    teacherName = document.data!![resources.getString(R.string.firebase_teacherName)]
                    teacherSurname = document.data!![resources.getString(R.string.firebase_teacherSurname)]

                    binding.teacherName.text = resources.getString(R.string.name_surname, teacherName.toString(), teacherSurname.toString())
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.cardGridLayout.visibility = View.VISIBLE
                }else{
                    Snackbar.make(view, resources.getString(R.string.no_user_data), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

}