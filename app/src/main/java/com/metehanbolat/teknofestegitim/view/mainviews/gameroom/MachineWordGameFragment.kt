package com.metehanbolat.teknofestegitim.view.mainviews.gameroom

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineWordGameBinding
import com.metehanbolat.teknofestegitim.utils.UserFirebaseProcess

class MachineWordGameFragment : Fragment() {

    private var _binding : FragmentMachineWordGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore : FirebaseFirestore
    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var random : String
    private lateinit var notRandom : String
    private lateinit var randomOrNot : String

    private lateinit var navController: NavController

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
                Snackbar.make(view, resources.getString(R.string.are_u_sure_quit_game), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.exit)){
                    navController = findNavController()
                    navController.navigate(R.id.action_machineWordGameFragment_to_machineGameListFragment)
                }.show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore

        randomOrNot = random

        binding.randomWordButton.setOnClickListener {
            randomOrNot = random
        }

        binding.lastLetterWordButton.setOnClickListener {
            randomOrNot = notRandom
        }

        val wordArray = resources.getStringArray(R.array.word_array)

        var randomNumber = (0 until (wordArray.size) - 1).random()
        binding.wordTextView.text = wordArray[randomNumber]
        binding.pastWords.append(binding.wordTextView.text.toString())
        binding.wordEditText.setOnKeyListener(View.OnKeyListener { keyView, i, keyEvent ->

            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                if (randomOrNot == random && binding.wordEditText.text.first() == binding.wordTextView.text.last()){
                    Snackbar.make(keyView, resources.getString(R.string.nice_answer), Snackbar.LENGTH_SHORT).show()
                    val earnGold = UserFirebaseProcess(firestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    earnGold.getCoin(resources.getString(R.string.firebase_userCoin)){
                        earnGold.userCoinIncrease(resources.getString(R.string.firebase_userCoin), it!!, 2)
                    }
                    binding.pastWords.append(resources.getString(R.string.comma) + binding.wordEditText.text.toString())
                    binding.wordEditText.setText(resources.getString(R.string.empty))
                    randomNumber = (0 until (wordArray.size) - 1).random()
                    binding.wordTextView.text = wordArray[randomNumber]
                    binding.pastWords.append(resources.getString(R.string.comma) + binding.wordTextView.text.toString() )
                }else if (randomOrNot == notRandom && binding.wordEditText.text.first() == binding.wordTextView.text.last()){
                    binding.pastWords.append(resources.getString(R.string.comma) + binding.wordEditText.text)
                    val earnGold = UserFirebaseProcess(firestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    earnGold.getCoin(resources.getString(R.string.firebase_userCoin)){
                        earnGold.userCoinIncrease(resources.getString(R.string.firebase_userCoin), it!!, 2)
                    }
                    if (wordArray.find { it.first() == binding.wordEditText.text.last() } != null){
                        binding.wordTextView.text = wordArray.find { it.first() == binding.wordEditText.text.last() }
                        binding.pastWords.append(resources.getString(R.string.comma) + binding.wordTextView.text.toString())
                    }else{
                        binding.wordTextView.text = resources.getString(R.string.you_won)
                        earnGold.getCoin(resources.getString(R.string.firebase_userCoin)){
                            earnGold.userCoinIncrease(resources.getString(R.string.firebase_userCoin), it!!, 5)
                        }
                    }
                } else{
                    Snackbar.make(keyView, resources.getString(R.string.last_word_press_enter), Snackbar.LENGTH_SHORT).show()
                    val earnGold = UserFirebaseProcess(firestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    earnGold.getCoin(resources.getString(R.string.firebase_userCoin)) {
                        earnGold.userCoinDecrease(resources.getString(R.string.firebase_userCoin), it!!, 5)
                    }
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