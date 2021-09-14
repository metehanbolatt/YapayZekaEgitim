package com.metehanbolat.teknofestegitim.view.mainviews.computervision

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentEightDrawHashBinding

class EightDrawHashFragment : Fragment() {

    private var _binding : FragmentEightDrawHashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEightDrawHashBinding.inflate(inflater, container, false)
        val view = binding.root

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.drawHashEightImage.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.drawHashEightImage to resources.getString(R.string.image_small2))
            val action = EightDrawHashFragmentDirections.actionEightDrawHashFragmentToHowComputerVisionFragment(2)
            findNavController().navigate(
                R.id.action_eightDrawHashFragment_to_howComputerVisionFragment,
                action.arguments,
                null,
                extras
            )
        }

        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}