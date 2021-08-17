package com.metehanbolat.teknofestegitim.view.mainviews.computervision

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentComputerVisionInfoBinding

class ComputerVisionInfoFragment : Fragment() {

    private var _binding : FragmentComputerVisionInfoBinding? = null
    private val binding get() = _binding!!
    private var counterYes : Int? = null
    private var counterNo : Int? = null

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComputerVisionInfoBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.black)
        counterYes = 1
        counterNo = 1

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        binding.button.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_computerVisionInfoFragment_to_gameListFragment)
        }

 */

        binding.noButton.setOnClickListener {
            when(counterNo){
                1 -> when(counterYes){
                    1 -> {
                        binding.question.text = resources.getString(R.string.absoNot)
                        invisibleButton()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.question.text = resources.getString(R.string.nowSee)
                            binding.duvarSekizImage.setImageResource(R.drawable.azgorunensekiz)
                            visibleButton()
                        },3000)
                        counterNo = 2
                    }
                    2 -> {

                    }
                    3 -> {

                    }
                    4 -> {
                        invisibleButton()
                        invisibleImage()
                        visibleGiphy(R.drawable.stop)
                        binding.question.text = ""
                        Snackbar.make(it,"10 altın kaybetmek istiyor gibisin",Snackbar.LENGTH_INDEFINITE).setAction("Hayır"){
                            invisibleGiphy()
                            visibleImage()
                            binding.duvarSekizImage.setImageResource(R.drawable.gorunensekiz)
                            binding.question.text = resources.getString(R.string.hereItIs)

                            // BURAYA OYUNDAN SONRAKİ KODLAR YAZILABİLİR.

                        }.show()

                    }
                }
                2 -> when(counterYes){
                    1 -> {
                        binding.question.text = resources.getString(R.string.reallycant)
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

                    }
                    3 -> {

                    }
                    4 -> {

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
                            binding.duvarSekizImage.setImageResource(R.drawable.gorunensekiz)
                            invisibleGiphy()
                            visibleImage()
                        },3000)
                        counterNo = 4

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                    4 -> {

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

                    }
                    3 -> {

                    }
                }
                2 -> when(counterNo){
                    1 -> {
                        binding.question.text = resources.getString(R.string.againLook)
                        visibleGiphy(R.drawable.sunglass)
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

                    }
                    3 -> {

                    }
                }
                3 -> when(counterNo){
                    1 -> {
                        invisibleButton()
                        invisibleImage()
                        visibleGiphy(R.drawable.stop)
                        Snackbar.make(it,"Üzgünüm 10 altın kaybettin.",Snackbar.LENGTH_INDEFINITE).setAction("Görmedim"){
                            binding.question.text = resources.getString(R.string.finaally)
                            binding.giphy.setImageResource(R.drawable.sunglass)
                            Handler(Looper.getMainLooper()).postDelayed({
                                invisibleGiphy()
                                visibleButton()
                                visibleImage()
                                binding.duvarSekizImage.setImageResource(R.drawable.azgorunensekiz)
                                binding.question.text = resources.getString(R.string.nowSee)
                            },3000)
                        }.show()
                        counterYes = 4
                    }
                    2 -> {

                    }
                    3 -> {

                    }
                }
                4 -> when(counterNo){
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

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
    private fun clickableButton(){
        binding.noButton.isClickable = true
        binding.yesButton.isClickable = true
    }
    private fun unClickableButton(){
        binding.noButton.isClickable = false
        binding.yesButton.isClickable = false
    }
    private fun visibleGiphy(giphy : Int){
        binding.giphy.visibility = View.VISIBLE
        binding.giphy.setImageResource(giphy)
    }
    private fun invisibleGiphy(){
        binding.giphy.visibility = View.INVISIBLE
    }
    private fun visibleImage(){
        binding.duvarSekizImage.visibility = View.VISIBLE
    }
    private fun invisibleImage(){
        binding.duvarSekizImage.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}