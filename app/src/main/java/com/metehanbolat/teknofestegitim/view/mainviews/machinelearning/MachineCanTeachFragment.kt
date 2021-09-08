package com.metehanbolat.teknofestegitim.view.mainviews.machinelearning

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineCanTeachBinding

class MachineCanTeachFragment : Fragment() {

    private var _binding : FragmentMachineCanTeachBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineCanTeachBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.machine_can_teach_background_color)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_machineCanTeachFragment_to_machineMachineNeuralFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        Handler(Looper.getMainLooper()).postDelayed({
            if (binding.machineCanTeachInfoOne.alpha == 1f){
                Snackbar.make(view, resources.getString(R.string.click_image), Snackbar.LENGTH_LONG).show()
            }
        },5000)

        binding.machineCanTeachButton.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_machineCanTeachFragment_to_machineChooseSectionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}