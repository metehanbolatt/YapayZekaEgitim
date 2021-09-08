package com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentArtificialIntelligenceBinding

class ArtificialIntelligenceFragment : Fragment() {

    private var _binding : FragmentArtificialIntelligenceBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtificialIntelligenceBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardViewGameOne.setOnLongClickListener {
            Snackbar.make(it, resources.getString(R.string.game_one), Snackbar.LENGTH_LONG).show()
            true
        }

        binding.cardViewGameOne.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_artificialIntelligenceFragment_to_AIGameOneFragment)
        }

        binding.cardViewGameTwo.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_artificialIntelligenceFragment_to_AIGameTwoFragment)
        }

        binding.cardViewGameTwo.setOnLongClickListener {
            Snackbar.make(it, resources.getString(R.string.game_two), Snackbar.LENGTH_LONG).show()
            true
        }

        binding.cardViewScore.setOnLongClickListener {
            Snackbar.make(it, resources.getString(R.string.score), Snackbar.LENGTH_LONG).show()
            true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}