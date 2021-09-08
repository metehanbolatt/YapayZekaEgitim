package com.metehanbolat.teknofestegitim.view.mainviews.machinelearning

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
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineMachineNeuralBinding

class MachineMachineNeuralFragment : Fragment() {

    private var _binding : FragmentMachineMachineNeuralBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineMachineNeuralBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.machine_human_background_color)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_machineMachineNeuralFragment_to_machineHumanNeuralFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.machineMachineButton.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_machineMachineNeuralFragment_to_machineCanTeachFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}