package com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.aieducation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMainEducationBinding

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            navController.navigate(R.id.action_mainEducation_to_artificialIntelligenceFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}