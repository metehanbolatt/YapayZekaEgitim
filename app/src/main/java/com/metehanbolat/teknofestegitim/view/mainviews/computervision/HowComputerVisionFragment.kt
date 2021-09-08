package com.metehanbolat.teknofestegitim.view.mainviews.computervision

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentHowComputerVisionBinding

class HowComputerVisionFragment : Fragment() {

    private var _binding : FragmentHowComputerVisionBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private var counterZoomDraw = 0
    private var counterZoomHashDraw = 0
    private var counterZoomHash = 0
    private var bigControl = 0

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHowComputerVisionBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        firestore = Firebase.firestore

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                val action = HowComputerVisionFragmentDirections.actionHowComputerVisionFragmentToComputerVisionInfoFragment(1)
                navController.navigate(R.id.action_howComputerVisionFragment_to_computerVisionInfoFragment,action.arguments)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.black)

        val bundle : HowComputerVisionFragmentArgs by navArgs()
        bigControl = bundle.bigControl

        when(bigControl){
            1 -> {
                binding.cizimSekizImage.visibility = View.VISIBLE
                binding.learnButton.visibility = View.INVISIBLE
                binding.informationHow2.visibility = View.VISIBLE
                binding.buttonHowComputer.visibility = View.VISIBLE

                counterZoomDraw = 1

                val animation = TransitionInflater.from(requireContext()).inflateTransition(
                    android.R.transition.move
                )
                sharedElementEnterTransition = animation
                sharedElementReturnTransition = animation
            }
            2 -> {
                binding.cizimSekizImage.visibility = View.VISIBLE
                binding.learnButton.visibility = View.INVISIBLE
                binding.informationHow2.visibility = View.VISIBLE
                binding.cizimHashSekizImage.visibility = View.VISIBLE
                binding.informationHow3.visibility = View.VISIBLE
                binding.buttonHowComputer.visibility = View.VISIBLE

                counterZoomHashDraw = 1

                val animation = TransitionInflater.from(requireContext()).inflateTransition(
                    android.R.transition.move
                )
                sharedElementEnterTransition = animation
                sharedElementReturnTransition = animation
            }
            3 -> {
                binding.cizimSekizImage.visibility = View.VISIBLE
                binding.learnButton.visibility = View.INVISIBLE
                binding.informationHow2.visibility = View.VISIBLE
                binding.cizimHashSekizImage.visibility = View.VISIBLE
                binding.informationHow3.visibility = View.VISIBLE
                binding.informationHow4.visibility = View.VISIBLE
                binding.hashSekizImage.visibility = View.VISIBLE
                binding.buttonHowComputer.visibility = View.VISIBLE
                binding.buttonHowComputer.visibility = View.VISIBLE

                counterZoomHash = 1

                val animation = TransitionInflater.from(requireContext()).inflateTransition(
                    android.R.transition.move
                )
                sharedElementEnterTransition = animation
                sharedElementReturnTransition = animation
            }
            4 -> {
                binding.cizimSekizImage.visibility = View.VISIBLE
                binding.learnButton.visibility = View.INVISIBLE
                binding.informationHow2.visibility = View.VISIBLE
                binding.cizimHashSekizImage.visibility = View.VISIBLE
                binding.informationHow3.visibility = View.VISIBLE
                binding.informationHow4.visibility = View.VISIBLE
                binding.hashSekizImage.visibility = View.VISIBLE
                binding.buttonHowComputer.visibility = View.VISIBLE
                binding.buttonHowComputer.visibility = View.VISIBLE
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cizimSekizImage.setOnClickListener {

            val extras = FragmentNavigatorExtras(binding.cizimSekizImage to "image_big")
            findNavController().navigate(
                R.id.action_howComputerVisionFragment_to_eightDrawBigFragment,
                null,
                null,
                extras
            )
        }

        binding.cizimHashSekizImage.setOnClickListener {

            val extras2 = FragmentNavigatorExtras(binding.cizimHashSekizImage to "image_big2")
            findNavController().navigate(
                R.id.action_howComputerVisionFragment_to_eightDrawHashFragment,
                null,
                null,
                extras2
            )
        }

        binding.hashSekizImage.setOnClickListener {

            val extras3 = FragmentNavigatorExtras(binding.hashSekizImage to "image_big3")
            findNavController().navigate(
                R.id.action_howComputerVisionFragment_to_eightHashBigFragment,
                null,
                null,
                extras3
            )
        }

        binding.buttonHowComputer.setOnClickListener {

            if (bigControl == 0 && counterZoomDraw == 0){
                Snackbar.make(it,resources.getString(R.string.click_look),Snackbar.LENGTH_SHORT).show()

            }else if (bigControl == 1 && counterZoomDraw == 1 ){
                binding.cizimSekizImage.visibility = View.VISIBLE
                binding.learnButton.visibility = View.INVISIBLE
                binding.informationHow2.visibility = View.VISIBLE
                binding.cizimHashSekizImage.visibility = View.VISIBLE
                binding.informationHow3.visibility = View.VISIBLE
                binding.buttonHowComputer.visibility = View.VISIBLE
                counterZoomDraw = 2

            }else if (bigControl == 1 && counterZoomHashDraw == 0){
                Snackbar.make(it,resources.getString(R.string.click_look),Snackbar.LENGTH_SHORT).show()

            }else if (bigControl == 2 && counterZoomHashDraw == 1){
                binding.cizimSekizImage.visibility = View.VISIBLE
                binding.learnButton.visibility = View.INVISIBLE
                binding.informationHow2.visibility = View.VISIBLE
                binding.cizimHashSekizImage.visibility = View.VISIBLE
                binding.informationHow3.visibility = View.VISIBLE
                binding.buttonHowComputer.visibility = View.VISIBLE
                binding.informationHow4.visibility = View.VISIBLE
                binding.hashSekizImage.visibility = View.VISIBLE

                counterZoomHashDraw = 2

            }else if (bigControl == 2 && counterZoomHash == 0){
                Snackbar.make(it,resources.getString(R.string.click_look),Snackbar.LENGTH_SHORT).show()

            }else if (bigControl == 3 && counterZoomHash == 1){
                navController = findNavController()
                navController.navigate(R.id.action_howComputerVisionFragment_to_gameRoomAnimFragment)
                thirdCoinUpdate()
            }else if (bigControl == 4){
                navController = findNavController()
                navController.navigate(R.id.action_howComputerVisionFragment_to_gameRoomAnimFragment)
                thirdCoinUpdate()
            }
        }

        binding.learnButton.setOnClickListener {
            binding.learnButton.visibility = View.INVISIBLE
            binding.cizimSekizImage.visibility = View.VISIBLE
            binding.informationHow2.visibility = View.VISIBLE
            binding.buttonHowComputer.visibility = View.VISIBLE
        }
    }

    private fun thirdCoinUpdate(){
        val coinControlUpdate = firestore.collection(resources.getString(R.string.firebase_userData)).document(auth.currentUser?.email.toString())
        coinControlUpdate.update(resources.getString(R.string.firebase_thirdCoin),4)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}