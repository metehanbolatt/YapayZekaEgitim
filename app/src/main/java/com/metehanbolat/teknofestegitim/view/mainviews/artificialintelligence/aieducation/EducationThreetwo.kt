package com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.aieducation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentEducationThreetwoBinding

class EducationThreetwo : Fragment() {

    private var _binding : FragmentEducationThreetwoBinding? = null
    private val binding get() = _binding!!
    private var controlNumber : Int = 0

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEducationThreetwoBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener {
            controlNumber += 1
            when(controlNumber){
                1 -> binding.classification.visibility = View.VISIBLE
                2 -> binding.classificationInfo.visibility = View.VISIBLE
                3 -> binding.clustering.visibility = View.VISIBLE
                4 -> binding.clusteringInfo.visibility = View.VISIBLE
                5 -> binding.classvsclus.visibility = View.VISIBLE
                6 -> binding.algorithm.visibility = View.VISIBLE
                7 -> binding.algorithmInfo.visibility = View.VISIBLE
                else -> {
                    binding.root.isClickable = false
                    navController = findNavController()
                    navController.navigate(R.id.action_educationThreetwo_to_educationFour)
                }
            }
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_educationThreetwo_to_mainEducation)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}