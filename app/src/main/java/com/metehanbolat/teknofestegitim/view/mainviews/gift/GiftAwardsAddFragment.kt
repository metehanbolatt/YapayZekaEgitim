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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentGiftAwardsAddBinding

class GiftAwardsAddFragment : Fragment() {

    private var _binding : FragmentGiftAwardsAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController : NavController

    private lateinit var firestore : FirebaseFirestore
    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var awardName : String

    private lateinit var docRef : DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGiftAwardsAddBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.gift_add_background)

        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore

        docRef = firestore.collection(resources.getString(R.string.firebase_userData)).document(firebaseAuth.currentUser!!.email.toString())

        arguments?.let {
            if (GiftAwardsAddFragmentArgs.fromBundle(it).award != null){
                awardName = GiftAwardsAddFragmentArgs.fromBundle(it).award.toString()
                binding.awardName.setText(awardName)
                binding.awardName.isEnabled = false
                binding.awardsAddButton.text = resources.getString(R.string.update)

                binding.awardsAddButton.setOnClickListener { awardsAddButtonView ->
                    if (binding.coinAmount.text.toString() == resources.getString(R.string.empty)){
                        Snackbar.make(awardsAddButtonView, resources.getString(R.string.new_coin_value), Snackbar.LENGTH_LONG).show()
                    }else{
                        val award = hashMapOf(
                            resources.getString(R.string.hash_award) to binding.awardName.text.toString(),
                            resources.getString(R.string.hash_coin) to binding.coinAmount.text.toString())

                        docRef.collection(firebaseAuth.currentUser!!.email.toString()).document(binding.awardName.text.toString()).set(award).addOnSuccessListener {
                            Snackbar.make(view, binding.awardName.text.toString() + resources.getString(R.string.prize_updated_as) + binding.coinAmount.text.toString() + resources.getString(R.string.coin_updated_as), Snackbar.LENGTH_LONG).show()
                            navController = findNavController()
                            navController.navigate(R.id.action_giftAwardsAddFragment_to_giftFragment)
                        }
                    }
                }
            }else{
                binding.awardsAddButton.setOnClickListener { awardsAddButtonView ->
                    if (binding.coinAmount.text.toString() == resources.getString(R.string.empty) && binding.awardName.text.toString() == resources.getString(R.string.empty)){
                        Snackbar.make(awardsAddButtonView, resources.getString(R.string.please_enter_award_and_coin), Snackbar.LENGTH_LONG).show()
                    }else if (binding.coinAmount.text.toString() != resources.getString(R.string.empty) && binding.awardName.text.toString() == resources.getString(R.string.empty)){
                        Snackbar.make(awardsAddButtonView, resources.getString(R.string.please_enter_two) + binding.coinAmount.text.toString() + resources.getString(R.string.please_worth_coin_enter_prize), Snackbar.LENGTH_LONG).show()
                    }else if (binding.coinAmount.text.toString() == resources.getString(R.string.empty) && binding.awardName.text.toString() != resources.getString(R.string.empty)){
                        Snackbar.make(awardsAddButtonView, resources.getString(R.string.please_enter_one) + binding.awardName.text.toString() + resources.getString(R.string.please_prize_for_enter_coin), Snackbar.LENGTH_LONG).show()
                    }else{
                        val award = hashMapOf(
                            resources.getString(R.string.hash_award) to binding.awardName.text.toString(),
                            resources.getString(R.string.hash_coin) to binding.coinAmount.text.toString())

                        docRef.collection(firebaseAuth.currentUser!!.email.toString()).document(binding.awardName.text.toString()).set(award).addOnSuccessListener {
                            Snackbar.make(view, binding.awardName.text.toString() + resources.getString(R.string.prize_has_been_added), Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_giftAwardsAddFragment_to_giftFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}