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
import com.metehanbolat.teknofestegitim.databinding.FragmentEducationFourBinding

class EducationFour : Fragment() {

    private var _binding : FragmentEducationFourBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEducationFourBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.black)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_educationFour_to_educationThree)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.educationFourButton.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_educationFour_to_educationSix)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}