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
import com.google.android.material.snackbar.Snackbar
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineGameListBinding

class MachineGameListFragment : Fragment() {

    private var _binding : FragmentMachineGameListBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineGameListBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.game_fragment)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                Snackbar.make(view,resources.getString(R.string.back_main_menu), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.back)){
                    navController = findNavController()
                    navController.navigate(R.id.action_machineGameListFragment_to_mainFragment)
                }.show()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.machineTicTacToeCard.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_machineGameListFragment_to_machineTicTacToeFragment)
        }

        binding.machineQuizCard.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_machineGameListFragment_to_machineQuizGameFragment)
        }

        binding.machineWordCard.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_machineGameListFragment_to_machineWordGameFragment)
        }

        binding.machineMineSweeperCard.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_machineGameListFragment_to_machineMineSweeperFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}