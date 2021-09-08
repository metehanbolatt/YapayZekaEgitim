package com.metehanbolat.teknofestegitim.view.mainviews.gameroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
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
            val extras = FragmentNavigatorExtras(binding.machineTicTacToeCard to resources.getString(R.string.anim_image_big))
            navController = findNavController()
            navController.navigate(R.id.action_machineGameListFragment_to_animMachineTicTacToeFragment, null, null, extras)
        }

        binding.machineQuizCard.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.machineQuizCard to resources.getString(R.string.anim_image_big2))
            navController = findNavController()
            navController.navigate(R.id.action_machineGameListFragment_to_animMachineQuizGameFragment, null, null, extras)
        }

        binding.machineWordCard.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.machineWordCard to resources.getString(R.string.anim_image_big3))
            navController = findNavController()
            navController.navigate(R.id.action_machineGameListFragment_to_animMachineWordGameFragment, null, null, extras)
        }

        binding.machineMineSweeperCard.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.machineMineSweeperCard to resources.getString(R.string.anim_image_big4))
            navController = findNavController()
            navController.navigate(R.id.action_machineGameListFragment_to_animMachineMineSweeperFragment, null, null, extras)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}