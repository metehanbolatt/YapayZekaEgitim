package com.metehanbolat.teknofestegitim.view.mainviews.gift

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
import com.metehanbolat.teknofestegitim.databinding.FragmentGiftAwardsAddBinding

class GiftAwardsAddFragment : Fragment() {

    private var _binding : FragmentGiftAwardsAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var firestore : FirebaseFirestore
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGiftAwardsAddBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_giftAwardsAddFragment_to_giftFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.background_color)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val docRef = firestore.collection(resources.getString(R.string.firebase_userData)).document(firebaseAuth.currentUser!!.email.toString())

        binding.awardsAddButton.setOnClickListener {
            val award = hashMapOf(
                "award" to binding.awardName.text.toString(),
                "coin" to binding.coinAmount.text.toString())

            docRef.collection(firebaseAuth.currentUser!!.email.toString()).document(binding.awardName.text.toString()).set(award).addOnSuccessListener {
                Snackbar.make(view, "Eklendi", Snackbar.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}