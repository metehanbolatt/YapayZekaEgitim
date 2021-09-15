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
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentEducationSevenBinding

class EducationSeven : Fragment() {

    private var _binding : FragmentEducationSevenBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEducationSevenBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.fragment_education_background)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_educationSeven_to_educationFive)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.educationSevenButton.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_educationSeven_to_educationEight)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}