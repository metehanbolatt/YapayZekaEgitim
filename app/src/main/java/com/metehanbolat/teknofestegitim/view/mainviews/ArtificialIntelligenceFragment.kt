package com.metehanbolat.teknofestegitim.view.mainviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.metehanbolat.teknofestegitim.databinding.FragmentArtificialIntelligenceBinding

class ArtificialIntelligenceFragment : Fragment() {

    private var _binding : FragmentArtificialIntelligenceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtificialIntelligenceBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}