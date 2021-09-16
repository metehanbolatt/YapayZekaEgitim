package com.metehanbolat.teknofestegitim.view.teacherviews.quizroom

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
import com.metehanbolat.teknofestegitim.databinding.FragmentTeacherQuizBinding

class TeacherQuizFragment : Fragment() {

    private var _binding : FragmentTeacherQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    private var counter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTeacherQuizBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.black)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                when(counter){
                    0 -> {
                        Snackbar.make(view,resources.getString(R.string.againClickExit), Snackbar.LENGTH_SHORT).show()
                        counter++
                    }
                    1 -> {
                        navController = findNavController()
                        navController.navigate(R.id.action_teacherQuizFragment_to_teacherMainFragment)
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        auth = Firebase.auth
        firestore = Firebase.firestore

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}