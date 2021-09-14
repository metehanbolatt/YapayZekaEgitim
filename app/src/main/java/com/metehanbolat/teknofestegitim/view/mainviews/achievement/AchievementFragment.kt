package com.metehanbolat.teknofestegitim.view.mainviews.achievement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentAchievementBinding

class AchievementFragment : Fragment() {

    private var _binding : FragmentAchievementBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var navController : NavController

    private lateinit var achievementList : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        val view = binding.root

        firestore = Firebase.firestore
        firebaseAuth = Firebase.auth

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_achievementFragment_to_mainFragment)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.recycler_achievement_background)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        achievementList = ArrayList()

        val docRef = firestore.collection(resources.getString(R.string.firebase_userData)).document(firebaseAuth.currentUser!!.email.toString())

        docRef.get().addOnSuccessListener { document ->

            val doc = document[resources.getString(R.string.firebase_first_achievement)]

            if (doc.toString() == resources.getString(R.string._one)){
                achievementList.add(resources.getString(R.string.computer_vision))

                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
                val achievementAdapter = AchievementRecyclerAdapter(achievementList)
                binding.recyclerView.adapter = achievementAdapter
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}