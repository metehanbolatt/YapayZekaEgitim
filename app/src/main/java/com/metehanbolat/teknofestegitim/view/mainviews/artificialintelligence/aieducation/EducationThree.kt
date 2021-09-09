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
import com.metehanbolat.teknofestegitim.databinding.FragmentEducationThreeBinding

class EducationThree : Fragment() {

    private var _binding : FragmentEducationThreeBinding? = null
    private val binding get() = _binding!!
    private var controlNum : Int = 0

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEducationThreeBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Snackbar.make(view, resources.getString(R.string.click_screen), Snackbar.LENGTH_LONG).show()

        binding.root.setOnClickListener {
            controlNum += 1
            when(controlNum){
                1 -> binding.ysaText.visibility = View.VISIBLE
                2 -> binding.ysaInfoText.visibility = View.VISIBLE
                3 -> binding.showModelText.visibility = View.VISIBLE
                4 -> binding.ysaModelImage.visibility = View.VISIBLE
                5 -> binding.showRealText.visibility = View.VISIBLE
                6 -> binding.ysaRealImage.visibility = View.VISIBLE
                7 -> binding.infoText.visibility = View.VISIBLE
                else -> {
                    navController = findNavController()
                    navController.navigate(R.id.action_educationThree_to_educationThreetwo)
                }
            }
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_educationThree_to_mainEducation)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}