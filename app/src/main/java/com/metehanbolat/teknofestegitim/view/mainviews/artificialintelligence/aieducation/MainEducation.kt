package com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.aieducation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMainEducationBinding
import java.util.*

class MainEducation : Fragment() {

    private var _binding : FragmentMainEducationBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainEducationBinding.inflate(inflater,container,false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.black)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val userName = MainEducationArgs.fromBundle(it).name
            binding.mainEducationTitle.text = "${resources.getString(R.string.welcome)} $userName"
        }

        binding.cardViewEducationOne.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainEducation_to_educationOne)
        }
        binding.cardViewEducationTwo.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainEducation_to_educationTwo)
        }
        binding.cardViewEducationThree.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainEducation_to_educationThree)
        }
        binding.cardViewGame.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_mainEducation_to_gameRoomAnimFragment)
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_mainEducation_to_mainFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}