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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentGameListBinding

class GameListFragment : Fragment() {

    private var _binding : FragmentGameListBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    private var currentCoin : String? = null

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseFirestore = Firebase.firestore
        firebaseAuth = Firebase.auth

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCoin(view, firebaseAuth.currentUser?.email.toString())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCoin(view : View, email : String){
        val coinRef = firebaseFirestore.collection("UserData").document(email)
        coinRef.get().addOnSuccessListener { document ->
            if (document != null){
                currentCoin = document.data!!["userCoin"].toString()
                binding.userCoin.text = currentCoin
            }else{
                Snackbar.make(view, "Altın bilgisi bulunamadı.", Snackbar.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            println(e.localizedMessage)
        }
    }

}