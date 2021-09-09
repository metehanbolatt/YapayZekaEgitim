package com.metehanbolat.teknofestegitim.view.mainviews.gameroom

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
import com.metehanbolat.teknofestegitim.databinding.FragmentAIGameOneBinding

class AIGameOneFragment : Fragment() {
    private lateinit var navController: NavController
    private var _binding : FragmentAIGameOneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAIGameOneBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
/*
                Snackbar.make(view,resources.getString(R.string.are_u_sure_quit_game), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.exit)){
                    navController = findNavController()
                    navController.navigate()
                }.show()

 */

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}