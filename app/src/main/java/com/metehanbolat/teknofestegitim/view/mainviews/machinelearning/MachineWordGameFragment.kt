package com.metehanbolat.teknofestegitim.view.mainviews.machinelearning

import android.os.Bundle
import android.view.KeyEvent
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
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineWordGameBinding

class MachineWordGameFragment : Fragment() {

    private lateinit var navController: NavController

    private var _binding : FragmentMachineWordGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var random : String
    private lateinit var notRandom : String
    private lateinit var randomOrNot : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineWordGameBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.background_design_start_color)

        random = resources.getString(R.string.random)
        notRandom = resources.getString(R.string.not_random)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_machineWordGameFragment_to_machineGameListFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        randomOrNot = random

        binding.randomWordButton.setOnClickListener {
            randomOrNot = random
        }

        binding.lastLetterWordButton.setOnClickListener {
            randomOrNot = notRandom
        }

        val wordArray = resources.getStringArray(R.array.word_array)

        var randomNumber = (0 until (wordArray.size)-1).random()
        binding.wordTextView.text = wordArray[randomNumber]
        binding.pastWords.append(binding.wordTextView.text.toString())
        binding.wordEditText.setOnKeyListener(View.OnKeyListener { keyView, i, keyEvent ->

            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                if (randomOrNot == random && binding.wordEditText.text.first() == binding.wordTextView.text.last()){
                    Snackbar.make(keyView, resources.getString(R.string.nice_answer), Snackbar.LENGTH_SHORT).show()
                    binding.pastWords.append(", " + binding.wordEditText.text.toString())
                    binding.wordEditText.setText(resources.getString(R.string.empty))
                    randomNumber = (0 until (wordArray.size)-1).random()
                    binding.wordTextView.text = wordArray[randomNumber]
                    binding.pastWords.append(", " + binding.wordTextView.text.toString() )
                }else if (randomOrNot == notRandom && binding.wordEditText.text.first() == binding.wordTextView.text.last()){
                    binding.pastWords.append(", "+binding.wordEditText.text)
                    if (wordArray.find { it.first() == binding.wordEditText.text.last() } != null){
                        binding.wordTextView.text = wordArray.find { it.first() == binding.wordEditText.text.last() }
                        binding.pastWords.append(", "+ binding.wordTextView.text.toString())
                    }else{
                        binding.wordTextView.text = resources.getString(R.string.you_won)
                    }
                } else{
                    Snackbar.make(keyView, resources.getString(R.string.last_word_press_enter), Snackbar.LENGTH_SHORT).show()
                }
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}