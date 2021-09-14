package com.metehanbolat.teknofestegitim.view.mainviews.computervision

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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentComputerVisionInfoBinding

class ComputerVisionInfoFragment : Fragment() {

    private var _binding : FragmentComputerVisionInfoBinding? = null
    private val binding get() = _binding!!
    private var counterYes : Int? = null
    private var counterNo : Int? = null
    private var counter : Int? = null

    private var thirdCoinControl : Any? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComputerVisionInfoBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.background_design_start_color)

        auth = Firebase.auth
        firestore = Firebase.firestore

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_computerVisionInfoFragment_to_computerVisionFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        val bundle : ComputerVisionInfoFragmentArgs by navArgs()
        val control = bundle.control

        if (control == 1){
            binding.question.visibility = View.INVISIBLE
            binding.noButton.visibility = View.INVISIBLE
            binding.yesButton.visibility = View.INVISIBLE
            binding.giphy.visibility = View.INVISIBLE
            binding.wallEightImage.visibility = View.INVISIBLE
            binding.linearLayoutImages.visibility = View.VISIBLE
            binding.information4.visibility = View.VISIBLE
            binding.information5.visibility = View.VISIBLE
            binding.colorScala.visibility = View.VISIBLE
            binding.questionRobot.visibility = View.VISIBLE
            binding.buttonRobot.visibility = View.VISIBLE
        }

        counterYes = 1
        counterNo = 1
        counter = 0

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRobot.setOnClickListener {
            Snackbar.make(it,resources.getString(R.string.didYouReadInfo),Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.iRead)){
                secondCoinChange()
                getNextFragment()
            }.show()
        }

        binding.noButton.setOnClickListener {
            when(counterNo){
                1 -> when(counterYes){
                    1 -> {
                        binding.question.text = resources.getString(R.string.absolutelyNot)
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.nowSee)
                            binding.wallEightImage.setImageResource(R.drawable.pale_look_eight)
                            visibleButton()
                        },3000)
                        counterNo = 2
                    }
                    2 -> {
                        binding.question.text = resources.getString(R.string.absolutelyNot)
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.nowSee)
                            binding.wallEightImage.setImageResource(R.drawable.pale_look_eight)
                            visibleButton()
                        },3000)
                        counterNo = 2

                    }
                    3 -> {
                        binding.question.text = resources.getString(R.string.absolutelyNot)
                            invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.nowSee)
                            binding.wallEightImage.setImageResource(R.drawable.pale_look_eight)
                            visibleButton()
                        },3000)
                        counterNo = 2
                    }
                    4 -> {
                        invisibleButton()
                        invisibleImage()
                        visibleGiphy(R.drawable.stop)
                        binding.question.text = resources.getString(R.string.empty)
                        Snackbar.make(it,resources.getString(R.string.wantTenGoldLose),Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.no)){
                            invisibleGiphy()
                            visibleImage()
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            binding.question.text = resources.getString(R.string.hereItIs)
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        }.show()
                    }
                }
                2 -> when(counterYes){
                    1 -> {
                        binding.question.text = resources.getString(R.string.reallyCant)
                        visibleGiphy(R.drawable.cry)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.stillNo)
                            invisibleGiphy()
                            visibleImage()
                            visibleButton()
                        },3000)
                        counterNo = 3
                    }
                    2 -> {
                        binding.question.text = resources.getString(R.string.reallyCant)
                        visibleGiphy(R.drawable.cry)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.stillNo)
                            invisibleGiphy()
                            visibleImage()
                            visibleButton()
                        },3000)
                        counterNo = 3
                    }
                    3 -> {
                        invisibleButton()
                        invisibleImage()
                        visibleGiphy(R.drawable.stop)
                        binding.question.text = resources.getString(R.string.sure)
                        Snackbar.make(it,resources.getString(R.string.loseTenGold),Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.saw)){
                            binding.question.text = resources.getString(R.string.sureSee)
                            binding.giphy.setImageResource(R.drawable.sunglasses)
                            Handler(Looper.getMainLooper()).postDelayed({
                                invisibleGiphy()
                                visibleImage()
                                binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                                binding.question.text = resources.getString(R.string.hereEight)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    endGame()
                                },3000)
                            },3000)
                        }.show()
                    }
                }
                3 -> when(counterYes){
                    1 -> {
                        binding.question.text = resources.getString(R.string.hardLook)
                        visibleGiphy(R.drawable.cry)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.hereItIs)
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            invisibleGiphy()
                            visibleImage()
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        },3000)
                    }
                    2 -> {
                        binding.question.text = resources.getString(R.string.hardLook)
                        visibleGiphy(R.drawable.cry)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.hereItIs)
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            invisibleGiphy()
                            visibleImage()
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        },3000)
                    }
                }
            }
        }

        binding.yesButton.setOnClickListener {
            when(counterYes){
                1 -> when(counterNo){
                    1 -> {
                        binding.question.text = resources.getString(R.string.really)
                        visibleGiphy(R.drawable.cry)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.cant_see)
                            invisibleGiphy()
                            visibleImage()
                            visibleButton()
                        },3000)
                        counterYes = 2
                    }
                    2 -> {
                        binding.question.text = resources.getString(R.string.more)
                        visibleGiphy(R.drawable.sunglasses)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.hereEight)
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            invisibleGiphy()
                            visibleImage()
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        }, 3000)
                    }
                    3 -> {
                        binding.question.text = resources.getString(R.string.perfect)
                        visibleGiphy(R.drawable.sunglasses)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.hereEight)
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            invisibleGiphy()
                            visibleImage()
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        }, 3000)
                    }
                }
                2 -> when(counterNo){
                    1 -> {
                        binding.question.text = resources.getString(R.string.againLook)
                        visibleGiphy(R.drawable.sunglasses)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.stillSee)
                            invisibleGiphy()
                            visibleImage()
                            visibleButton()
                        },3000)
                        counterYes = 3
                    }
                    2 -> {
                        binding.question.text = resources.getString(R.string.congrats)
                        visibleGiphy(R.drawable.sunglasses)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.hereEight)
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            invisibleGiphy()
                            visibleImage()
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        },3000)
                        counterYes = 3
                    }
                }
                3 -> when(counterNo){
                    1 -> {
                        invisibleButton()
                        invisibleImage()
                        visibleGiphy(R.drawable.stop)
                        Snackbar.make(it,resources.getString(R.string.loseTenGold),Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.noSaw)){
                            binding.question.text = resources.getString(R.string.sureNoSee)
                            binding.giphy.setImageResource(R.drawable.sunglasses)
                            Handler(Looper.getMainLooper()).postDelayed({
                                invisibleGiphy()
                                visibleButton()
                                visibleImage()
                                binding.wallEightImage.setImageResource(R.drawable.pale_look_eight)
                                binding.question.text = resources.getString(R.string.nowSee)
                            },3000)
                        }.show()
                        counterYes = 4
                    }
                    2 -> {
                        binding.question.text = resources.getString(R.string.congrats)
                        visibleGiphy(R.drawable.sunglasses)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.hereEight)
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            invisibleGiphy()
                            visibleImage()
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        },3000)
                    }
                }
                4 -> when(counterNo){
                    1 -> {
                        binding.question.text = resources.getString(R.string.congrats)
                        visibleGiphy(R.drawable.sunglasses)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.hereEight)
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            invisibleGiphy()
                            visibleImage()
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        },3000)
                    }
                    2 -> {
                        binding.question.text = resources.getString(R.string.congrats)
                        visibleGiphy(R.drawable.sunglasses)
                        invisibleImage()
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.hereEight)
                            binding.wallEightImage.setImageResource(R.drawable.visible_eight)
                            invisibleGiphy()
                            visibleImage()
                            Handler(Looper.getMainLooper()).postDelayed({
                                endGame()
                            },3000)
                        },3000)
                    }
                }
            }
        }
    }

    private fun visibleButton(){
        binding.noButton.visibility = View.VISIBLE
        binding.yesButton.visibility = View.VISIBLE
    }
    private fun invisibleButton(){
        binding.noButton.visibility = View.GONE
        binding.yesButton.visibility = View.GONE
    }
    private fun visibleGiphy(giphy : Int){
        binding.giphy.visibility = View.VISIBLE
        binding.giphy.setImageResource(giphy)
    }
    private fun invisibleGiphy(){
        binding.giphy.visibility = View.INVISIBLE
    }
    private fun visibleImage(){
        binding.wallEightImage.visibility = View.VISIBLE
    }
    private fun invisibleImage(){
        binding.wallEightImage.visibility = View.INVISIBLE
    }
    private fun endGame(){
        binding.question.visibility = View.INVISIBLE
        binding.noButton.visibility = View.INVISIBLE
        binding.yesButton.visibility = View.INVISIBLE
        binding.wallEightImage.visibility = View.INVISIBLE
        binding.linearLayoutImages.visibility = View.VISIBLE
        binding.information4.visibility = View.VISIBLE
        binding.information5.visibility = View.VISIBLE
        binding.colorScala.visibility = View.VISIBLE
        binding.questionRobot.visibility = View.VISIBLE
        binding.buttonRobot.visibility = View.VISIBLE
    }

    private fun secondCoinChange(){
        val coinControlUpdate = firestore.collection(resources.getString(R.string.firebase_userData)).document(auth.currentUser?.email.toString())
        coinControlUpdate.update(resources.getString(R.string.firebase_secondCoin),1)
    }

    private fun getNextFragment(){
        val getCoinInfo = firestore.collection(resources.getString(R.string.firebase_userData)).document(auth.currentUser?.email.toString())
        getCoinInfo.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    thirdCoinControl = document.data!![resources.getString(R.string.firebase_thirdCoin)]
                    val action = ComputerVisionInfoFragmentDirections.actionComputerVisionInfoFragmentToHowComputerVisionFragment(thirdCoinControl.toString().toInt())
                    Navigation.findNavController(requireView()).navigate(R.id.action_computerVisionInfoFragment_to_howComputerVisionFragment,action.arguments)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}