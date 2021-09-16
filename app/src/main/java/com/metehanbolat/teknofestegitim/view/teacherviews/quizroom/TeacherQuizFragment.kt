package com.metehanbolat.teknofestegitim.view.teacherviews.quizroom

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
import com.metehanbolat.teknofestegitim.databinding.FragmentTeacherQuizBinding

class TeacherQuizFragment : Fragment() {

    private var _binding : FragmentTeacherQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    private var counter = 0
    private var radioControl = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTeacherQuizBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.black)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                when(counter){
                    0 -> {
                        Snackbar.make(view,resources.getString(R.string.againClickExit), Snackbar.LENGTH_SHORT).show()
                        counter++
                    }
                    1 -> {
                        navController = findNavController()
                        navController.navigate(R.id.action_teacherQuizFragment_to_teacherMainFragment)
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        auth = Firebase.auth
        firestore = Firebase.firestore

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioButtonOne.setOnClickListener{
            binding.radioButtonOne.text = "Doğru"
            binding.radioButtonTwo.text = resources.getString(R.string.empty)
            binding.radioButtonThree.text = resources.getString(R.string.empty)
            binding.radioButtonFour.text = resources.getString(R.string.empty)
            radioControl = 1
        }

        binding.radioButtonTwo.setOnClickListener{
            binding.radioButtonOne.text = resources.getString(R.string.empty)
            binding.radioButtonTwo.text = "Doğru"
            binding.radioButtonThree.text = resources.getString(R.string.empty)
            binding.radioButtonFour.text = resources.getString(R.string.empty)
            radioControl = 2
        }

        binding.radioButtonThree.setOnClickListener{
            binding.radioButtonOne.text = resources.getString(R.string.empty)
            binding.radioButtonTwo.text = resources.getString(R.string.empty)
            binding.radioButtonThree.text = "Doğru"
            binding.radioButtonFour.text = resources.getString(R.string.empty)
            radioControl = 3
        }

        binding.radioButtonFour.setOnClickListener{
            binding.radioButtonOne.text = resources.getString(R.string.empty)
            binding.radioButtonTwo.text = resources.getString(R.string.empty)
            binding.radioButtonThree.text = resources.getString(R.string.empty)
            binding.radioButtonFour.text = "Doğru"
            radioControl = 4
        }

        binding.quizButton.setOnClickListener {

            if (binding.quizQuestionEditText.text.isNullOrBlank()){
                Snackbar.make(view, "Lütfen soru giriniz!!", Snackbar.LENGTH_LONG).show()
            }else{
                if (binding.quizAnswerOne.text.isNullOrBlank() || binding.quizAnswerTwo.text.isNullOrBlank() || binding.quizAnswerThree.text.isNullOrBlank() || binding.quizAnswerFour.text.isNullOrBlank()){
                    Snackbar.make(view, "Lütfen boş cevap bırakmayın", Snackbar.LENGTH_LONG).show()
                }else{
                    if (binding.radioButtonOne.text == "Doğru" || binding.radioButtonTwo.text == "Doğru" || binding.radioButtonThree.text == "Doğru" || binding.radioButtonFour.text == "Doğru"){
                        val questionMap = hashMapOf<String, Any>()
                        questionMap["question"] = binding.quizQuestionEditText.text.toString()
                        questionMap["answer_one"] = binding.quizAnswerOne.text.toString()
                        questionMap["answer_two"] = binding.quizAnswerTwo.text.toString()
                        questionMap["answer_three"] = binding.quizAnswerThree.text.toString()
                        questionMap["answer_four"] = binding.quizAnswerFour.text.toString()
                        questionMap["correct_answer"] = radioControl.toString()

                        firestore.collection("quizRoom").document("qwe123").collection("questions").document("question").update(questionMap).addOnSuccessListener {
                            Snackbar.make(view, "Soru öğrencilere gönderildi.", Snackbar.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Snackbar.make(view, "Soru gönderilirken hata oluştu", Snackbar.LENGTH_LONG).show()
                        }
                    }else{
                        Snackbar.make(view, "Şıklardan birini doğru olarak işaretlemelisiniz", Snackbar.LENGTH_LONG).show()
                    }
                }
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}