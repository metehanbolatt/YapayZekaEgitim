package com.metehanbolat.teknofestegitim.view.mainviews.computervision

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
import com.metehanbolat.teknofestegitim.databinding.FragmentGameListBinding

class GameListFragment : Fragment() {

    private var _binding : FragmentGameListBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        val view = binding.root

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                val action = EightDrawBigFragmentDirections.actionEightDrawBigFragmentToHowComputerVisionFragment(4)
                navController.navigate(R.id.action_gameListFragment_to_howComputerVisionFragment,action.arguments)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.game_fragment)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}